package com.example.artermis2.Data;

import com.example.artermis2.Database.Entity.Device;
import com.example.artermis2.Database.Entity.Scheduler;

public class DataHolder {
    private static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        DataHolder.name = name;
    }

    private static Scheduler scheduler;

    public static Scheduler getScheduler() {
        return scheduler;
    }

    public static void setScheduler(Scheduler scheduler) {
        DataHolder.scheduler = scheduler;
    }
    private static Device  device;

    public static void setDevice(Device device) {
        DataHolder.device = device;
    }

    public static Device getDevice() {
        return device;
    }
}
