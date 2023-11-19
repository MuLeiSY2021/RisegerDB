package org.risegerdd.client.shell;

import org.apache.log4j.Logger;
import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.TextSQLMessageSession;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.otherProtocol.ProgressBar;
import org.riseger.protoctl.packet.RequestType;
import org.riseger.protoctl.packet.response.TextSQLResponse;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.risegerdd.client.shell.introduce.CyberIntroduce;
import org.risegerdd.client.shell.progressbar.WavyProgressBar;
import org.risegerdd.client.shell.style.ColorList;
import org.risegerdd.client.shell.style.ColorStyle;
import org.risegerdd.client.shell.style.CyberColorStyle;
import org.risegerdd.client.shell.table.TablePrinter;

import java.io.PrintStream;
import java.util.Scanner;

public class DatabaseShellClient {
    private static final Logger LOG = Logger.getLogger(DatabaseShellClient.class);


    public static void main(String[] args) throws Exception {
        try {
            Scanner reader;
            PrintStream out;
            TextSQLMessageSession statement;
            String userInput;
            Connection connection;
            TablePrinter tablePrinter;
            try {
                out = System.out;
                CyberIntroduce.introduce(out);
                ProgressBar progressBar = new WavyProgressBar(out, ColorList.CYBER_COLOR, 100);
                progressBar.loading(0);
                tablePrinter = new TablePrinter();
                progressBar.loading(10);
                reader = new Scanner(System.in);
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
                StringBuilder sb = new StringBuilder();
                out.print(CyberColorStyle.VERY_SOFT_MAGENTA.toColor("ResigerDB" + CyberColorStyle.DARK_BLUE.toColor("❯ ")));
                do {
                    userInput = reader.nextLine();
                    sb.append(userInput).append('\n');
                } while (!userInput.endsWith(";"));
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                LOG.debug(sb.toString());
                statement.setSqlText(sb.toString());
                statement.setType(RequestType.SHELL);
                try {
                    //发送查询请求，并接收结果
                    TextSQLResponse response = statement.send();

                    //输出结果
                    if (response.isSuccess()) {
                        LOG.debug(JsonSerializer.serializeToString(response.getResult()));
                        out.println(tablePrinter.getTables(response.getResult()));
                    } else {
                        out.print(ColorStyle.PROMPT_FRONT + "197m" + "[ERROR] Unknown error:");
                        out.println(response.getException() + ColorStyle.END);
                    }
                } catch (Exception e) {
                    LOG.error("程序内部错误：", e);
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
        }
    }
}

