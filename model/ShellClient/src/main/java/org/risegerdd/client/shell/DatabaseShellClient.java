package org.risegerdd.client.shell;

import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.TextSQLMessageSession;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.otherProtocol.ProgressBar;
import org.riseger.protoctl.packet.response.TextSQLResponse;
import org.risegerdd.client.shell.introduce.CyberIntroduce;
import org.risegerdd.client.shell.progressbar.WavyProgressBar;
import org.risegerdd.client.shell.style.ColorList;
import org.risegerdd.client.shell.style.CyberColorStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class DatabaseShellClient {

    public static void main(String[] args) throws Exception {
        try {


            PrintStream out = System.out;
            CyberIntroduce.introduce(out);
            ProgressBar progressBar = new WavyProgressBar(out, ColorList.CYBER_COLOR, 100);
            progressBar.loading(0);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            progressBar.loading(10);
            Connection connection = Connection.connect("localhost", 10086, progressBar);
            progressBar.loading(10);
            TextSQLMessageSession statement = connection.sqlText();
            progressBar.loading(10);
            String userInput;

            progressBar.done();
            out.println(ColorList.CYBER_COLOR.toColorful("ResigerDB is now loaded and ready to use!"));
            while (true) {
                out.print(CyberColorStyle.VERY_SOFT_MAGENTA.toColor("ResigerDB" + CyberColorStyle.DARK_BLUE.toColor("❯ ")));
                userInput = reader.readLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                statement.setSqlText(userInput);
                try {
                    //发送查询请求，并接收结果
                    TextSQLResponse response = statement.send();

                    //输出结果
                    out.print(response.getMessage());
//                    if (userInput.trim().toLowerCase().startsWith("select")) {
//                        ResultSet resultSet = statement.executeQuery(userInput);
//                        ResultSetMetaData metaData = resultSet.getMetaData();
//                        int columnCount = metaData.getColumnCount();
//
//                        // 输出查询结果
//                        while (resultSet.next()) {
//                            for (int i = 1; i <= columnCount; i++) {
//                                out.print(resultSet.getString(i) + "\t");
//                            }
//                            out.println();
//                        }
//                    } else {
//                        int rowsAffected = statement.executeUpdate(userInput);
//                        out.println("Query OK, " + rowsAffected + " row(s) affected");
//                    }
                } catch (Exception e) {
                    System.err.println("\nERROR: " + e.getMessage());
                }
            }

            connection.close();
        } catch (SQLException | IOException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
        }
    }
}

