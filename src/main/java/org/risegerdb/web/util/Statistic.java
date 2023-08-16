package org.risegerdb.web.util;

public class Statistic {
    private static Long onlineUsers = 0L;

    private static Long processingBytes = 0L;

    private static Long InfoTransferSum = 0L;

    public static Long getOnlineUsers() {
        return onlineUsers;
    }

    public static void addOnlineUser() {
        Statistic.onlineUsers++;
    }

    public static void addOnlineUser(int num) {
        Statistic.onlineUsers+= num;
    }

    public static void subOnlineUser() {
        Statistic.onlineUsers--;
    }

    public static void subOnlineUser(int num) {
        Statistic.onlineUsers-= num;
    }

    public static Long getProcessingBytes() {
        return processingBytes;
    }

    public static void addProcessingBytes(int num) {
        Statistic.processingBytes += num;
    }

    public static Long getInfoTransferSum() {
        return InfoTransferSum;
    }

    public static void addInfoTransfer() {
        InfoTransferSum ++;
    }

    public static void addInfoTransfer(int num) {
        InfoTransferSum += num;
    }
}
