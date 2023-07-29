package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuildingGenerator {

    // Function to generate random double value within a specified range
    private static double getRandomInRange(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    // Function to check if two buildings overlap
    private static boolean doBuildingsOverlap(Building building1, Building building2, int overlapSize) {
        double x_overlap = Math.max(0, Math.min(building1.x_max, building2.x_max) - Math.max(building1.x_min, building2.x_min));
        double y_overlap = Math.max(0, Math.min(building1.y_max, building2.y_max) - Math.max(building1.y_min, building2.y_min));

        double overlapArea = x_overlap * y_overlap;
        return overlapArea >= overlapSize;
    }

    // Function to generate non-overlapping buildings
    public static List<Building> createBuildings(Double x_min, Double y_min, Double x_max, Double y_max, int count,
                                                 String addressPrefix, int overlapSize) {
        List<Building> buildings = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            double randomXMin = getRandomInRange(x_min, x_max);
            double randomYMin = getRandomInRange(y_min, y_max);
            double randomXMax = getRandomInRange(randomXMin, x_max);
            double randomYMax = getRandomInRange(randomYMin, y_max);

            Building newBuilding = new Building(randomXMin, randomYMin, randomXMax, randomYMax,
                    "Building " + (i + 1), addressPrefix + (i + 1), getRandomInRange(1000, 5000));

            boolean isOverlapping = false;
            for (Building existingBuilding : buildings) {
                if (doBuildingsOverlap(newBuilding, existingBuilding, overlapSize)) {
                    isOverlapping = true;
                    break;
                }
            }

            if (!isOverlapping) {
                buildings.add(newBuilding);
            }
        }

        return buildings;
    }

    public static void main(String[] args) {
        Double x_min = 0.0;
        Double y_min = 0.0;
        Double x_max = 100.0;
        Double y_max = 100.0;
        int count = 10;
        String addressPrefix = "Address ";
        int overlapSize = 50;

        List<Building> buildings = createBuildings(x_min, y_min, x_max, y_max, count, addressPrefix, overlapSize);

        // Print the generated buildings
        for (Building building : buildings) {
            System.out.println("Building Name: " + building.name);
            System.out.println("Address: " + building.address);
            System.out.println("Floor Area: " + building.floorArea);
            System.out.println("X_min: " + building.x_min + ", Y_min: " + building.y_min);
            System.out.println("X_max: " + building.x_max + ", Y_max: " + building.y_max);
            System.out.println("---------------------------------------------------");
        }
    }
}
