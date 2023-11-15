package org.risegerdd.client.shell.table;

import org.riseger.protoctl.compiler.result.ResultSet;

import java.util.Iterator;
import java.util.List;

public class TablePrinter {
    private static final String[] BOUNDARY = new String[]
            {"╔", "╠", "═", "╤", "╗", "╣", "╚", "╧", "╝", "║"};
    private static final String[] SINGLE_LINE = new String[]
            {"─", "┼", "┬", "┴", "│"};
    private static final String[] DOUBLE_LINE = new String[]
            {"═", "╪", "╤", "╧", "║"};

    public String getTable(ResultSet resultSet) {
        /*
          ╔══════════════╤════════╤═══╗
          ║ a            │ b      │ f ║
          ╠═══╤══════════╪════════╪═══╣
          ║ c │ dttt     │ 3.1515 │ f ║
          ╟───┼──────────┼────────┼───╢
          ║ a │ baaaaaaa │ c      │ f ║
          ╟───┼──────────┼────────┼───╢
          ║ a │ e        │ t      │ k ║
          ╚═══╧══════════╧════════╧═══╝
         */

    }

    private void addLine(StringBuilder stringBuilder, TableElement tableElement) {

    }

    private void addTop(StringBuilder stringBuilder, TableElement tableElement) {
        //╔══════════════╤════════╤═══╗
        //╠═══╤══════════╪════════╪═══╣
        //╟───┼──────────┼────────┼───╢
        //╟───┼──────────┴────────┼───╢
        //{"═","╔","═","╤","╗"," ","║","╠","═","╪","╣","╚","╧","╝"}
        if (tableElement.topest) {
            stringBuilder.append(BOUNDARY[0]);
        } else {
            stringBuilder.append(BOUNDARY[1]);
        }
        for (int i = 0; i < tableElement.size; i++) {
            int length = tableElement.standardAssignedValueList.get(i);
            for (int j = 0; j < length + 2; j++) {
                stringBuilder.append(tableElement.bi ? DOUBLE_LINE[0] : SINGLE_LINE[0]);
            }
            int in = 1;
            if (tableElement.mixlist.get(i)) {
                if (tableElement.topest) {
                    in = 0;
                } else {
                    in = 3;
                }
                stringBuilder.append(tableElement.bi ? DOUBLE_LINE[0] : SINGLE_LINE[0]);
            } else {

                if (tableElement.topest) {
                    in = 2;
                }
                stringBuilder.append(tableElement.bi ? DOUBLE_LINE[1] : SINGLE_LINE[1]);
            }
        }
    }

    private void addMiddle(StringBuilder stringBuilder, TableElement tableElement) {
        Iterator<String> iterator = tableElement.contents.iterator();

    }

    private static class TableElement {
        boolean bi;

        boolean topest;

        List<String> contents;

        List<Boolean> mixlist;

        List<Integer> standardAssignedValueList;

        int size;
    }


}
