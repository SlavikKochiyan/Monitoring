package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.w3c.dom.ls.LSOutput;
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
    JPanel infoPanel;
    JPanel CPU;
    JPanel memPanel;
    JPanel gpuPanel;
    JPanel networksPanel;
    JLabel physicCores;
    JLabel logicCores;
    JLabel cpusLoad;
    JLabel cpusLoad2name;
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
    JLabel procModel;
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
    String processorModel;
    Font font = new Font("Times New Roman",Font.PLAIN,14);


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
        infoPanel = new JPanel();
        infoPanel.setBounds(0,0, frame.getWidth(), 300);
        infoPanel.setLayout(null);
        frame.add(infoPanel);

        CPU = new JPanel();
        CPU.setBounds(0,0,300,300);
        CPU.setBorder(BorderFactory.createTitledBorder("Info about CPU"));
        CPU.setLayout(null);
        infoPanel.add(CPU);

        memPanel = new JPanel();
        memPanel.setBounds(350,0,300,300);
        memPanel.setBorder(BorderFactory.createTitledBorder("Info about memory"));
        memPanel.setLayout(null);
        infoPanel.add(memPanel);

        gpuPanel = new JPanel();
        gpuPanel.setBounds(700,0,300,300);
        gpuPanel.setBorder(BorderFactory.createTitledBorder("Info about GPU"));
        gpuPanel.setLayout(null);
        infoPanel.add(gpuPanel);

        networksPanel = new JPanel();
        networksPanel.setBounds(1050,0,300,300);
        networksPanel.setBorder(BorderFactory.createTitledBorder("Info about network"));
        networksPanel.setLayout(null);
        infoPanel.add(networksPanel);

        physicCores = new JLabel();
        physicCores.setSize(100, 25);
        physicCores.setLocation(5, 50);
        physicCores.setFont(font);
        //physicCores.setBackground(Color.BLACK);
        CPU.add(physicCores);

        logicCores = new JLabel();
        logicCores.setSize(100, 50);
        logicCores.setLocation(5, 60);
        logicCores.setFont(font);
        CPU.add(logicCores);

        cpusLoad2name = new JLabel();
        cpusLoad2name.setSize(150, 25);
        cpusLoad2name.setLocation(5, 94);
        cpusLoad2name.setOpaque(true);
        cpusLoad2name.setFont(font);
        cpusLoad2name.setText("CPU load, %");
        CPU.add(cpusLoad2name);

        procModel = new JLabel();
        procModel.setSize(250, 25);
        procModel.setLocation(5, 25);
        procModel.setOpaque(true);
        procModel.setFont(font);
        CPU.add(procModel);

        cpusLoad = new JLabel();
        cpusLoad.setSize(100, 25);
        cpusLoad.setLocation(100, 94);
        cpusLoad.setOpaque(true);              //делает jLabel непрозрачной
        cpusLoad.setFont(font);
        CPU.add(cpusLoad);

        totalMemory = new JLabel();
        totalMemory.setSize(250, 50);
        totalMemory.setLocation(10, 0);
        totalMemory.setFont(font);
        memPanel.add(totalMemory);

        availablesMemory = new JLabel();
        availablesMemory.setSize(250, 25);
        availablesMemory.setLocation(10, 50);
        availablesMemory.setFont(font);
        memPanel.add(availablesMemory);

        GPUsName = new JLabel();
        GPUsName.setSize(500, 50);
        GPUsName.setLocation(10, 0);
        GPUsName.setFont(font);
        gpuPanel.add(GPUsName);

        Vendor = new JLabel();
        Vendor.setSize(500, 50);
        Vendor.setLocation(10, 50);
        Vendor.setFont(font);
        gpuPanel.add(Vendor);

        GPUsMemory = new JLabel();
        GPUsMemory.setSize(500, 50);
        GPUsMemory.setLocation(10, 100);
        GPUsMemory.setFont(font);
        gpuPanel.add(GPUsMemory);

        countSentBytes = new JLabel();
        countSentBytes.setSize(100, 50);
        countSentBytes.setLocation(10, 250);
        countSentBytes.setFont(font);
        networksPanel.add(countSentBytes);

        countSentBytes2 = new JLabel();
        countSentBytes2.setSize(250, 50);
        countSentBytes2.setLocation(10, 85);
        countSentBytes2.setFont(font);
        networksPanel.add(countSentBytes2);

        countRecvBytes = new JLabel();
        countRecvBytes.setSize(250, 50);
        countRecvBytes.setLocation(0, 300);
        countSentBytes2.setFont(font);
        frame.add(countRecvBytes);

        networkSpeed = new JLabel();
        networkSpeed.setSize(200, 50);
        networkSpeed.setLocation(0, 350);
        networkSpeed.setFont(font);
        networksPanel.add(networkSpeed);

        ipAddress = new JLabel();
        ipAddress.setSize(200, 50);
        ipAddress.setLocation(10, 0);
        ipAddress.setFont(font);
        networksPanel.add(ipAddress);

        macAddress = new JLabel();
        macAddress.setSize(280, 50);
        macAddress.setLocation(10, 20);
        macAddress.setFont(font);
        networksPanel.add(macAddress);

        macAddress2 = new JLabel();
        macAddress2.setSize(280, 50);
        macAddress2.setLocation(10, 40);
        macAddress2.setFont(font);
        networksPanel.add(macAddress2);

        countRecvBytes2 = new JLabel();
        countRecvBytes2.setSize(250, 50);
        countRecvBytes2.setLocation(10, 65);
        countRecvBytes2.setFont(font);
        networksPanel.add(countRecvBytes2);

        cpuLoadSeries = new TimeSeries("CPU Load");
        dataset = new TimeSeriesCollection(cpuLoadSeries);
        chart = ChartFactory.createTimeSeriesChart("CPU Load Over Time", "Time", "CPU Load (%)", dataset, false, true, false);
        panel = new ChartPanel(chart);
        panel.setBounds(5, 300, 750, 750);
        frame.add(panel);

        memoryLoadSeries = new TimeSeries("Memory Load");
        memoryDataset = new TimeSeriesCollection(memoryLoadSeries);
        memoryChart = ChartFactory.createTimeSeriesChart("Memory Load", "Time", "Memory Load (MB)", memoryDataset,false,true, false);
        memoryPanel = new ChartPanel(memoryChart);
        memoryPanel.setBounds(800,300, 750, 750);
        frame.add(memoryPanel);

        networkLoadSeries = new TimeSeries("Network Load");
        networkDataset = new TimeSeriesCollection(networkLoadSeries);
        networkChart = ChartFactory.createTimeSeriesChart("Network Load", "Time", "Load (KB/s)", networkDataset,false,true, false);
        networkPanel = new ChartPanel(networkChart);
        networkPanel.setBounds(1600,300, 750, 750);
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
        processorModel = processor.getProcessorIdentifier().getName();
        procModel.setText("Name: " + processorModel);
        timer = new Timer(1000, e -> monitorSystem(processor, memory));
        timer.start();
    }

    public void monitorSystem(CentralProcessor cpu, GlobalMemory gm) {
        double cpuLoad = cpu.getSystemCpuLoad(5000) * 100;
        if (cpuLoad < 1) cpusLoad.setBackground(new Color(21,171,0));
        else if (cpuLoad > 1 || cpuLoad < 10) cpusLoad.setBackground(new Color(255,255,0));
        else if (cpuLoad > 3 || cpuLoad < 40) cpusLoad.setBackground(new Color(254,138,24));
        else if (cpuLoad > 50) cpusLoad.setBackground(new Color(255,43,0));
//        else cpusLoad.setBackground(new Color(255,255,255));
        long availableMemory = gm.getAvailable();
        long totalsMemory = gm.getTotal();
        formatedDouble = new DecimalFormat("#0.00").format(cpuLoad);

        cpusLoad.setText(" " + formatedDouble);
        totalMemory.setText("Total memory " + totalsMemory / (1024 * 1024) + " MB"); //Всего установлено памяти
        availablesMemory.setText("Available memory " + availableMemory / (1024 * 1024) + " MB"); //Сколько используется

        cpuLoadSeries.addOrUpdate(new Second(), cpuLoad);
        memoryLoadSeries.addOrUpdate(new Second(), availableMemory/(1024*1024));

        for (NetworkIF net: sysInfo.getHardware().getNetworkIFs()){
            net.updateAttributes();
            sendBytes = (double) net.getBytesSent() / 1024;
            countSentBytes.setText("Network " + sendBytes);
            RecvBytes = (double) net.getBytesRecv() / 1024;
            countRecvBytes.setText("Network Recv " + RecvBytes);
            //networkLoadSeries.addOrUpdate(new Second(),sendBytes + RecvBytes);
            String [] ipAddres = net.getIPv4addr();
            for (int i = 0; i < ipAddres.length; i++){
                ipAddress.setText("Ip-address " + ipAddres[i]);
            }
            macAddress.setText("MAC-address ethernet: " + net.getMacaddr());
            if(net.getDisplayName().toLowerCase().contains("wi-fi") || net.getName().toLowerCase().contains("wlan")){
                macAddress2.setText("Mac-address wlan " + net.getMacaddr());
                sendBytes2 = (double) net.getBytesSent() / 1024;
                countSentBytes2.setText("Count send bytes " + sendBytes2);
                RecvBytes2 = (double) net.getBytesRecv() / 1024;
                countRecvBytes2.setText("Count recv bytes " + RecvBytes2); //количество полученных пакетов
                networkLoadSeries.addOrUpdate(new Second(),sendBytes2 + RecvBytes2);
                long speed = net.getSpeed();
                networkSpeed.setText("Network speed: " + speed);
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
