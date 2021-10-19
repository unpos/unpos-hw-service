/*
 * Developed by Sokolov Alexey aka scream3r for 7pikes inc., 2014.
 *
 * scream3r.org@gmail.com
 * http://scream3r.org
 *
 * http://7pikes.com
 *
 * © Sokolov Alexey, 7pikes inc., 2014.
 */
package ru.unpos.hw.service.vfd;

import ru.unpos.hw.service.vfd.commands.BasicCommandPrototype;
import ru.unpos.hw.service.vfd.commands.CombinedCommandPrototype;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author scream3r
 */
public class Display {

    public static final int ENCODING_WITHOUT = 0;
    public static final int ENCODING_CP866 = 1;
    public static final int ENCODING_JAPANESE_POSIFLEX = 2;

    public static final int PROTOCOL_EPSON = 0;
    public static final int PROTOCOL_DSP800F = 1;
    public static final int PROTOCOL_CD5220 = 2;
    public static final int PROTOCOL_DATECS = 3;

    private int encoding = ENCODING_CP866;
    private int protocol = PROTOCOL_EPSON;
    
    private boolean connected = false;

    private SerialPort serialPort;

    private String portName = "";
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;

    public Display(String portName, int baudRate) {
        this(portName, baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

    public Display(String portName, int baudRate, int dataBits, int stopBits, int parity) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        protocol = PROTOCOL_EPSON;
    }

    public void connect() throws DisplayDriverException {
        if (isConnected()) {
            throw new DisplayDriverException(this, "connect()", DisplayDriverException.DRIVER_CONNECTION_ALREADY_ESTABLISHED);
        }
        serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(baudRate, dataBits, stopBits, parity);
            connected = true;
        } catch (SerialPortException ex) {
            throw convertSerialPortException("connect()", ex);
        } finally {
            if (!connected && serialPort.isOpened()) {
                try {
                    serialPort.closePort();
                } catch (SerialPortException ex) {
                    //Do nothing
                }
            }
        }
    }

    public void disconnect() throws DisplayDriverException {
        if (isConnected()) {
            connected = false;
            try {
                serialPort.closePort();
            } catch (SerialPortException ex) {
                throw convertSerialPortException("disconnect()", ex);
            }
        } else {
            throw new DisplayDriverException(this, "disconnect()", DisplayDriverException.DRIVER_CONNECTION_NOT_ESTABLISHED);
        }
    }

    public DisplayLinesStatus getLinesStatus() throws DisplayDriverException {
        if (isConnected()) {
            try {
                int[] lines = serialPort.getLinesStatus();
                return new DisplayLinesStatus(lines[0] == 1, lines[1] == 1, lines[2] == 1, lines[3] == 1);
            } catch (SerialPortException ex) {
                throw convertSerialPortException("getLinesStatus()", ex);
            }
        } else {
            throw new DisplayDriverException(this, "getLinesStatus()", DisplayDriverException.DRIVER_CONNECTION_NOT_ESTABLISHED);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public String getPortName() {
        return portName;
    }
    
    public int getBaudRate() {
        return baudRate;
    }
    
    public int getDataBits() {
        return dataBits;
    }
    
    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }

    public int getEncoding() {
        return encoding;
    }

    public void setEncoding(int encoding) throws DisplayDriverException {
        switch (encoding) {
            case ENCODING_WITHOUT:
            case ENCODING_CP866:
            case ENCODING_JAPANESE_POSIFLEX:
                this.encoding = encoding;
                break;
            default:
                throw new DisplayDriverException(this, "setEncoding()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
        }
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) throws DisplayDriverException {
        switch (protocol) {
            case PROTOCOL_EPSON:
            case PROTOCOL_DSP800F:
            case PROTOCOL_CD5220:
            case PROTOCOL_DATECS:
                this.protocol = protocol;
                break;
            default:
                throw new DisplayDriverException(this, "setProtocol()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
        }
    }

    public void excecuteCommand(CombinedCommandPrototype command) throws DisplayDriverException {
        command.executeCommand(this);
    }

    public synchronized void excecuteCommand(BasicCommandPrototype command) throws DisplayDriverException {
        if (!isConnected()) {
            throw new DisplayDriverException(this, "excecuteCommand()", DisplayDriverException.DRIVER_CONNECTION_NOT_ESTABLISHED);
        }
        if (command == null) {
            throw new DisplayDriverException(this, "excecuteCommand()", DisplayDriverException.DRIVER_NULL_NOT_PERMITTED);
        }
        writeCommandByteArray(command);
    }

    private void writeCommandByteArray(BasicCommandPrototype command) throws DisplayDriverException {
        byte[] commandByteArray = command.getCommandByteArray(this);
        try {
            serialPort.writeBytes(commandByteArray);
        } catch (SerialPortException ex) {
            throw convertSerialPortException("writeCommandByteArray()", ex);
        }
    }

    private DisplayDriverException convertSerialPortException(String methodName, SerialPortException ex) {
        DisplayDriverException convertedException;
        if (ex.getExceptionType().equals(SerialPortException.TYPE_PORT_BUSY)) {
            convertedException = new DisplayDriverException(this, methodName, DisplayDriverException.SERIAL_PORT_BUSY);
        } else if (ex.getExceptionType().equals(SerialPortException.TYPE_PORT_NOT_FOUND)) {
            convertedException = new DisplayDriverException(this, methodName, DisplayDriverException.SERIAL_PORT_NOT_FOUND);
        } else if (ex.getExceptionType().equals(SerialPortException.TYPE_PERMISSION_DENIED)) {
            convertedException = new DisplayDriverException(this, methodName, DisplayDriverException.SERIAL_PORT_PERMISSION_DENIED);
        } else if (ex.getExceptionType().equals(SerialPortException.TYPE_INCORRECT_SERIAL_PORT)) {
            convertedException = new DisplayDriverException(this, methodName, DisplayDriverException.SERIAL_PORT_INCORRECT);
        } else if (ex.getExceptionType().equals(SerialPortException.TYPE_PARAMETER_IS_NOT_CORRECT) || ex.getExceptionType().equals(SerialPortException.TYPE_NULL_NOT_PERMITTED)) {
            convertedException = new DisplayDriverException(this, methodName, DisplayDriverException.SERIAL_PORT_PARAMETER_INCORRECT);
        } else {
            convertedException = new DisplayDriverException(this, methodName, DisplayDriverException.SERIAL_PORT_INTERFACE_ERROR);
        }
        return convertedException;
    }

    public class DisplayLinesStatus {

        private boolean cts;
        private boolean dsr;
        private boolean ring;
        private boolean rlsd;

        public DisplayLinesStatus(boolean cts, boolean dsr, boolean ring, boolean rlsd) {
            this.cts = cts;
            this.dsr = dsr;
            this.ring = ring;
            this.rlsd = rlsd;
        }

        public boolean isCTS() {
            return cts;
        }

        public boolean isDSR() {
            return dsr;
        }

        public boolean isRING() {
            return ring;
        }

        public boolean isRLSD() {
            return rlsd;
        }

    }
}
