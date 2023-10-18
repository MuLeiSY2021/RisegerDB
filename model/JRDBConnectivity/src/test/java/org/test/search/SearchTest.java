package org.test.search;

import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.SearchSession;
import org.riseger.protoctl.packet.response.SearchResponse;
import org.riseger.protoctl.search.function.Entity_F;
import org.riseger.protoctl.search.function.condition.math.Math_F;
import org.riseger.protoctl.search.function.entity.Attribute_F;
import org.riseger.protoctl.search.function.entity.FIELD_F;
import org.riseger.protoctl.search.function.key.graphic.GRAPHIC_F;
import org.riseger.protoctl.search.function.logic.LOGIC_F;

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
                                    Entity_F.use(1), Entity_F.use(2)),
                            Entity_F.use(20000)))
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
                                                                    Entity_F.use(4000),
                                                                    Entity_F.use(1000)
                                                            ),
                                                            Entity_F.use(5000)
                                                    )
                                            ),
                                            GRAPHIC_F.OUT().invoke(
                                                    FIELD_F.RECT().invoke(
                                                            FIELD_F.COORD().invoke(
                                                                    Entity_F.use(777.5),
                                                                    Entity_F.use(72658.1)
                                                            ),
                                                            Entity_F.use(45.2)
                                                    )
                                            )
                                    ),
                                    Math_F.BIG().invoke(
                                            Attribute_F.use(Entity_F.use("building_model.floorArea")),
                                            Entity_F.use(1000)
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
