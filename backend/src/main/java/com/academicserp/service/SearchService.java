package com.academicserp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.academicserp.model.AnalysisResponse;
import com.academicserp.model.SubHeading;
import com.academicserp.model.SearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Value("${serper.api.key:YOUR_SERPER_API_KEY}")
    private String serperApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public AnalysisResponse analyzeQuery(String query, int maxResults) throws Exception {
        long startTime = System.currentTimeMillis();

        // Fetch SERP results
        List<SearchResult> searchResults = fetchSearchResults(query, maxResults);

        // Extract sub-headings using multithreading
        List<SubHeading> subHeadings = extractSubHeadingsMultithreaded(searchResults, query);

        long processingTime = System.currentTimeMillis() - startTime;

        return new AnalysisResponse(
                query,
                searchResults.size(),
                searchResults,
                subHeadings,
                processingTime
        );
    }

    private List<SearchResult> fetchSearchResults(String query, int maxResults) throws Exception {
        // Serper API endpoint
        String url = "https://google.serper.dev/search";
        
        // Create JSON request body
        String requestBody = String.format(
            "{\"q\":\"%s\",\"num\":%d}",
            query.replace("\"", "\\\""), 
            Math.min(maxResults, 10)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-API-KEY", serperApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Serper API returned status: " + response.statusCode());
            System.out.println("Using mock data fallback...");
            // Fallback to mock data for demo purposes
            return generateMockResults(query, maxResults);
        }

        return parseSerperResults(response.body());
    }

    private List<SearchResult> parseSerperResults(String jsonResponse) throws Exception {
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode organicResults = root.get("organic");

        if (organicResults == null || !organicResults.isArray()) {
            System.out.println("No organic results found in Serper response");
            return new ArrayList<>();
        }

        List<SearchResult> results = new ArrayList<>();
        for (int i = 0; i < organicResults.size(); i++) {
            JsonNode item = organicResults.get(i);
            
            String title = item.has("title") ? item.get("title").asText() : "";
            String link = item.has("link") ? item.get("link").asText() : "";
            String snippet = item.has("snippet") ? item.get("snippet").asText() : "";
            
            // Extract domain from link
            String displayLink = "";
            if (!link.isEmpty()) {
                try {
                    URI uri = new URI(link);
                    displayLink = uri.getHost();
                    if (displayLink != null && displayLink.startsWith("www.")) {
                        displayLink = displayLink.substring(4);
                    }
                } catch (Exception e) {
                    displayLink = link;
                }
            }
            
            SearchResult result = new SearchResult(
                    title,
                    link,
                    snippet,
                    displayLink,
                    i + 1
            );
            results.add(result);
        }

        return results;
    }

    private List<SearchResult> generateMockResults(String query, int count) {
        List<SearchResult> results = new ArrayList<>();
        String[] sources = {"arXiv", "IEEE Xplore", "ACM Digital Library", "Springer", "Nature", 
                          "Science Direct", "JMLR", "NeurIPS", "ICML", "CVPR"};
        String[] topics = {"Transformer Models", "CNN Architectures", "RNN Applications", 
                          "GANs", "Reinforcement Learning", "Transfer Learning",
                          "Neural Architecture Search", "Attention Mechanisms",
                          "Meta-Learning", "Few-Shot Learning"};
        
        for (int i = 0; i < Math.min(count, 10); i++) {
            results.add(new SearchResult(
                    topics[i % topics.length] + " in Deep Learning: A Comprehensive Study",
                    "https://arxiv.org/abs/2024." + (1000 + i),
                    "This paper presents a novel approach to " + topics[i % topics.length].toLowerCase() + 
                    ". We introduce our methodology, describe the experimental setup, present results on benchmark datasets, " +
                    "discuss the findings, and outline future work. Our architecture demonstrates state-of-the-art performance.",
                    sources[i % sources.length].toLowerCase().replace(" ", ""),
                    i + 1
            ));
        }
        
        return results;
    }

    private List<SubHeading> extractSubHeadingsMultithreaded(List<SearchResult> results, String query) {
        // Create tasks for analyzing each result
        List<CompletableFuture<Map<String, Integer>>> futures = results.stream()
                .map(result -> CompletableFuture.supplyAsync(
                        () -> analyzeResult(result, query),
                        executorService
                ))
                .collect(Collectors.toList());

        // Wait for all tasks to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        try {
            allFutures.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Aggregate results
        Map<String, Integer> headingCounts = new ConcurrentHashMap<>();
        Map<String, String> headingDescriptions = new ConcurrentHashMap<>();
        Map<String, String> headingCategories = new ConcurrentHashMap<>();

        initializeHeadingMetadata(headingDescriptions, headingCategories);

        for (CompletableFuture<Map<String, Integer>> future : futures) {
            try {
                Map<String, Integer> resultHeadings = future.get();
                resultHeadings.forEach((heading, count) ->
                        headingCounts.merge(heading, count, Integer::sum)
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Convert to SubHeading objects and sort by count
        int totalResults = results.size();
        return headingCounts.entrySet().stream()
                .map(entry -> new SubHeading(
                        entry.getKey(),
                        headingDescriptions.getOrDefault(entry.getKey(), "Common section in academic papers"),
                        entry.getValue(),
                        headingCategories.getOrDefault(entry.getKey(), "General"),
                        (entry.getValue() * 100.0) / totalResults
                ))
                .sorted(Comparator.comparingInt(SubHeading::getCount).reversed())
                .collect(Collectors.toList());
    }

    private Map<String, Integer> analyzeResult(SearchResult result, String query) {
        Map<String, Integer> headings = new HashMap<>();

        String combinedText = (result.getTitle() + " " + result.getSnippet()).toLowerCase();

        // Detect academic paper sub-headings
        if (combinedText.contains("abstract") || combinedText.matches(".*\\babstract\\b.*")) {
            headings.put("Abstract", 1);
        }
        if (combinedText.contains("introduction") || combinedText.contains("background")) {
            headings.put("Introduction", 1);
        }
        if (combinedText.contains("related work") || combinedText.contains("literature review") || 
            combinedText.contains("prior work") || combinedText.contains("previous work")) {
            headings.put("Related Work", 1);
        }
        if (combinedText.contains("methodolog") || combinedText.contains("method") || 
            combinedText.contains("approach") || combinedText.contains("framework")) {
            headings.put("Methodology", 1);
        }
        if (combinedText.contains("architecture") || combinedText.contains("model") || 
            combinedText.contains("network") || combinedText.contains("design")) {
            headings.put("Architecture", 1);
        }
        if (combinedText.contains("experiment") || combinedText.contains("setup") || 
            combinedText.contains("evaluation")) {
            headings.put("Experiments", 1);
        }
        if (combinedText.contains("result") || combinedText.contains("finding") || 
            combinedText.contains("performance") || combinedText.contains("accuracy")) {
            headings.put("Results", 1);
        }
        if (combinedText.contains("discussion") || combinedText.contains("analysis") || 
            combinedText.contains("interpretation")) {
            headings.put("Discussion", 1);
        }
        if (combinedText.contains("conclusion") || combinedText.contains("summary")) {
            headings.put("Conclusion", 1);
        }
        if (combinedText.contains("reference") || combinedText.contains("citation") || 
            combinedText.contains("bibliograph")) {
            headings.put("References", 1);
        }
        if (combinedText.contains("future work") || combinedText.contains("future direction") || 
            combinedText.contains("future research")) {
            headings.put("Future Work", 1);
        }
        if (combinedText.contains("limitation") || combinedText.contains("constraint") || 
            combinedText.contains("challenge")) {
            headings.put("Limitations", 1);
        }
        if (combinedText.contains("dataset") || combinedText.contains("data collection") || 
            combinedText.contains("benchmark")) {
            headings.put("Datasets", 1);
        }
        if (combinedText.contains("implementation") || combinedText.contains("code") || 
            combinedText.contains("detail")) {
            headings.put("Implementation", 1);
        }
        if (combinedText.contains("contribution") || combinedText.contains("novel")) {
            headings.put("Contributions", 1);
        }
        if (combinedText.contains("ablation") || combinedText.contains("component analysis")) {
            headings.put("Ablation Study", 1);
        }
        if (combinedText.contains("baseline") || combinedText.contains("comparison") || 
            combinedText.contains("state-of-the-art")) {
            headings.put("Baselines", 1);
        }
        if (combinedText.contains("hyperparameter") || combinedText.contains("tuning") || 
            combinedText.contains("training detail")) {
            headings.put("Training Details", 1);
        }

        return headings;
    }

    private void initializeHeadingMetadata(Map<String, String> descriptions, Map<String, String> categories) {
        // Descriptions
        descriptions.put("Abstract", "Brief summary of the entire paper");
        descriptions.put("Introduction", "Background and motivation for the research");
        descriptions.put("Related Work", "Review of existing literature and prior research");
        descriptions.put("Methodology", "Detailed description of research methods");
        descriptions.put("Architecture", "Model structure and technical design");
        descriptions.put("Experiments", "Experimental setup and evaluation procedures");
        descriptions.put("Results", "Findings and performance metrics");
        descriptions.put("Discussion", "Interpretation and analysis of results");
        descriptions.put("Conclusion", "Summary of findings and final remarks");
        descriptions.put("References", "Citations and bibliography");
        descriptions.put("Future Work", "Proposed directions for future research");
        descriptions.put("Limitations", "Acknowledged constraints and limitations");
        descriptions.put("Datasets", "Data sources and benchmark information");
        descriptions.put("Implementation", "Technical implementation details");
        descriptions.put("Contributions", "Key contributions of the research");
        descriptions.put("Ablation Study", "Component-wise performance analysis");
        descriptions.put("Baselines", "Comparison with existing methods");
        descriptions.put("Training Details", "Hyperparameters and training configuration");

        // Categories
        categories.put("Abstract", "Paper Structure");
        categories.put("Introduction", "Paper Structure");
        categories.put("Conclusion", "Paper Structure");
        categories.put("Related Work", "Research Content");
        categories.put("Methodology", "Research Content");
        categories.put("Results", "Research Content");
        categories.put("Architecture", "Technical Details");
        categories.put("Implementation", "Technical Details");
        categories.put("Datasets", "Technical Details");
        categories.put("Training Details", "Technical Details");
        categories.put("Discussion", "Analysis");
        categories.put("Limitations", "Analysis");
        categories.put("Future Work", "Analysis");
        categories.put("Ablation Study", "Analysis");
        categories.put("References", "Supporting");
        categories.put("Experiments", "Research Content");
        categories.put("Contributions", "Research Content");
        categories.put("Baselines", "Research Content");
    }
}
