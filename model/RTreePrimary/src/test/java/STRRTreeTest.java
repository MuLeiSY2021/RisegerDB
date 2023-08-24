import pers.muleisy.rtree.RTreeDao;
import pers.muleisy.rtree.othertree.STRRTree;
import pers.muleisy.rtree.rectangle.CommonRectangle;

import java.io.IOException;
import java.util.List;

public class STRRTreeTest {
    public static void main(String[] args) throws IOException {
        int size = 1600;

        RTreeDao<CommonRectangle> tree = new STRRTree<>(4,0.5);
        List<CommonRectangle> rectangles = ImagePrint.generateRectangles(size,20);
        tree.insertAll(rectangles);

        try {
            System.out.println(tree);
            ImagePrint.toPNG(size,"model/RTreePrimary/src/test/resources/pngPicture/str_r_tree","all"+rectangles.size(),tree);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}