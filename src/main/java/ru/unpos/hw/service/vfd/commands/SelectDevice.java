/*
 * Developed by Sokolov Alexey aka scream3r for 7pikes inc., 2015.
 *
 * scream3r.org@gmail.com
 * http://scream3r.org
 *
 * http://7pikes.com
 *
 * © Sokolov Alexey, 7pikes inc., 2015.
 */
package ru.unpos.hw.service.vfd.commands;

import ru.unpos.hw.service.vfd.Display;
import ru.unpos.hw.service.vfd.DisplayDriverException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author scream3r
 */
public class SelectDevice extends BasicCommandPrototype {
    
    public static final int DEVICE_NEXT = 0;
    public static final int DEVICE_DISPLAY = 1;
    public static final int DEVICE_DISPLAY_AND_NEXT = 2;
    
    private int device;

    public SelectDevice(int device) {
        this.device = device;
    }

    private SelectDevice() {
        //Empty constructor
    }

    @Override
    protected void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException {
        switch (this.device) {
            case DEVICE_NEXT:
            case DEVICE_DISPLAY:
            case DEVICE_DISPLAY_AND_NEXT:
                break;
            default:
                throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
        }
        try {
            byte[] commandBuffer = new byte[]{};
            switch (device.getProtocol()) {
                case Display.PROTOCOL_EPSON: {
                    commandBuffer = new byte[]{ 0x1B, 0x3D, (byte)this.device };
                    break;
                }
                case Display.PROTOCOL_DSP800F: {
                    if (this.device != DEVICE_DISPLAY_AND_NEXT) {
                        commandBuffer = new byte[]{ 0x04, 0x01, 0x3D, (byte)this.device, 0x17 };
                    }
                    break;
                }
                case Display.PROTOCOL_CD5220: {
                    commandBuffer = new byte[]{ 0x1B, 0x3D, (byte)this.device };
                    break;
                }
                case Display.PROTOCOL_DATECS: {
                    break;
                }
            }
            if (commandBuffer != null) {
                outputStream.write(commandBuffer);
            } else {
                throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
            }
        } catch (IOException ex) {
            throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
        }
    }

    @Override
    public void executeCommand(Display device) throws DisplayDriverException {
        if (device.getProtocol() != Display.PROTOCOL_DATECS) {
            super.execute(device);
        }
    }

}
