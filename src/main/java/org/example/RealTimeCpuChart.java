package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class RealTimeCpuChart extends JFrame {

    private XYSeries series;
    private CentralProcessor processor;

    public RealTimeCpuChart(String title) {
        super(title);

        // Создаем данные
        series = new XYSeries("CPU Load");

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Процессорная нагрузка в реальном времени", // заголовок
                "Время (секунды)", // ось X
                "Загрузка CPU (%)", // ось Y
                dataset, // данные
                PlotOrientation.VERTICAL,
                true, // легенда
                true,
                false
        );

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);

        // OSHI для мониторинга процессора
        SystemInfo systemInfo = new SystemInfo();
        processor = systemInfo.getHardware().getProcessor();

        // Таймер для обновления данных каждые 2 секунды
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int time = 0;

            @Override
            public void run() {
                // Получаем загрузку процессора
                double cpuLoad = processor.getSystemCpuLoad(5000) * 100;
                series.add(time++, cpuLoad);
            }
        }, 0, 2000);
    }

    public void SomeGraph() {
        SwingUtilities.invokeLater(() -> {
            RealTimeCpuChart example = new RealTimeCpuChart("Мониторинг CPU в реальном времени");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
