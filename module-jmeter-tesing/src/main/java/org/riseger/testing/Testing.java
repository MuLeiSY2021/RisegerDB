package org.riseger.testing;

import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.TextSQLMessageSession;
import org.riseger.protocol.packet.RequestType;
import org.riseger.protocol.packet.response.TextSQLResponse;

public class Testing {
    public static void main(String[] args) {
        try {
            Connection connection = Connection.connect("localhost", 12000);

            TextSQLMessageSession statement = connection.sqlText();

            statement.setSqlText("USE\n" +
                    "  DATABASE 'test_db'|\n" +
                    "  MAP 'china_mp'|\n" +
                    "  SCOPE RECT(\n" +
                    "    [1, 2],\n" +
                    "    20000\n" +
                    "  )|\n" +
                    "  MODEL province_scope.area_scope.building_model\n" +
                    "SEARCH\n" +
                    "  building_model.KEY_LOOP,\n" +
                    "  building_model.name\n" +
                    "WHERE\n" +
                    "  IN RECT(\n" +
                    "    [4000, 1000],\n" +
                    "    5000\n" +
                    "  )\n" +
                    "  OR OUT RECT(\n" +
                    "    [777.5, 72658.1],\n" +
                    "    45.2\n" +
                    "  )\n" +
                    "  AND building_model.floorArea > 10;");
            statement.setType(RequestType.SHELL);
            //发送查询请求，并接收结果
            TextSQLResponse response = statement.send(1000);

            //输出结果
            System.out.println(response);

        } catch (Exception e) {
        }
    }
}
