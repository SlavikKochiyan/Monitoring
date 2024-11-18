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

public class Menu extends JFrame {
    JFrame frame;
    JLabel physicCores;
    JLabel logicCores;
    JLabel cpusLoad;
    JLabel totalMemory;
    JLabel availablesMemory;
    JLabel countSentBytes;
    JLabel countSentBytes2;
    JLabel countRecvBytes;
    JLabel countRecvBytes2;
    JLabel ipAddress;
    JLabel macAddress;
    JLabel macAddress2;
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
    TimeSeries networkLoadSeries;
    TimeSeriesCollection dataset;
    TimeSeriesCollection memoryDataset;
    TimeSeriesCollection networkDataset;
    JFreeChart chart;
    JFreeChart memoryChart;
    JFreeChart networkChart;
    ChartPanel panel;
    ChartPanel memoryPanel;
    ChartPanel networkPanel;
    double sendBytes, sendBytes2, RecvBytes, RecvBytes2;


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

        countSentBytes2 = new JLabel();
        countSentBytes2.setSize(100, 50);
        countSentBytes2.setLocation(0, 200);
        frame.add(countSentBytes2);

        countRecvBytes = new JLabel();
        countRecvBytes.setSize(250, 50);
        countRecvBytes.setLocation(0, 300);
        frame.add(countRecvBytes);

        networkSpeed = new JLabel();
        networkSpeed.setSize(200, 50);
        networkSpeed.setLocation(0, 350);
        frame.add(networkSpeed);

        ipAddress = new JLabel();
        ipAddress.setSize(200, 50);
        ipAddress.setLocation(0, 400);
        frame.add(ipAddress);

        macAddress = new JLabel();
        macAddress.setSize(200, 50);
        macAddress.setLocation(0, 450);
        frame.add(macAddress);

        macAddress2 = new JLabel();
        macAddress2.setSize(200, 50);
        macAddress2.setLocation(0, 500);
        frame.add(macAddress2);

        countRecvBytes2 = new JLabel();
        countRecvBytes2.setSize(250, 50);
        countRecvBytes2.setLocation(0, 550);
        frame.add(countRecvBytes2);

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

        networkLoadSeries = new TimeSeries("Network Load");
        networkDataset = new TimeSeriesCollection(networkLoadSeries);
        networkChart = ChartFactory.createTimeSeriesChart("Network Load", "Time", "Load (KB/s)", networkDataset,false,true, false);
        networkPanel = new ChartPanel(networkChart);
        networkPanel.setBounds(800,50,500,300);
        frame.add(networkPanel);
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
            sendBytes = (double) net.getBytesSent() / 1024;
            countSentBytes.setText("Network " + sendBytes);
            RecvBytes = (double) net.getBytesRecv() / 1024;
            countRecvBytes.setText("Network Recv " + RecvBytes);
            long speed = net.getSpeed();
            networkSpeed.setText("Speed: " + speed);
            //networkLoadSeries.addOrUpdate(new Second(),sendBytes + RecvBytes);
            String [] ipAddres = net.getIPv4addr();
            for (int i = 0; i < ipAddres.length; i++){
                ipAddress.setText(ipAddres[i]);
            }
            macAddress.setText(" " + net.getMacaddr());
            if(net.getDisplayName().toLowerCase().contains("wi-fi") || net.getName().toLowerCase().contains("wlan")){
                macAddress2.setText(net.getMacaddr());
                sendBytes2 = (double) net.getBytesSent() / 1024;
                countSentBytes2.setText("Network " + sendBytes2);
                RecvBytes2 = (double) net.getBytesRecv() / 1024;
                countRecvBytes2.setText(" " + RecvBytes2);
                networkLoadSeries.addOrUpdate(new Second(),sendBytes2 + RecvBytes2);
            }
            if(sendBytes2 > sendBytes)
                countSentBytes.setVisible(false);
            else if (sendBytes > sendBytes2)
                countSentBytes2.setVisible(false);
            if (RecvBytes2 > RecvBytes)
                countRecvBytes.setVisible(false);
            else if (RecvBytes > RecvBytes2)
                countRecvBytes2.setVisible(false);
        }
    }
}
