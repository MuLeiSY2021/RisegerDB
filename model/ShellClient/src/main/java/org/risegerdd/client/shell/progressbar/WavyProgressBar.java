package org.risegerdd.client.shell.progressbar;

import org.riseger.protoctl.otherProtocol.ProgressBar;
import org.risegerdd.client.shell.style.ColorList;

import java.io.PrintStream;

public class WavyProgressBar implements ProgressBar {
    private final PrintStream out;

    private final ColorList color;

    private final int totalSteps;

    private int currentStep;

    public WavyProgressBar(PrintStream out, ColorList color, int totalSteps) {
        this.out = out;
        this.color = color;
        this.totalSteps = totalSteps;
        this.currentStep = 0;
    }

    public void loading(int addedSteps) {
        this.currentStep += addedSteps;


        // 显示波浪进度条
        out.print(color.toColor(generateWave(currentStep, 40, "Loading: " + currentStep * 100 / totalSteps + "% ", 0, totalSteps - 1)) + "\r");
    }

    @Override
    public void done() {
        this.currentStep = this.totalSteps;
        loading(0);
    }

    // 生成Unicode波浪进度条
    public String generateWave(double value, int length, String title, double vmin, double vmax) {
        String fill = "▒";
        StringBuilder res = new StringBuilder();
        String[] blocks = new String[]{"", "▏", "▎", "▍", "▌", "▋", "▊", "▉", "█"};
        String lsep = "┃", rsep = "┃";
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