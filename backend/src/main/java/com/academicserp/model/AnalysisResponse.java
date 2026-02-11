package com.academicserp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResponse {
    private String query;
    private int totalResults;
    private List<SearchResult> searchResults;
    private List<SubHeading> subHeadings;
    private long processingTimeMs;
}
