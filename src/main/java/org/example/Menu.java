package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.NetworkIF;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * TODO
 * Understanding how to get information from sensors of computers;
 * Get information about GPU
 **/

public class Menu extends JFrame {
    JFrame frame;
    JLabel physicCores;
    JLabel logicCores;
    JLabel cpusLoad;
    JLabel totalMemory;
    JLabel availablesMemory;
    JLabel countSentBytes;
    JLabel countRecvBytes;
    JLabel networkSpeed;
    JLabel GPUsName;
    JLabel Vendor;
    JLabel GPUsMemory;
    String formatedDouble;
    CentralProcessor processor;
    List<GraphicsCard> cardList;
    GlobalMemory memory;
    SystemInfo sysInfo;
    Timer timer;
    TimeSeries cpuLoadSeries;
    TimeSeries memoryLoadSeries;
    TimeSeriesCollection dataset;
    TimeSeriesCollection memoryDataset;
    JFreeChart chart;
    JFreeChart memoryChart;
    ChartPanel panel;
    ChartPanel memoryPanel;


    public void createFrame() {
        frame = new JFrame("Monitoring");
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(null);
        addComponents();
    }

    public void addComponents() {
        physicCores = new JLabel();
        physicCores.setSize(100, 50);
        physicCores.setLocation(25, 25);
        physicCores.setBackground(Color.BLACK);
        frame.add(physicCores);

        logicCores = new JLabel();
        logicCores.setSize(100, 50);
        logicCores.setLocation(0, 100);
        frame.add(logicCores);

        countSentBytes = new JLabel();
        countSentBytes.setSize(100, 50);
        countSentBytes.setLocation(0, 250);
        frame.add(countSentBytes);

        countRecvBytes = new JLabel();
        countRecvBytes.setSize(100, 50);
        countRecvBytes.setLocation(0, 300);
        frame.add(countRecvBytes);

        networkSpeed = new JLabel();
        networkSpeed.setSize(100, 50);
        networkSpeed.setLocation(0, 350);
        frame.add(networkSpeed);

        GPUsName = new JLabel();
        GPUsName.setSize(500, 50);
        GPUsName.setLocation(200, 700);
        frame.add(GPUsName);

        Vendor = new JLabel();
        Vendor.setSize(500, 50);
        Vendor.setLocation(200, 750);
        frame.add(Vendor);

        GPUsMemory = new JLabel();
        GPUsMemory.setSize(500, 50);
        GPUsMemory.setLocation(200, 800);
        frame.add(GPUsMemory);

        cpusLoad = new JLabel();
        cpusLoad.setSize(100, 50);
        cpusLoad.setLocation(100, 50);
        cpusLoad.setOpaque(true);              //делает jLabel непрозрачной
        cpusLoad.setBackground(new Color(43, 55, 67));
        cpusLoad.setForeground(Color.white);
        frame.add(cpusLoad);

        totalMemory = new JLabel();
        totalMemory.setSize(100, 50);
        totalMemory.setLocation(100, 200);
        frame.add(totalMemory);

        availablesMemory = new JLabel();
        availablesMemory.setSize(100, 50);
        totalMemory.setLocation(100, 250);
        frame.add(availablesMemory);

        cpuLoadSeries = new TimeSeries("CPU Load");
        dataset = new TimeSeriesCollection(cpuLoadSeries);
        chart = ChartFactory.createTimeSeriesChart("CPU Load Over Time", "Time", "CPU Load (%)", dataset, false, true, false);
        panel = new ChartPanel(chart);
        panel.setBounds(300, 50, 500, 300);
        frame.add(panel);

        memoryLoadSeries = new TimeSeries("Memory Load");
        memoryDataset = new TimeSeriesCollection(memoryLoadSeries);
        memoryChart = ChartFactory.createTimeSeriesChart("Memory Load", "Time", "Memory Load (MB)", memoryDataset,false,true, false);
        memoryPanel = new ChartPanel(memoryChart);
        memoryPanel.setBounds(300,350,500,300);
        frame.add(memoryPanel);
        sysInfo();
    }

    public void sysInfo() {
        sysInfo = new SystemInfo();
        processor = sysInfo.getHardware().getProcessor();
        cardList = sysInfo.getHardware().getGraphicsCards();
        memory = sysInfo.getHardware().getMemory();

        //Get info about graphic card
        for (GraphicsCard gpu : cardList) {
            GPUsName.setText("Name " + gpu.getName());
            Vendor.setText("Vendor: " + gpu.getVendor());
            GPUsMemory.setText("GPU`s Memory " + gpu.getVRam() / (1024*1024));
        }
        int physicalCores = processor.getPhysicalProcessorCount();
        physicCores.setText("Physical cores: " + physicalCores);
        int logicalCores = processor.getLogicalProcessorCount();
        logicCores.setText("Logical cores: " + logicalCores);
        timer = new Timer(1000, e -> monitorSystem(processor, memory));
        timer.start();
    }

    public void monitorSystem(CentralProcessor cpu, GlobalMemory gm) {
        double cpuLoad = cpu.getSystemCpuLoad(5000) * 100;
        long availableMemory = gm.getAvailable();
        long totalsMemory = gm.getTotal();
        formatedDouble = new DecimalFormat("#0.00").format(cpuLoad);

        cpusLoad.setText(" " + formatedDouble);
        totalMemory.setText("" + totalsMemory / (1024 * 1024));
        availablesMemory.setText(" " + availableMemory / (1024 * 1024));

        cpuLoadSeries.addOrUpdate(new Second(), cpuLoad);
        memoryLoadSeries.addOrUpdate(new Second(), availableMemory/(1024*1024));

        for (NetworkIF net: sysInfo.getHardware().getNetworkIFs()){
            net.updateAttributes();
            double sendBytes = net.getBytesSent();
            countSentBytes.setText("Network " + sendBytes);
            double RecvBytes = net.getBytesRecv();
            countRecvBytes.setText("Network Recv " + RecvBytes);
            long speed = net.getSpeed();
            networkSpeed.setText("Speed: " + speed);

        }
    }
}
