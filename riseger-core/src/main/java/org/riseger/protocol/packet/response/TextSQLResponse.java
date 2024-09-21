package org.riseger.protocol.packet.response;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protocol.compiler.result.ResultSet;
import org.riseger.protocol.packet.PacketType;
import org.riseger.protocol.packet.printer.SimpleTablePrinter;

@Setter
@Getter
public class TextSQLResponse extends BasicResponse {
    private ResultSet Result;

    public TextSQLResponse() {
        super(PacketType.TEXT_SQL_RESPONSE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        SimpleTablePrinter tablePrinter = new SimpleTablePrinter();

        if (this.isSuccess()) {
            sb.append("--------------------------\n");
            if (this.getResult() == null || this.getResult().getSize() == 0) {
                sb.append("Query successfully processed\n");

            } else {
                sb.append("Query OK, ").append(this.getResult().getSize()).append(" rows affected\n");

                sb.append(tablePrinter.getTables(this.getResult())).append("\n");
            }
        } else {
            sb.append("[ERROR] error:\n");
            sb.append(this.getException()).append("\n");
        }
        return sb.toString();
    }
}
