package org.test.preload;

import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.PreloadSession;
import org.riseger.protoctl.packet.response.PreloadResponse;

public class PreloadTest {
    public static void main(String[] args) throws Exception {
        Connection connection = Connection.connect("localhost", 10086);

        PreloadSession session = connection.preload();
        session.setUri("/home/MuLeiY9000P/IdeaProjects/RisegerDB/src/main/resources/dataSource/0.json");
        PreloadResponse result = session.send();

        System.out.println(result);

        connection.close();
    }
}
