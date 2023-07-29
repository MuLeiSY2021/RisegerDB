package org.example;

import com.google.gson.Gson;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.resegerdb.jrdbc.command.preload.*;
import org.resegerdb.jrdbc.driver.Connection;
import org.resegerdb.jrdbc.driver.PreloadSession;
import org.resegerdb.jrdbc.driver.Result;
import org.resegerdb.jrdbc.struct.model.Type;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
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

    public static void main(String[] args) throws IOException {
        Connection connection = new Connection();
        connection.connect("localhost","10086");
        PreloadSession preloadSession = connection.preloadSession();

        //创建数据
        Database test_db = new Database("test_db");
        preloadSession.addDatabase(test_db);

        MapDB china_mp = new MapDB("china_mp",test_db);
        test_db.addMap(china_mp);

        Scope province_scope = new Scope("province_scope",test_db);
        Scope area_scope = new Scope("area_scope",province_scope,test_db);
        Model building_model = new Model("building_model",test_db);
        building_model.addParameter("name", Type.STRING);
        building_model.addParameter("address", Type.STRING);
        building_model.addParameter("floorArea", Type.DOUBLE);
        test_db.addModel(building_model);

        String[] province_names = new String[]{"Beijing","ShangHai","Tianjin","Chongqing"};

        Map<String,String[]> map = new HashMap<>();
        String[] tianjin_areas = new String[]{"Hexi","Heping","Hedong"};
        map.put("tianjin",tianjin_areas);
        String[] shanghai_areas = new String[]{"Huangpu", "Jing'an", "Xuhui"};
        map.put("ShangHai", shanghai_areas);
        String[] chongqing_areas = new String[]{"Yuzhong", "Jiangbei", "Shapingba"};
        map.put("Chongqing", chongqing_areas);

        int x_min = 0;
        int y_min = 0;
        int x_max = 10000;
        int y_max = 10000;
        int count = 4;
        int overlapSize = 50;

        List<int[]> rectangles = RectangleGenerator.createRectangles(x_min, y_min, x_max, y_max, count, overlapSize);
        int i = 0;
        for (String name:province_names) {
            SubMap submap = new SubMap(name,province_scope,test_db);
            int count_sub = map.get(name).length;
            int overlapSize_d = 10;

            List<Building> buildings = BuildingGenerator.createBuildings(
                    (double) rectangles.get(i)[0],
                    (double) rectangles.get(i)[1],
                    (double) rectangles.get(i)[2],
                    (double) rectangles.get(i)[3],
                    count_sub,
                    name,
                    overlapSize_d);
            int j = 0;
            for (String area_name:map.get(name)) {
                SubMap submap_area = new SubMap(area_name,area_scope,test_db);
                Building building = buildings.get(j);
                Element element = new Element(test_db,submap,building_model,area_scope);
                element.setValue("x_min", String.valueOf(building.getX_min()));
                element.setValue("y_min", String.valueOf(building.getY_min()));
                element.setValue("x_max", String.valueOf(building.getX_max()));
                element.setValue("y_max", String.valueOf(building.getY_max()));

                element.setValue("name", building.getName());
                element.setValue("address", building.getAddress());
                element.setValue("floorArea", String.valueOf(building.getFloorArea()));
                submap_area.addElement(element);

                submap.addSubMap(submap_area);
                j++;
            }
            china_mp.addSubMap(submap);
            i++;
        }


        Result result = preloadSession.send();
        System.out.println(new Gson().toJson(result));

        connection.close();


    }
}
