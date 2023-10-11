package org.risegerdd.client.shell;

import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.TextSQLMessageSession;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.otherProtocol.ProgressBar;
import org.riseger.protoctl.packet.response.TextSQLResponse;
import org.risegerdd.client.shell.introduce.CyberIntroduce;
import org.risegerdd.client.shell.progressbar.WavyProgressBar;
import org.risegerdd.client.shell.style.ColorList;
import org.risegerdd.client.shell.style.ColorStyle;
import org.risegerdd.client.shell.style.CyberColorStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class DatabaseShellClient {

    public static void main(String[] args) throws Exception {
        try {
            BufferedReader reader;
            PrintStream out;
            TextSQLMessageSession statement;
            String userInput;
            Connection connection;
            try {
                out = System.out;
                CyberIntroduce.introduce(out);
                ProgressBar progressBar = new WavyProgressBar(out, ColorList.CYBER_COLOR, 100);
                progressBar.loading(0);
                reader = new BufferedReader(new InputStreamReader(System.in));
                progressBar.loading(10);
                connection = Connection.connect("localhost", 10086, progressBar);
                progressBar.loading(10);
                statement = connection.sqlText();
                progressBar.done();
            } catch (Exception e) {
                System.out.println();
                System.err.print("[ERROR]   ");
                throw e;
            }
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
                    if (response.isSuccess()) {
                        out.println(response.getShellOutcome());
                    } else {
                        out.print(ColorStyle.PROMPT_FRONT + "197m" + "[ERROR] Unknown error:");
                        out.println(response.getException() + ColorStyle.END);
                    }
                } catch (Exception e) {
                    System.err.println("\n[ERROR] " + e.getMessage());
                }
            }

            connection.close();
        } catch (SQLException | IOException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
        }
    }
}

