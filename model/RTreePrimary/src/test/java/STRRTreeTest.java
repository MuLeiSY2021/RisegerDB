import pers.muleisy.rtree.RTreeDao;
import pers.muleisy.rtree.othertree.STRRTree;
import pers.muleisy.rtree.rectangle.MBRectangle;

import java.util.List;

public class STRRTreeTest {
    public static void main(String[] args) {
        int size = 1600;

        RTreeDao<MBRectangle> tree = new STRRTree<>(4, 0.5, MBRectangle.class);
        List<MBRectangle> rectangles = ImagePrint.generateRectangles(size, 20);
        tree.insertAll(rectangles);

        System.out.println(tree);
        ImagePrint.toPNG(size, "model/RTreePrimary/src/test/resources/pngPicture/str_r_tree", "all" + rectangles.size(), tree);

    }


}