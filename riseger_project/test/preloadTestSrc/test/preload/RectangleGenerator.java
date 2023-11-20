package test.preload;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RectangleGenerator {

    // Function to generate random integer value within a specified range
    private static int getRandomInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    // Function to check if two rectangles overlap
    private static boolean doRectanglesOverlap(int[] rect1, int[] rect2, int overlapSize) {
        int x_overlap = Math.max(0, Math.min(rect1[2], rect2[2]) - Math.max(rect1[0], rect2[0]));
        int y_overlap = Math.max(0, Math.min(rect1[3], rect2[3]) - Math.max(rect1[1], rect2[1]));

        int overlapArea = x_overlap * y_overlap;
        return overlapArea >= overlapSize;
    }

    // Function to generate non-overlapping rectangles
    public static List<int[]> createRectangles(int x_min, int y_min, int x_max, int y_max, int count, int overlapSize) {
        List<int[]> rectangles = new ArrayList<>();
        int i = 0;

        do {
            int randomXMin = getRandomInRange(x_min, x_max);
            int randomYMin = getRandomInRange(y_min, y_max);
            int randomWidth = getRandomInRange(1, x_max - randomXMin);
            int randomHeight = getRandomInRange(1, y_max - randomYMin);
            int randomXMax = randomXMin + randomWidth;
            int randomYMax = randomYMin + randomHeight;

            int[] newRectangle = {randomXMin, randomYMin, randomXMax, randomYMax};

            boolean isOverlapping = false;
            for (int[] existingRectangle : rectangles) {
                if (doRectanglesOverlap(newRectangle, existingRectangle, overlapSize)) {
                    isOverlapping = true;
                    break;
                }
            }

            if (!isOverlapping) {
                rectangles.add(newRectangle);
                i++;
            }
        } while (i != count);

        return rectangles;
    }

    public static void main(String[] args) {
        int x_min = 0;
        int y_min = 0;
        int x_max = 100;
        int y_max = 100;
        int count = 10;
        int overlapSize = 50;

        List<int[]> rectangles = createRectangles(x_min, y_min, x_max, y_max, count, overlapSize);

        // Print the generated rectangles
        for (int i = 0; i < rectangles.size(); i++) {
            int[] rect = rectangles.get(i);
            System.out.println("Rectangle " + (i + 1) + ":");
            System.out.println("X_min: " + rect[0] + ", Y_min: " + rect[1]);
            System.out.println("X_max: " + rect[2] + ", Y_max: " + rect[3]);
            System.out.println("---------------------------------------------------");
        }
    }
}
