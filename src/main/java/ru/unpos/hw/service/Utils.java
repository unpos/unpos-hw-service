package ru.unpos.hw.service;

import ru.unpos.hw.service.requests.SerialPorts;
import jssc.SerialNativeInterface;
import jssc.SerialPortList;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

@Path("/utils")
@Singleton
public class Utils {

    private final Pattern PORTNAMES_LINUX_REGEXP = Pattern.compile("(ttyD|ttyS|ttyUSB|ttyACM|ttyAMA|rfcomm|ttyO)[0-9]{1,3}");
    private final Pattern PORT_IDS_REGEXP = Pattern.compile("");

    @Path("getSerialPorts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SerialPorts getSerialPorts() {
        String[] portNames = {};
        try {
            if (SerialNativeInterface.getOsType() == SerialNativeInterface.OS_LINUX) {
                ArrayList<String> linuxPortNames = new ArrayList<>(Arrays.asList(SerialPortList.getPortNames(PORTNAMES_LINUX_REGEXP)));
                ArrayList<String> linuxPortIDs = new ArrayList<>(Arrays.asList(SerialPortList.getPortNames("/dev/serial/by-id", PORT_IDS_REGEXP)));

                for (String id : linuxPortIDs) {
                    linuxPortNames.remove(new File(id).getCanonicalPath());
                }

                linuxPortNames.addAll(linuxPortIDs);
                portNames = linuxPortNames.stream().toArray(String[]::new);
            } else {
                portNames = SerialPortList.getPortNames();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SerialPorts(portNames);
    }

}
