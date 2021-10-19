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
package ru.unpos.hw.service.vfd.commands;

import ru.unpos.hw.service.vfd.Display;
import ru.unpos.hw.service.vfd.DisplayDriverException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author scream3r
 */
public class CursorToPosition extends BasicCommandPrototype {

    private int position;

    public CursorToPosition(int position) {
        this.position = position;
    }

    private CursorToPosition() {
        //Empty constructor
    }

    @Override
    protected void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException {
        if (position < 0 || position > 39) {
            throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
        }
        try {
            byte[] commandBuffer = null;
            switch (device.getProtocol()) {
                case Display.PROTOCOL_EPSON: {
                    int column = (position > 19 ? position - 20 : position) + 1;
                    int row = (position > 19 ? 2 : 1);
                    commandBuffer = new byte[]{ 0x1F, 0x24, (byte)column, (byte)row };
                    break;
                }
                case Display.PROTOCOL_DSP800F: {
                    commandBuffer = new byte[]{ 0x04, 0x01, 0x50, (byte)(position + 49), 0x17 };
                    break;
                }
                case Display.PROTOCOL_CD5220: {
                    int column = (position > 19 ? position - 20 : position) + 1;
                    int row = (position > 19 ? 2 : 1);
                    commandBuffer = new byte[]{ 0x1B, 0x6C, (byte)column, (byte)row };
                    break;
                }
                case Display.PROTOCOL_DATECS: {
                    commandBuffer = new byte[]{ 0x1B, 0x48, (byte)position };
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
        super.execute(device);
    }

}
