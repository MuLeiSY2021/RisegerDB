package org.test.search;

import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.SearchSession;
import org.riseger.protoctl.packet.response.SearchResponse;
import org.riseger.protoctl.search.function.Entity_f;
import org.riseger.protoctl.search.function.entity.Attribute_f;
import org.riseger.protoctl.search.function.logic.LOGIC_F;
import org.riseger.protoctl.search.function.math.Math_F;

public class SearchTest {
    public static void main(String[] args) throws InterruptedException {
        Connection connection = Connection.connect("localhost", 10086);
        try {

            SearchSession session = connection.search();
            session.use()
                    .useDatabase("test_db")
                    .useMap("china_mp")
                    .useScope(FIELD_F.RECT().invoke(
                            FIELD_F.COORD().invoke(
                                    Entity_f.use(1), Entity_f.use(2)),
                            Entity_f.use(20000)))
                    .useModel("province_scope.area_scope.building_model")
                    .search()
                    .attr("building_model.KEY_LOOP")
                    .attr("building_model.name")
                    .where()
                    .condition(
                            LOGIC_F.OR().invoke(
                                    LOGIC_F.AND().invoke(
                                            GRAPHIC_F.IN().invoke(
                                                    FIELD_F.RECT().invoke(
                                                            FIELD_F.COORD().invoke(
                                                                    Entity_f.use(4000),
                                                                    Entity_f.use(1000)
                                                            ),
                                                            Entity_f.use(5000)
                                                    )
                                            ),
                                            GRAPHIC_F.OUT().invoke(
                                                    FIELD_F.RECT().invoke(
                                                            FIELD_F.COORD().invoke(
                                                                    Entity_f.use(777.5),
                                                                    Entity_f.use(72658.1)
                                                            ),
                                                            Entity_f.use(45.2)
                                                    )
                                            )
                                    ),
                                    Math_F.BIG().invoke(
                                            Attribute_f.use(Entity_f.use("building_model.floorArea")),
                                            Entity_f.use(1000)
                                    )
                            )
                    );
            System.out.println("发送了");
            SearchResponse result = session.send();

            System.out.println(result);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            connection.close();
        }
    }
}
