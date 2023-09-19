package org.risegerdd.client.shell.progressbar;

import org.risegerdd.client.shell.style.ColorList;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class WavyProgressBar {
    public static void loading(final PrintStream out, ColorList color, int totalSteps, int currentStep) {
        int animationSpeed = 100; // 波浪动画速度（毫秒）

        while (currentStep <= totalSteps) {
            // 清除控制台内容
            out.print("\033[H\033[2J");
            out.flush();

            // 显示波浪进度条
            out.print(color.toColor(generateWave(currentStep, 40, "Loading: " + currentStep * 100 / totalSteps + "% ", 0, totalSteps - 1)) + "\r");

            // 增加步数
            currentStep++;

            // 模拟加载延迟
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 生成Unicode波浪进度条
    public static String generateWave(double value, int length, String title, double vmin, double vmax) {
        String fill = "▒";
        StringBuilder res = new StringBuilder();
        String[] blocks = new String[]{"", "▏", "▎", "▍", "▌", "▋", "▊", "▉", "█"};
//        vmin = 0.0;
//        vmax = 1.0;
        String lsep = "▏", rsep = "▕";
        value = Math.min(Math.max(value, vmin), vmax);
        value = (value - vmin) / (vmax - vmin);

        double v = value * length,
                x = Math.floor(v),
                y = v - x;

        int i = (int) Math.round(y * 8);
        StringBuilder progressBar = new StringBuilder();

        for (int j = 0; j < x; j++) {
            progressBar.append("█");
        }
        progressBar.append(blocks[i]);
        int n = length - progressBar.length();
        for (int j = 0; j < n; j++) {
            progressBar.append(fill);
        }
        progressBar.append(rsep);
        res.append("\r").append(title).append(lsep).append(progressBar);

        return res.toString();
    }

}