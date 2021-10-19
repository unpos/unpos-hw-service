package ru.unpos.hw.service;

import ru.unpos.hw.service.requests.DisplaySettings;
import ru.unpos.hw.service.vfd.Display;
import ru.unpos.hw.service.vfd.DisplayData;
import ru.unpos.hw.service.vfd.DisplayDriverException;
import ru.unpos.hw.service.vfd.commands.ClearScreen;
import ru.unpos.hw.service.vfd.commands.PrintString;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/vfd")
@Singleton
public class VFDApi {

    private Display display;

    public VFDApi() {
        System.out.println("VFD created");
    }

    private void checkDevice() {

    }

    @Path("connect")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public DisplayData connect(DisplaySettings settings) {
        try {
            System.out.println("PORT: " + settings.getPortName());
            System.out.println("BAUD: " + settings.getBaudRate());
            System.out.println("PROT: " + settings.getProtocol());
            System.out.println("ENCO: " + settings.getEncoding());

            if (display != null && display.isConnected()) {
                display.disconnect();
            }
            display = new Display(settings.getPortName(), settings.getBaudRate());
            display.setProtocol(settings.getProtocol());
            display.setEncoding(settings.getEncoding());
            display.connect();
            return new DisplayData();
        } catch (DisplayDriverException e) {
            return new DisplayData(e);
        }
    }

    @Path("clearScreen")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public DisplayData clearScreen() {
        try {
            checkDevice();
            display.excecuteCommand(new ClearScreen());
            return new DisplayData();
        } catch (DisplayDriverException e) {
            return new DisplayData(e);
        }
    }

    @Path("printString")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DisplayData printString(PrintString printString) {
        try {
            checkDevice();
            display.excecuteCommand(printString);
            return new DisplayData();
        } catch (DisplayDriverException e) {
            return new DisplayData(e);
        }
    }

}