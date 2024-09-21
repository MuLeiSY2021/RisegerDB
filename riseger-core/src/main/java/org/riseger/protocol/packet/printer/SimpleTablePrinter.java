package org.riseger.protocol.packet.printer;

import org.riseger.protocol.compiler.result.ResultElement;
import org.riseger.protocol.compiler.result.ResultModelSet;
import org.riseger.protocol.compiler.result.ResultSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleTablePrinter {
    private static final String[][] TOP_LINE = new String[][]
            {
                    {"═", "╠", "╦", "═", "╤", "╤", "╣"},
                    {"═", "╠", "╬", "═", "╤", "╪", "╣"},
                    {"─", "╟", "╫", "─", "┬", "┼", "╣"}
            };

    private static final String[] TITLE_LINE = new String[]
            {"╔", "═", "╗"};
    private static final String[] BOTTOM_LINE = new String[]
            {"═", "╚", "╩", "═", "╧", "╝"};
    private static final String[] MIDDLE_LINE = new String[]
            {"║", "│", " "};

    public String getTables(ResultSet resultSet) {
        if (resultSet.getModelSetMap().entrySet().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ResultModelSet> entry : resultSet.getModelSetMap().entrySet()) {
            List<TableElement> tableElements = TableElement.getTableElements(entry.getValue());
            toTable(sb, entry.getKey(), tableElements);
        }
        return sb.toString();
    }

    private void toTable(StringBuilder stringBuilder, String title, List<TableElement> tableElements) {
       /*
        ╔═══════════════╗
        ║ aaaaaaaaaaaaa ║
        ╠═══╦═══════╤═══╣
        ║   ║ a     │ a ║
        ╠═══╬═══╤═══╪═══╣
        ║ 1 ║ a │ a │ a ║
        ╟───╫───┼───┼───╢
        ║ 2 ║ a │ a │ a ║
        ╚═══╩═══╧═══╧═══╝
         */
        if (tableElements.isEmpty()) {
            return;
        }
        addTitle(stringBuilder, title);
        for (TableElement element : tableElements) {
            addTop(stringBuilder, element);
            addMiddle(stringBuilder, element);
        }
        addBottom(stringBuilder, tableElements.get(tableElements.size() - 1));
    }

    private void addTitle(StringBuilder stringBuilder, String title) {
        /*
        {"╔", "═", "╗"}
        ╔═══════════════╗
        ║ aaaaaaaaaaaaa ║
        */
        int total = 0;
        for (int i : TableElement.standardAssignedValueList) {
            total += i + 3;
        }
        total++;
        stringBuilder.append(TITLE_LINE[0])
                .append(repeat(TITLE_LINE[1], total + 2))
                .append(TITLE_LINE[2]).append("\n")
                .append(MIDDLE_LINE[0])
                .append(" ").append(title).append(" ")
                .append(repeat(" ", total - title.length()))
                .append(MIDDLE_LINE[0]).append("\n");
    }

    private void addTop(StringBuilder stringBuilder, TableElement tableElement) {
        /*
        Num  Top  Mix  PMix Norm End
     Fst╠═══ ╦═══ ════ ╤═══ ╤═══ ╣
 {"═", "╠", "╦", "═", "╤", "╤", "╣"},

      Bi╠═══ ╬═══ ════ ╤═══ ╪═══ ╣
 {"═", "╠", "╬", "═", "╤", "╪", "╣"},

    Norm╟─── ╫─── ──── ┬─── ┼─── ╢
 {"─", "╟", "╫", "─", "┬", "┼", "╣"},
         */
        int type = tableElement.fst ? 0 : tableElement.bi ? 1 : 2;

        //添加数字排
        stringBuilder.append(TOP_LINE[type][1])
                .append(repeat(TOP_LINE[type][0], TableElement.indexAssigned + 2));
        //添加第二排
        for (int i = 0; i < TableElement.standardAssignedValueList.length; i++) {
            int row_c = i == 0 ? 2 : tableElement.getMix(i) ? 3 : tableElement.getPrevMix(i) ? 4 : 5;
            stringBuilder.append(TOP_LINE[type][row_c]);
            stringBuilder.append(repeat(TOP_LINE[type][0], TableElement.standardAssignedValueList[i] + 2));
        }

        //添加最后一行
        stringBuilder.append(TOP_LINE[type][6]).append("\n");
    }

    private String repeat(String s, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < i; j++) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    private void addMiddle(StringBuilder stringBuilder, TableElement tableElement) {
        /*
          ║ aaaaaaaaaaaa │ b      │ f ║
          ║ a            │ b      │ f ║
          ║ c │ dttt     │ 3.1515 │ f ║
          ║ a │ e        │ t      │ k ║
         */
        stringBuilder.append(MIDDLE_LINE[0]).append(" ");
        String indexStr = tableElement.index == -1 ? " " : String.valueOf(tableElement.index + 1);
        stringBuilder.append(indexStr)
                .append(repeat(" ", TableElement.indexAssigned - indexStr.length() + 1));
        int bend = 0;
        for (int i = 0; i < TableElement.standardAssignedValueList.length; i++) {
            if (tableElement.getMix(i)) {
                bend++;
                stringBuilder.append(repeat(" ", TableElement.standardAssignedValueList[i] + 2));
            } else {
                if (i == 0) {
                    stringBuilder.append(MIDDLE_LINE[0]).append(" ");
                } else {
                    stringBuilder.append(MIDDLE_LINE[1]).append(" ");
                }
                String str = tableElement.contents.get(i - bend);
                stringBuilder
                        .append(str)
                        .append(repeat(" ", TableElement.standardAssignedValueList[i] - str.length()));
            }

            stringBuilder.append(" ");
        }
        stringBuilder.append(MIDDLE_LINE[0]).append("\n");
    }

    private void addBottom(StringBuilder stringBuilder, TableElement tableElement) {
        /*
         Num   Bi   Mix  Norm  End
          ╚═══ ╩═══ ════ ╧═══ ╝
   {"═", "╚", "╩", "═", "╧", "╝"};
         */
        //添加数字排
        stringBuilder.append(BOTTOM_LINE[1])
                .append(repeat(BOTTOM_LINE[0], TableElement.indexAssigned + 2));
        //添加第二排
        for (int i = 0; i < TableElement.standardAssignedValueList.length; i++) {
            int row_c = i == 0 ? 2 : tableElement.getMix(i) ? 3 : 4;
            stringBuilder.append(BOTTOM_LINE[row_c])
                    .append(repeat(BOTTOM_LINE[0], TableElement.standardAssignedValueList[i] + 2));
        }

        //添加最后一行
        stringBuilder.append(BOTTOM_LINE[5]).append("\n");

    }

    private static class TableElement {
        private static int kRowSize;

        private static int indexAssigned;
        private static int[] standardAssignedValueList;

        private final List<String> contents;

        private final List<Boolean> prevMixlist;
        private final List<Boolean> mixlist;

        private final boolean bi;

        private final int index;

        private final boolean fst;

        private TableElement(ResultElement re) {
            this.index = -1;
            this.prevMixlist = new LinkedList<>();
            for (int i = 0; i < kRowSize + re.getColumns().size(); i++) {
                this.prevMixlist.add(true);
            }

            this.mixlist = new LinkedList<>();
            this.mixlist.add(false);
            for (int i = 0; i < kRowSize - 1; i++) {
                this.mixlist.add(true);
            }
            for (int i = 0; i < re.getColumns().size(); i++) {
                this.mixlist.add(false);
            }

            this.bi = false;

            this.fst = true;

            int i = 0;
            this.contents = new ArrayList<>(kRowSize == 0 ? 0 : 1 + re.getColumns().size());
            if (kRowSize != 0) {
                String s = "Key";
                this.contents.add(s);
                standardAssignedValueList[i] = Math.max(standardAssignedValueList[i], s.length());
                i++;
                i += kRowSize - 1;
            }

            for (Map.Entry<String, Object> entry : re.getColumns().entrySet()) {
                String s = entry.getKey();
                this.contents.add(s);
                standardAssignedValueList[i] = Math.max(standardAssignedValueList[i], s.length());
                i++;
            }
        }

        private TableElement(List<Boolean> prevMixList, int index, ResultElement re) {
            this.index = index;
            this.prevMixlist = prevMixList;

            this.mixlist = new LinkedList<>();
            for (int i = 0; i < kRowSize + re.getColumns().size(); i++) {
                this.mixlist.add(false);
            }

            this.bi = index == 0;

            this.fst = false;
            int i = 0;
            this.contents = new ArrayList<>(kRowSize + re.getColumns().size());
            for (Map.Entry<Integer, Double[]> entry : re.getKeyColumns().entrySet()) {
                String s = entry.getKey() + ":(" + entry.getValue()[0] + "," + entry.getValue()[1] + ")";
                this.contents.add(entry.getKey(), s);
                standardAssignedValueList[i] = Math.max(standardAssignedValueList[i], s.length());
                i++;
            }
            for (Map.Entry<String, Object> entry : re.getColumns().entrySet()) {
                String s = entry.getValue().toString();
                this.contents.add(s);
                standardAssignedValueList[i] = Math.max(standardAssignedValueList[i], s.length());
                i++;
            }
        }

        public static List<TableElement> getTableElements(ResultModelSet resultModelSet) {
            int kRowSize = 0;
            TableElement.indexAssigned = String.valueOf(resultModelSet.getResultElements().size()).length();
            for (ResultElement element : resultModelSet.getResultElements()) {
                kRowSize = Math.max(kRowSize, element.getKeyColumns().size());
            }
            TableElement.kRowSize = kRowSize;
            TableElement.standardAssignedValueList = new int[kRowSize + resultModelSet.getResultElements().get(0).getColumns().size()];

            List<TableElement> tableElements = new LinkedList<>();
            int i = 0;
            TableElement tmp = new TableElement(resultModelSet.getResultElements().get(0));
            tableElements.add(tmp);
            List<Boolean> previous = tmp.mixlist;
            for (ResultElement element : resultModelSet.getResultElements()) {
                tmp = new TableElement(previous, i++, element);
                previous = tmp.mixlist;
                tableElements.add(tmp);
            }
            return tableElements;
        }

        public boolean getMix(int i) {
            return mixlist.get(i);
        }

        public boolean getPrevMix(int i) {
            return prevMixlist.get(i);
        }
    }

}
