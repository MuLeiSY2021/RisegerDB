import pers.muleisy.rtree.RTreeDao;
import pers.muleisy.rtree.othertree.RStarTree;
import pers.muleisy.rtree.rectangle.MBRectangle;
import pers.muleisy.rtree.test.TestRectangle;

import java.io.IOException;
import java.util.List;

public class RStarTreeTest {
    public static void main(String[] args) {
        int size = 1600;

        RTreeDao<MBRectangle> tree = new RStarTree<>(4, 0.5, MBRectangle.class);
        List<MBRectangle> rectangles = ImagePrint.generateRectangles(size, 20);
        for (MBRectangle rectangle : rectangles) {
            tree.insert(rectangle);
        }

        System.out.println(tree);
        ImagePrint.toPNG(size, "model/RTreePrimary/src/test/resources/pngPicture/star_r_tree", "all" + rectangles.size(), tree);
        System.out.println(tree.getDeep());
        List<MBRectangle> list = tree.search(new TestRectangle(1235.0, 773.0, 1283.0, 924.0, 0.5));
        System.out.println(list);
        try {
            ImagePrint.toPNG(size, "model/RTreePrimary/src/test/resources/pngPicture/star_r_tree", "Search_N5&N6", list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //delete
        tree.delete(new TestRectangle(159.0, 614.0, 186.0, 641.0, 0.5));
        ImagePrint.toPNG(size, "model/RTreePrimary/src/test/resources/pngPicture/star_r_tree", "deleted_N0", tree);
    }

}