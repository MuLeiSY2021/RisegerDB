package org.test.preload;

import org.reseger.jrdbc.driver.connector.Connector;
import org.reseger.jrdbc.driver.session.PreloadSession;
import org.riseger.protoctl.response.PreloadResponse;

public class PreloadTest {
    public static void main(String[] args) throws Exception {
        Connector connector = Connector.connect("localhost", 10086);

        PreloadSession session = connector.preload();
        session.setUri("/home/MuLeiY9000P/IdeaProjects/RisegerDB/src/main/resources/dataSource/0.json");
        PreloadResponse result = (PreloadResponse) session.send();

        System.out.println(result);

        connector.close();
    }
}
