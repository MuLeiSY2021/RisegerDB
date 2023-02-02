package StoredDataManager.DataStruct.rTree;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.typography.general.GeneralFont;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.point.Point2dImpl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ImagePrint {
    private static final HashMap<Integer, Float[]> colorHashMap = new HashMap<>();


    public static void toPNG(String URL,String fileName,R_Tree tree) throws IOException {
        int depth = tree.getDepth();
        ConcurrentLinkedQueue<StoredDataManager.DataStruct.rTree.Rectangle> tuples = tree.toQueue4Layer();
        int[] size = tree.getMapSize();

        BufferedImage prevImage = new BufferedImage(size[0] + 40, size[1] + 40, BufferedImage.TYPE_INT_RGB);
        MBFImage image = ImageUtilities.createMBFImage(prevImage, false);
        image.fill(new Float[]{255f, 255f, 255f});
        for (StoredDataManager.DataStruct.rTree.Rectangle tuple : tuples) {
            if (tuple.getID() == null) {
                depth--;
                continue;
            }
            drawRectangles(image, tuple, depth);
        }
        File f = new File(URL + "/" + fileName +".png");
        f.createNewFile();
        ImageUtilities.write(image, "PNG", f);
        System.out.println("H:" + image.getHeight() + " W:" + image.getWidth());
        DisplayUtilities.display(image);
    }

    public static void toPNG(String URL, String fileName, Collection<? extends StoredDataManager.DataStruct.rTree.Rectangle> rectangles) throws IOException {
        int[] size = getMapSize(rectangles);

        BufferedImage prevImage = new BufferedImage(size[0] + 40, size[1] + 40, BufferedImage.TYPE_INT_RGB);
        MBFImage image = ImageUtilities.createMBFImage(prevImage, false);
        image.fill(new Float[]{255f, 255f, 255f});
        for (StoredDataManager.DataStruct.rTree.Rectangle tuple : rectangles) {
            drawRectangles(image, tuple, 2);
        }
        File f = new File(URL + "/" + fileName +".png");
        f.createNewFile();
        ImageUtilities.write(image, "PNG", f);
        System.out.println("H:" + image.getHeight() + " W:" + image.getWidth());
        DisplayUtilities.display(image);
    }

    private static int[] getMapSize(Collection<? extends StoredDataManager.DataStruct.rTree.Rectangle> rectangles) {
        int[] res = new int[]{Integer.MIN_VALUE,Integer.MIN_VALUE};
        for (StoredDataManager.DataStruct.rTree.Rectangle rectangle: rectangles) {
            if(rectangle.getMaxX() > res[0]) {
                res[0] = rectangle.getMaxX();
            }
            if(rectangle.getMaxY() > res[1]) {
                res[1] = rectangle.getMaxY();
            }
        }
        return res;
    }

    private static Float[] getRandomColor() {
        Random rand = new Random();
        Float[] res = new Float[3];
        for (; ; ) {
            res[0] = rand.nextFloat();
            res[1] = rand.nextFloat();
            res[2] = rand.nextFloat();
            if (!colorHashMap.containsKey(Arrays.hashCode(res))) {
                colorHashMap.put(Arrays.hashCode(res), res);
                return res;
            }
        }
    }

    private static void drawRectangles(MBFImage image, Rectangle rectangles, int thickness) {
        Point2d tl = new Point2dImpl(rectangles.getMinX() - thickness * 10, rectangles.getMinY() - thickness * 10);
        Point2d bl = new Point2dImpl(rectangles.getMinX() - thickness * 10, rectangles.getMaxY() + thickness * 10);
        Point2d br = new Point2dImpl(rectangles.getMaxX() + thickness * 10, rectangles.getMaxY() + thickness * 10);
        Point2d tr = new Point2dImpl(rectangles.getMaxX() + thickness * 10, rectangles.getMinY() - thickness * 10);
        Polygon polygon = new Polygon(Arrays.asList(tl, bl, br, tr));
        Float[] col = getRandomColor();
        image.drawPolygon(polygon, thickness * 3, col);
        tl.setX(tl.getX() - thickness * 4);
        tl.setY(tl.getY() - thickness * 4);
        if(rectangles.getID() != null) {
            image.drawText(rectangles.getID(), tl, new GeneralFont("Monospaced", Font.PLAIN), thickness * 14, col);
        }
    }

}
