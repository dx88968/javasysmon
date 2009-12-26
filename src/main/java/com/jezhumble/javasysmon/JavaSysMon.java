package com.jezhumble.javasysmon;

import java.util.ArrayList;
import java.util.Iterator;

public class JavaSysMon implements Monitor {

    private static Monitor monitor = null;
    private static ArrayList supported = new ArrayList();

    public static void setMonitor(Monitor myMonitor) {
        monitor = myMonitor;
    }

    public static void addSupportedConfig(String config) {
        supported.add(config);
    }

    static {
        new MacOsXMonitor();
        new LinuxMonitor();
        new WindowsMonitor();
        new SolarisMonitor();
    }

    public static void main (String[] params) throws Exception {
        if (monitor == null) {
            System.err.println("Couldn't find an implementation for OS: " + System.getProperty("os.name"));
            System.err.println("Supported configurations:");
            for (Iterator iter = supported.iterator(); iter.hasNext(); ) {
                String config = (String) iter.next();
                System.err.println(config);
            }
        } else {
            CpuTimes initialTimes = monitor.cpuTimes();
            System.out.println("OS name: " + monitor.osName() +
                    "  Uptime: " + secsInDaysAndHours(monitor.uptimeInSeconds()) +
                    "  Current PID: " + monitor.currentPid());
            System.out.println("Number of CPUs: " + monitor.numCpus() +
                    "  CPU frequency: " + monitor.cpuFrequency() / (1000*1000) + " MHz");
            System.out.println("RAM " + monitor.physical() + "  SWAP " + monitor.swap());
            System.out.println("Sampling CPU usage...");
            Thread.sleep(500);
            System.out.println("CPU Usage: " + monitor.cpuTimes().getCpuUsage(initialTimes));
            System.out.println("\n" + ProcessInfo.header());
            ProcessInfo[] processes = monitor.processTable();
            for (int i = 0; i < processes.length; i++) {
                System.out.println(processes[i].toString());
            }
        }
    }

    private static String secsInDaysAndHours(long seconds) {
        long days = seconds / (60 * 60 * 24);
        long hours = (seconds / (60 * 60)) - (days * 24);
        return days + " days " + hours + " hours";
    }

    // Following are the actual stats you can get

    public String osName() {
        return monitor.osName();
    }

    public int numCpus() {
        return monitor.numCpus();
    }

    public long cpuFrequency() {
        return monitor.cpuFrequency();
    }

    public long uptimeInSeconds() {
        return monitor.uptimeInSeconds();
    }

    public int currentPid() {
        return monitor.currentPid();
    }

    public CpuTimes cpuTimes() {
        return monitor.cpuTimes();
    }

    public MemoryStats physical() {
        return monitor.physical();
    }

    public MemoryStats swap() {
        return monitor.swap();
    }

    public ProcessInfo[] processTable() {
        return monitor.processTable();
    }
}
