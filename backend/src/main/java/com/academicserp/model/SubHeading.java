package com.academicserp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubHeading {
    private String name;
    private String description;
    private int count;
    private String category;
    private double percentage;
}
