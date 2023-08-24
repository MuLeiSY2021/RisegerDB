import pers.muleisy.rtree.RTreeDao;
import pers.muleisy.rtree.othertree.RStarTree;
import pers.muleisy.rtree.rectangle.CommonRectangle;

import java.io.IOException;
import java.util.List;

public class RStarTreeTest {
    public static void main(String[] args) throws IOException {
        int size = 1600;

        RTreeDao<CommonRectangle> tree = new RStarTree(4,0.5);
        List<CommonRectangle> rectangles = ImagePrint.generateRectangles(size,20);
        for (CommonRectangle rectangle : rectangles) {
            tree.insert(rectangle);
        }

        try {
            System.out.println(tree);
            ImagePrint.toPNG(size,"model/RTreePrimary/src/test/resources/pngPicture/star_r_tree","all"+rectangles.size(),tree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(tree.getDeep());
        List<CommonRectangle> list =  tree.search(new CommonRectangle(1235.0, 773.0, 1283.0, 924.0));
        System.out.println(list);
        try {
            ImagePrint.toPNG(size,"model/RTreePrimary/src/test/resources/pngPicture/star_r_tree","Search_N5&N6",list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //delete
        tree.delete(new CommonRectangle(159.0, 614.0, 186.0, 641.0));
        try {
            ImagePrint.toPNG(size,"model/RTreePrimary/src/test/resources/pngPicture/star_r_tree","deleted_N0",tree);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}