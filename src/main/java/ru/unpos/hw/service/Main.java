package ru.unpos.hw.service;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import jssc.SerialNativeInterface;
import jssc.SerialPortList;

import java.util.Arrays;

@QuarkusMain
public class Main/* implements QuarkusApplication*/ {

    /*@Override
    public int run(String... args) throws Exception {
        System.out.println("Running main method");
        Arrays.stream(SerialPortList.getPortNames()).forEach(System.out::println);
        ConfigProvider.getConfig().getPropertyNames().forEach(System.out::println);
        System.out.println(ConfigProvider.getConfig().getConfigValue("binPath").getValue());
        Quarkus.waitForExit();
        return 0;
    }*/

    public static void main(String[] args) throws Exception {
        System.out.println("Check resources");
        try {
            System.out.println(Main.class.getResourceAsStream("/natives/linux_64/libjssc.so").available());
            System.out.println(Main.class.getResourceAsStream("/natives/windows_64/jssc.dll").available());
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(">>> List Ports");
        Arrays.stream(SerialPortList.getPortNames()).forEach(System.out::println);
        System.out.println(SerialNativeInterface.getNativeLibraryVersion());
        System.out.println("List Ports <<<");
        Quarkus.run(args);
        Quarkus.waitForExit();
        //new Main().run(args);
    }
}
