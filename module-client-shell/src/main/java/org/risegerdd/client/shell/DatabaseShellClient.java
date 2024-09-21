package org.risegerdd.client.shell;

import org.apache.log4j.PropertyConfigurator;
import org.reseger.jrdbc.driver.connector.Connection;
import org.reseger.jrdbc.driver.session.TextSQLMessageSession;
import org.riseger.protocol.exception.SQLException;
import org.riseger.protocol.otherProtocol.ProgressBar;
import org.riseger.protocol.packet.RequestType;
import org.riseger.protocol.packet.response.TextSQLResponse;
import org.risegerdd.client.shell.introduce.CyberIntroduce;
import org.risegerdd.client.shell.progressbar.WavyProgressBar;
import org.risegerdd.client.shell.style.Color;
import org.risegerdd.client.shell.style.ColorList;
import org.risegerdd.client.shell.style.CyberColorStyle;
import org.risegerdd.client.shell.table.ColorfulTablePrinter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Scanner;

import static org.risegerdd.client.shell.style.CyberColorStyle.DARK_BLUE;
import static org.risegerdd.client.shell.style.CyberColorStyle.VERY_SOFT_MAGENTA;

public class DatabaseShellClient {

    public static void main(String[] args) throws Exception {
        try {
            Scanner reader;
            PrintStream out;
            TextSQLMessageSession statement;
            Connection connection;
            ColorfulTablePrinter tablePrinter;
            try {
                out = System.out;
                out.println("\n");
                CyberIntroduce.introduce(out);
                ProgressBar progressBar = new WavyProgressBar(out, ColorList.CYBER_COLOR, 100);
                progressBar.loading(0);
                String root = args[0];
                progressBar.loading(10);
                initLog(root);
                progressBar.loading(10);
                tablePrinter = new ColorfulTablePrinter();
                progressBar.loading(10);
                reader = new Scanner(System.in);
                progressBar.loading(10);
                connection = Connection.connect("localhost", 10086, progressBar);
                progressBar.loading(10);
                statement = connection.sqlText();
                progressBar.done();
            } catch (Exception e) {
                System.err.println("[ERROR]   ");
                throw e;
            }
            System.out.print("\033[2K"); // Erase line content
            out.println(ColorList.CYBER_COLOR.toColorful("ResigerDB is now loaded and ready to use!"));
            while (true) {
                if (!clientSending(reader, out, statement, tablePrinter)) {
                    break;
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
        }
    }

    private static boolean clientSending(Scanner reader,
                                         PrintStream out,
                                         TextSQLMessageSession statement,
                                         ColorfulTablePrinter tablePrinter) {

        StringBuilder sb = new StringBuilder();
        out.print(CyberColorStyle.color(VERY_SOFT_MAGENTA).toColor("ResigerDB" + CyberColorStyle.color(DARK_BLUE).toColor("❯ ")));
        String userInput;
        do {
            userInput = reader.nextLine();
            sb.append(userInput).append('\n');
        } while (!userInput.endsWith(";"));
        if (userInput.startsWith("exit")) {
            return false;
        }
        statement.setSqlText(sb.toString());
        statement.setType(RequestType.SHELL);
        try {
            //发送查询请求，并接收结果
            TextSQLResponse response = statement.send();

            //输出结果
            if (response.isSuccess()) {
                out.println("--------------------------");
                if (response.getResult() == null || response.getResult().getSize() == 0) {
                    out.println(CyberColorStyle.color(VERY_SOFT_MAGENTA).toColor("Query successfully processed \uD83D\uDE80"));

                } else {
                    out.println(CyberColorStyle.color(VERY_SOFT_MAGENTA).toColor("Query OK, " + response.getResult().getSize()) + " rows affected \uD83D\uDE80");

                    out.println(tablePrinter.getTables(response.getResult()));
                }
            } else {
                out.print(Color.PROMPT_FRONT + "197m" + "[ERROR] error:");
                out.println(response.getException() + Color.END);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void initLog(String rootPath) {
        FileInputStream fileInputStream = null;
        try {
            Properties properties = new Properties();
            fileInputStream = new FileInputStream(rootPath + "/resources/log4j.properties");
            properties.load(fileInputStream);
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

