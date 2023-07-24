package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.resegerdb.jrdbc.command.Commands;
import org.resegerdb.jrdbc.command.create.*;
import org.resegerdb.jrdbc.driver.Connection;
import org.resegerdb.jrdbc.driver.Result;
import org.resegerdb.jrdbc.driver.Session;
import org.resegerdb.jrdbc.struct.model.ParentModel;
import org.resegerdb.jrdbc.struct.model.Type;

import java.io.IOException;

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

        Session createSession = connection.createSession();
        Commands commands = createSession.commands();
        commands.create().database().name("db_test");
        commands.create().map().database("db_test").name("map_test");
//        commands.create().scope().database("scp_test").name("scp_test");
//
//        CreateDatabase cd = new CreateDatabase().name("db_test");
//        CreateMap cm = new CreateMap().database("map_test");
//        CreateScope cs = new CreateScope().database("scp_test");
//        CreateModel cmd = new CreateModel().database("db_test")
//                .name("md_my_model")
//                .parent(ParentModel.POINT)
//                .parameter("name", Type.STRING)
//                .parameter("id", Type.INT);
//
//        createSession.addCommand(cd,cm,cs,cmd);
        Result result = createSession.send();
        System.out.println(result);

        Session session = connection.createSession();
        for (int i = 0; i < 10; i++) {
//            InsertData insertData = new InsertData();
//            insertData.database("db_test")
//                    .map("map_test")
//                    .scope("scp_test")
//                    .model("md_my_model")
//                    .parameter("name", "name_" + i)
//                    .parameter("id", "id_" + i);
//            session.addCommand(insertData);
        }

        result = session.send();
        System.out.println(result);

        connection.close();


    }
}
