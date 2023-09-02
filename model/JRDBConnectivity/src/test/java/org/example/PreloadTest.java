package org.example;

import org.resegerdb.jrdbc.driver.connector.Connector;
import org.resegerdb.jrdbc.driver.session.PreloadSession;

public class PreloadTest {
    public static void main(String[] args) throws Exception {
        Connector connector = Connector.connect("localhost", 10086);
        PreloadSession session = connector.preload();
        session.setUri("/home/MuLeiY9000P/IdeaProjects/RisegerDB/src/main/resources/dataSource/0.json");
        session.send();
        connector.close();
    }
}
