package org.risegerdd.client.shell.table;

import org.riseger.protoctl.compiler.result.ResultSet;

import java.util.List;

public class TablePrinter {
    private static final String[] BOUNDARY = new String[]
            {"╔", "╠", "╗", "╣", "╚", "╝", "╦"};
    private static final String[] SINGLE_LINE = new String[]
            {"─", "┼", "┬", "┴", "│", "╫"};
    private static final String[] DOUBLE_LINE = new String[]
            {"═", "╪", "╤", "╧", "║", "╬"};

    public String getTable(ResultSet resultSet) {
        List<TableElement> tableElements = getTableElements(resultSet);
        if (tableElements == null) return null;
        StringBuilder sb = new StringBuilder();
        toTable(sb, tableElements);
        return sb.toString();
    }

    private List<TableElement> getTableElements(ResultSet resultSet) {
        TableElement.indexAssigened = String.valueOf(resultSet.getCount()).length();
        for (:){

        }
        return null;
    }

    private void toTable(StringBuilder stringBuilder, List<TableElement> tableElements) {
        if (tableElements.isEmpty()) {
            return;
        }
        for (TableElement element : tableElements) {
            addTop(stringBuilder, element);
            addMiddle(stringBuilder, element);
        }
        addEnd(stringBuilder, tableElements.get(tableElements.size() - 1));
    }

    private void addTop(StringBuilder stringBuilder, TableElement tableElement) {
        //╔══════════════╤════════╤═══╗
        //╠═══╤══════════╪════════╪═══╣
        //╟───┼──────────┼────────┼───╢
        //╟───┼──────────┴────────┼───╢
        //{"═","╔","═","╤","╗"," ","║","╠","═","╪","╣","╚","╧","╝"}
        if (tableElement.top) {
            stringBuilder.append(BOUNDARY[0]);
        } else {
            stringBuilder.append(BOUNDARY[1]);
        }
        for (int i = 0; i < tableElement.indexAssigened + 2; i++) {
            stringBuilder.append(tableElement.bi ? DOUBLE_LINE[0] : SINGLE_LINE[0]);
        }
        if (tableElement.top) {
            stringBuilder.append(BOUNDARY[6]);
        } else {
            stringBuilder.append(tableElement.bi ? DOUBLE_LINE[5] : SINGLE_LINE[5]);
        }
        for (int i = 0; i < tableElement.size; i++) {
            int length = TableElement.standardAssignedValueList.get(i);
            for (int j = 0; j < length + 2; j++) {
                stringBuilder.append(tableElement.bi ? DOUBLE_LINE[0] : SINGLE_LINE[0]);
            }
            int in;
            if (i != tableElement.size - 1) {
                if (tableElement.mixlist.get(i)) {
                    if (tableElement.top) {
                        in = 0;
                    } else {
                        in = 3;
                    }
                    stringBuilder.append(tableElement.bi ? DOUBLE_LINE[in] : SINGLE_LINE[in]);
                } else {
                    if (tableElement.top) {
                        in = 2;
                    } else {
                        in = 1;
                    }
                    stringBuilder.append(tableElement.bi ? DOUBLE_LINE[in] : SINGLE_LINE[in]);
                }
            } else {
                if (tableElement.top) {
                    stringBuilder.append(BOUNDARY[2]);
                } else {
                    stringBuilder.append(BOUNDARY[3]);
                }
            }
        }
    }

    private void addMiddle(StringBuilder stringBuilder, TableElement tableElement) {
        /*
          ║ aaaaaaaaaaaa │ b      │ f ║
          ║ a            │ b      │ f ║
          ║ c │ dttt     │ 3.1515 │ f ║
          ║ a │ e        │ t      │ k ║
         */
        boolean flg = false;
        stringBuilder.append(DOUBLE_LINE[4]).append(" ");
        stringBuilder.append(tableElement.index);
        for (int i = 1; i < TableElement.indexAssigened - String.valueOf(tableElement.size).length(); i++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append(DOUBLE_LINE[4]);
        for (int i = 0; i < tableElement.size; i++) {
            if (flg) {
                flg = false;
                continue;
            }
            if (i == 0) {
                stringBuilder.append(DOUBLE_LINE[4]).append(" ");
            } else {
                stringBuilder.append(SINGLE_LINE[4]).append(" ");
            }
            String str = tableElement.contents.get(i);
            int len = str.length();
            if (len > TableElement.standardAssignedValueList.get(i)) {
                flg = true;
            }
            stringBuilder.append(str);
            for (int j = 0; j < TableElement.standardAssignedValueList.get(i) - str.length(); j++) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(" ");
        }
        stringBuilder.append(DOUBLE_LINE[4]);
    }

    private void addEnd(StringBuilder stringBuilder, TableElement tableElement) {
        /*
          ╚═══╧══════════╧════════╧═══╝
          ╚══════════════╧════════╧═══╝
         */
        // {"╔", "╠", "═", "╤", "╗", "╣", "╚", "╧", "╝", "║"}
        stringBuilder.append(BOUNDARY[4]);
        for (int i = 0; i < tableElement.size; i++) {
            int length = TableElement.standardAssignedValueList.get(i);
            for (int j = 0; j < length + 2; j++) {
                stringBuilder.append(DOUBLE_LINE[0]);
            }
            if (i != tableElement.size - 1) {
                if (tableElement.mixlist.get(i)) {
                    stringBuilder.append(DOUBLE_LINE[0]);
                } else {
                    stringBuilder.append(DOUBLE_LINE[3]);
                }
            } else {
                stringBuilder.append(BOUNDARY[5]);
            }
        }

    }

    private static class TableElement {
        private static int indexAssigened;
        private static List<Integer> standardAssignedValueList;

        boolean bi;
        int index;
        boolean top;
        private List<String> contents;
        private List<Boolean> mixlist;

        int size;

        private TableElement() {
        }
    }


}
