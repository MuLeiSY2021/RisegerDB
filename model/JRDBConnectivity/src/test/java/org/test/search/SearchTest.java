package org.test.search;

import org.resegerdb.jrdbc.command.search.function.basic.BASIC_F;
import org.resegerdb.jrdbc.command.search.function.graphic.GRAPHIC_F;
import org.resegerdb.jrdbc.command.search.function.graphic.field.FIELD_F;
import org.resegerdb.jrdbc.command.search.function.logic.LOGIC_F;
import org.resegerdb.jrdbc.command.search.function.math.MATH_F;
import org.resegerdb.jrdbc.command.search.function.type.COORD_FUNCTION;
import org.resegerdb.jrdbc.driver.connector.Connector;
import org.resegerdb.jrdbc.driver.result.Result;
import org.resegerdb.jrdbc.driver.session.SearchSession;

public class SearchTest {
    public static void main(String[] args) throws InterruptedException {
        Connector connector = Connector.connect("localhost", 10086);

        SearchSession session = connector.search();
        session.use()
                    .useDatabase("test_db")
                    .useMap("china_mp")
                    .useScope("province_scope.area_scope")
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
                                                                 36.5,72.1
                                                         ),
                                                         MATH_F.NUMBER().invoke(4)
                                                 )
                                         ),
                                         GRAPHIC_F.OUT().invoke(
                                                 FIELD_F.RECT().invoke(
                                                         FIELD_F.COORD().invoke(
                                                                 777.5,72658.1
                                                         ),
                                                         MATH_F.NUMBER().invoke(45.2)
                                                 )
                                         )
                                 ),
                                 MATH_F.SMALL().invoke(
                                         GRAPHIC_F.DISTANCE().invoke(
                                                 FIELD_F.COORD().invoke(99,88),
                                                 (COORD_FUNCTION) BASIC_F.ATTRIBUTE().invoke("people.site")
                                         ),
                                         MATH_F.NUMBER().invoke(10)
                                 )
                         )
                 ).warp();
        Result result = session.send();

        System.out.println(result.getResult());

        connector.close();
    }
}
