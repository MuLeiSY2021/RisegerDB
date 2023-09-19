package org.risegerdd.client.shell;

import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.SearchSession;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.search.ResultSet;
import org.riseger.protoctl.search.ResultSetMetaData;
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
            Connection connection = Connection.connect("localhost", 10086);
            SearchSession statement = connection.search();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            PrintStream out = System.out;
            CyberIntroduce.introduce(out);
            WavyProgressBar.loading(out, ColorList.CYBER_COLOR, 100, 0);
            out.println(ColorList.CYBER_COLOR.toColorful("ResigerDB is now loaded and ready to use!"));
            while (true) {
                out.print(CyberColorStyle.VERY_SOFT_MAGENTA.toColor("ResigerDB" + CyberColorStyle.DARK_BLUE.toColor("❯ ")));
                userInput = reader.readLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    if (userInput.trim().toLowerCase().startsWith("select")) {
                        ResultSet resultSet = statement.executeQuery(userInput);
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        // 输出查询结果
                        while (resultSet.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                out.print(resultSet.getString(i) + "\t");
                            }
                            out.println();
                        }
                    } else {
                        int rowsAffected = statement.executeUpdate(userInput);
                        out.println("Query OK, " + rowsAffected + " row(s) affected");
                    }
                } catch (SQLException e) {
                    System.err.println("ERROR: " + e.getMessage());
                }
            }

            connection.close();
        } catch (SQLException | IOException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
        }
    }
}

