package org.test.search;

import org.reseger.jrdbc.driver.connector.Connector;
import org.reseger.jrdbc.driver.result.Result;
import org.reseger.jrdbc.driver.session.SearchSession;
import org.riseger.protoctl.search.function.condition.math.MATH_F;
import org.riseger.protoctl.search.function.entity.basic.BASIC_F;
import org.riseger.protoctl.search.function.entity.field.FIELD_F;
import org.riseger.protoctl.search.function.key.graphic.GRAPHIC_F;
import org.riseger.protoctl.search.function.logic.LOGIC_F;

public class SearchTest {
    public static void main(String[] args) throws InterruptedException {
        Connector connector = Connector.connect("localhost", 10086);
        try {

            SearchSession session = connector.search();
            session.use()
                    .useDatabase("test_db")
                    .useMap("china_mp")
                    .useScope(FIELD_F.RECT().invoke(
                            FIELD_F.COORD().invoke(99,88),
                            MATH_F.NUMBER().invoke(8)))
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
                                                                    36.5, 72.1
                                                            ),
                                                            MATH_F.NUMBER().invoke(4)
                                                    )
                                            ),
                                            GRAPHIC_F.OUT().invoke(
                                                    FIELD_F.RECT().invoke(
                                                            FIELD_F.COORD().invoke(
                                                                    777.5, 72658.1
                                                            ),
                                                            MATH_F.NUMBER().invoke(45.2)
                                                    )
                                            )
                                    ),
                                    MATH_F.SMALL().invoke(
                                            GRAPHIC_F.DISTANCE().invoke(
                                                    FIELD_F.COORD().invoke(99, 88),
                                                    BASIC_F.ATTRIBUTE().invoke("people.site")
                                            ),
                                            MATH_F.NUMBER().invoke(10)
                                    )
                            )
                    );
            System.out.println("发送了");
            Result result = session.send();

            System.out.println(result.getResult());

            connector.close();
        } catch (Exception e) {
            e.printStackTrace();
            connector.close();
        }
    }
}
