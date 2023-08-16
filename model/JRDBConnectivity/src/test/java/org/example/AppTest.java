package org.example;

import com.google.gson.Gson;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.resegerdb.jrdbc.command.preload.builder.*;
import org.resegerdb.jrdbc.driver.connector.Connector;
import org.resegerdb.jrdbc.driver.session.PreloadSession;
import org.resegerdb.jrdbc.driver.result.Result;
import org.resegerdb.jrdbc.struct.model.Type;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public static Map<String,String[]> initDataMap() {


        Map<String,String[]> map = new HashMap<>();
        String[] tianjin_areas = new String[]{"Hexi","Heping","Hedong"};
        map.put("tianjin",tianjin_areas);
        String[] shanghai_areas = new String[]{"Huangpu", "Jing'an", "Xuhui"};
        map.put("ShangHai", shanghai_areas);
        String[] chongqing_areas = new String[]{"Yuzhong", "Jiangbei", "Shapingba"};
        map.put("Chongqing", chongqing_areas);

        return map;
    }

    public static List<Building> initDataBuildings(Map<String,String[]> map,String name,int i) {
        int count_sub = map.get(name).length;
        int overlapSize_d = 10;
        int x_min = 0;
        int y_min = 0;
        int x_max = 10000;
        int y_max = 10000;
        int count = 4;
        int overlapSize = 50;
        List<int[]> rectangles = RectangleGenerator.createRectangles(x_min, y_min, x_max, y_max, count, overlapSize);
        List<Building> buildings = BuildingGenerator.createBuildings(
                (double) rectangles.get(i)[0],
                (double) rectangles.get(i)[1],
                (double) rectangles.get(i)[2],
                (double) rectangles.get(i)[3],
                count_sub,
                name,
                overlapSize_d);
        return buildings;
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        Connector connector = Connector.connet("localhost",10086);

        PreloadSession preloadSession = connector.preload();

        DatabaseBuilder databaseBuilder = preloadSession.buildDatabase();
        databaseBuilder.name("test_db").build();

        MapBuilder mapBuilder = databaseBuilder.buildMap();
        mapBuilder.name("china_mp").build();

        ScopeBuilder scopeBuilder = databaseBuilder.buildScope();
        scopeBuilder.name("province_scope").build();
        scopeBuilder.child().name("area_scope").build();

        ModelBuilder modelBuilder = databaseBuilder.buildModel();
        modelBuilder.name("building_model")
                .parameter("name", Type.STRING)
                .parameter("address", Type.STRING)
                .parameter("floorArea", Type.DOUBLE)
                .build();

        String[] province_names = new String[]{"Beijing","ShangHai","Tianjin","Chongqing"};
        Map<String,String[]> map = initDataMap();
        int i = 0;
        for (String name:province_names) {
            List<Building> buildings = initDataBuildings(map,name,i);
            SubmapBuilder submapBuilder = mapBuilder.buildSubMap();
            submapBuilder.name(name)
                    .scopePath("province_scope").build();

            int j = 0;
            for (String area_name:map.get(name)) {
                SubmapBuilder submap_area = submapBuilder.buildSubmap();
                submap_area.name(area_name).build();

                Building building = buildings.get(j);

                FieldBuilder fieldBuilder = submap_area.buildField();
                fieldBuilder.addCoord(building.getX_min(),building.getY_min())
                        .addCoord(building.getX_max(),building.getY_max())
                        .value("name", building.getName())
                        .value("address", building.getAddress())
                        .value("floorArea", String.valueOf(building.getFloorArea()))
                        .build();
                j++;
            }
            i++;
        }

        Result result = preloadSession.send();

        System.out.println(new Gson().toJson(result));

        connector.close();


    }
}
