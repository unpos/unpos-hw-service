package ru.unpos.hw.service.requests;

public class SerialPorts {

    private String[] portNames;

    public SerialPorts() {}

    public SerialPorts(String[] portNames) {
        this.portNames = portNames;
    }

    public String[] getPortNames() {
        return portNames;
    }

    public void setPortNames(String[] portNames) {
        this.portNames = portNames;
    }
}
