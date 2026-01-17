package org.abbtech.module3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Car {
    private String name;
    private String color;
    private int year;
    private double price;
    private Long id;
}