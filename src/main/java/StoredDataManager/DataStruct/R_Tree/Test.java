package StoredDataManager.DataStruct.R_Tree;


import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {

        R_Tree<Rectangle> tree = new R_Tree<>();
        Random r =new Random(1);
        for (int i = 0; i < 5; i++) {
            int minX = r.nextInt(501) + 1;
            int maxX = r.nextInt(501) + minX + 1;
            int minY = r.nextInt(501) + 1;
            int maxY = r.nextInt(501) + minX + 1;
            Rectangle rectangle = new BaseRectangle(minX,minY,maxX,maxY);

            tree.insert(rectangle);
            try {
                ImagePrint.toPNG(
                        "src/resources/pngPicture","add_N"+i,tree);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(tree);
            System.out.println();
        }
        LinkedList<Rectangle> list =  tree.get(new BaseRectangle(186,529,358,671));
        System.out.println(list);
        try {
            ImagePrint.toPNG(
                    "src/resources/pngPicture","Search_N5&N6",list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tree.delete(new BaseRectangle(408,279,572,306));
        try {
            ImagePrint.toPNG("src/resources/pngPicture","deleted_N0",tree);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}