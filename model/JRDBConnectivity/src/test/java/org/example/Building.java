package org.example;

import lombok.Data;

@Data
public class Building {

    Double x_min;
    Double y_min;
    Double x_max;
    Double y_max;

    String name;
    String address;
    Double floorArea;

    Building(Double x_min, Double y_min, Double x_max, Double y_max, String name, String address, Double floorArea) {
        this.x_min = x_min;
        this.y_min = y_min;
        this.x_max = x_max;
        this.y_max = y_max;
        this.name = name;
        this.address = address;
        this.floorArea = floorArea;
    }
}