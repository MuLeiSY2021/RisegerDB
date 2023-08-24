import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.typography.general.GeneralFont;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Polygon;
import pers.muleisy.rtree.RTreeDao;
import pers.muleisy.rtree.othertree.RTree;
import pers.muleisy.rtree.rectangle.MBRectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class ImagePrint {
    private static final HashMap<Integer, Float[]> colorHashMap = new HashMap<>();

    //1080p
    private static final int resolution = 2160;

    private static final Random random = new Random(0);

    public static List<MBRectangle> generateRectangles(int size, int numRectangles) {
        double minSize = Math.sqrt(size) / 2;
        double maxSize = Math.sqrt(size)*2;
        List<MBRectangle> rectangles = new LinkedList<>();


        while (rectangles.size() < numRectangles) {
            double x = random.nextDouble() * (size - maxSize); // 随机生成矩形左上角x坐标
            double y = random.nextDouble() * (size - maxSize); // 随机生成矩形左上角y坐标
            double r_width = random.nextDouble() * (maxSize - minSize) + minSize; // 随机生成矩形大小
            double r_height = random.nextDouble() * (maxSize - minSize) + minSize;

            MBRectangle rect = new TestRectangle( x, y, (x + r_width), (y + r_height),0.5);

            while (rect.intersects(rectangles)) {
                x = x + 1; // 线性探测，尝试放置在下一个位置
                if (x + r_width >= size) { // 如果超出了地图边界，就从地图的左边重新开始
                    x = 0;
                    y += 1;
                    if (y + r_height >= size) { // 如果超出了地图边界，就从地图的上边重新开始
                        y = 0;
                    }
                }
                rect.move(x, y);
            }
            rectangles.add(rect);

        }

        return rectangles;
    }

    public static void toPNG(int size,String URL, String fileName, RTreeDao<MBRectangle> tree) throws IOException {
        double K = resolution /size <= 0 ? 1 : resolution / (double) size;
        List<MBRectangle> tuples = tree.getAllNode4Test();
        //
        BufferedImage prevImage = new BufferedImage((int) (size * 1.2 * K), (int) (size * 1.2 * K), BufferedImage.TYPE_INT_RGB);
        MBFImage image = ImageUtilities.createMBFImage(prevImage, false);
        image.fill(new Float[]{255f, 255f, 255f});
        int depth = 5;
        for (MBRectangle tuple : tuples) {
            if (tuple instanceof RTree.TestSpace) {
                depth--;
                continue;
            }
            drawRectangles(image, tuple, depth,K);
        }
        File url = new File(URL);
        url.mkdirs();
        File f = new File(URL + "/" + fileName +".png");
        f.createNewFile();

        ImageUtilities.write(image, "PNG", f);
//        DisplayUtilities.display(image);
    }

    public static void toPNG(int size,String URL, String fileName, Collection<? extends MBRectangle> rectangles) throws IOException {
        double K = resolution /(double) size < 1 ? 1 : resolution / (double) size;
        BufferedImage prevImage = new BufferedImage((int) (size * 1.2 * K), (int) (size * 1.2 * K), BufferedImage.TYPE_INT_RGB);
        MBFImage image = ImageUtilities.createMBFImage(prevImage, false);
        image.fill(new Float[]{255f, 255f, 255f});
        for (MBRectangle tuple : rectangles) {
            drawRectangles(image, tuple, 2,K);
        }
        File f = new File(URL + "/" + fileName +".png");
        f.createNewFile();

        ImageUtilities.write(image, "PNG", f);
//        DisplayUtilities.display(image);
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
    static int i = 1;

    static HashMap<MBRectangle,Integer> hashMap = new HashMap<>();

    private static void drawRectangles(MBFImage image, MBRectangle rectangles, int thickness, double K) {
        int minX = (int) ((rectangles.minX() - thickness * 5 + 50) * K);
        int minY = (int) ((rectangles.minY() - thickness * 5 + 50) * K);
        int maxX = (int) ((rectangles.maxX() + thickness * 5 + 50) * K);
        int maxY = (int) ((rectangles.maxY() + thickness * 5 + 50) * K);
        Point2d tl = new Point2dImpl(minX, minY);
        Point2d bl = new Point2dImpl(minX, maxY);
        Point2d br = new Point2dImpl(maxX, maxY);
        Point2d tr = new Point2dImpl(maxX, minY);
        Polygon polygon = new Polygon(Arrays.asList(tl, bl, br, tr));
        Float[] col = getRandomColor();
        image.drawPolygon(polygon, (int) (4 * K), col);
        tl.setX(tl.getX() - 4);
        tl.setY(tl.getY() - 4);
        if(!hashMap.containsKey(rectangles)) {
            hashMap.put(rectangles,i);
            i++;
        }

        String s = RTree.getStringBuilder(rectangles,hashMap);
        image.drawText(s, tl, new GeneralFont("Monospaced", Font.PLAIN), (int)(14 * K), col);
    }

}
