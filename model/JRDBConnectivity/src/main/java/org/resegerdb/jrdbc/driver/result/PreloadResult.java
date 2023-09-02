package org.resegerdb.jrdbc.driver.result;

public class PreloadResult implements Result {
    public boolean success;

    public String message;

    public PreloadResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public String getResult() {
        StringBuilder sb = new StringBuilder();
        if(success) {
            sb.append("预载入成功");
        } else {
            sb.append("预载入失败，错误原因：\n").append(message);
        }
        return sb.toString();
    }
}
