package org.test.preload;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.reseger.jrdbc.utils.*;
import org.riseger.protocol.struct.entity.ParentModel;
import org.riseger.protocol.struct.entity.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class BuildPreloadTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BuildPreloadTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(BuildPreloadTest.class);
    }

    public static Map<String, String[]> initDataMap() {


        Map<String, String[]> map = new HashMap<>();
        String[] tianjin_areas = new String[]{"Hexi", "Heping", "Hedong"};
        map.put("Tianjin", tianjin_areas);
        String[] shanghai_areas = new String[]{"Huangpu", "Jing'an", "Xuhui"};
        map.put("ShangHai", shanghai_areas);
        String[] chongqing_areas = new String[]{"Yuzhong", "Jiangbei", "Shapingba"};
        map.put("Chongqing", chongqing_areas);

        return map;
    }

    public static List<Building> initDataBuildings(Map<String, String[]> map, String name, int i) {
        int count_sub = map.get(name).length;
        int x_min = 0;
        int y_min = 0;
        int x_max = 10000;
        int y_max = 10000;
        int count = 4;
        int overlapSize = 50;
        List<int[]> rectangles = RectangleGenerator.createRectangles(x_min, y_min, x_max, y_max, count, overlapSize);
        return BuildingGenerator.createBuildings(
                (double) rectangles.get(i)[0],
                (double) rectangles.get(i)[1],
                (double) rectangles.get(i)[2],
                (double) rectangles.get(i)[3],
                count_sub,
                name);
    }

    public static void main(String[] args) {
        PreloadBuilder builder = new PreloadBuilder();

        DatabaseBuilder databaseBuilder = builder.buildDatabase();
        databaseBuilder.name("test_db").build();

        MapBuilder mapBuilder = databaseBuilder.buildMap();
        mapBuilder.name("china_mp")
                .threshold(0.5)
                .nodeSize(4)
                .build();

        ModelBuilder modelBuilder = databaseBuilder.buildModel();
        modelBuilder.name("building_model")
                .parent(ParentModel.FIELD)
                .parameter("name", Type.STRING)
                .parameter("address", Type.STRING)
                .parameter("floorArea", Type.DOUBLE)
                .build();

        String[] province_names = new String[]{"ShangHai", "Tianjin", "Chongqing"};
        Map<String, String[]> map = initDataMap();
        int i = 0;
        for (String name : province_names) {
            List<Building> buildings = initDataBuildings(map, name, i);
            SubmapBuilder submapBuilder = mapBuilder.buildSubMap();
            submapBuilder
                    .name(name)
                    .scopePath("province_scope")
                    .build();

            int j = 0;
            for (String area_name : map.get(name)) {
                SubmapBuilder submap_area = submapBuilder.buildSubmap();
                submap_area
                        .name(area_name)
                        .scopePath("province_scope.area_scope")
                        .build();

                Building building = buildings.get(j);

                FieldBuilder fieldBuilder = submap_area.buildField();
                fieldBuilder.addModel("building_model")
                        .addCoord(building.getX_min(), building.getY_min())
                        .addCoord(building.getX_max(), building.getY_max())
                        .value("name", building.getName())
                        .value("address", building.getAddress())
                        .value("floorArea", String.valueOf(building.getFloorArea()))
                        .build();
                j++;
            }
            i++;
        }
        builder.write();
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }
}
