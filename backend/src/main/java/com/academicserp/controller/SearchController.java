package com.academicserp.controller;

import com.academicserp.dto.SearchRequest;
import com.academicserp.model.AnalysisResponse;
import com.academicserp.service.SearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyzeQuery(@Valid @RequestBody SearchRequest request) {
        try {
            AnalysisResponse response = searchService.analyzeQuery(
                    request.getQuery(),
                    request.getMaxResults()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Academic SERP Analyzer API is running");
    }
}
