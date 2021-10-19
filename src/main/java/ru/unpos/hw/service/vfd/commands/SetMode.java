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
public class SetMode extends BasicCommandPrototype {

    public static final int MODE_OVERWRITE = 0;
    public static final int MODE_VERTICAL_SCROLL = 1;
    public static final int MODE_HORIZONTAL_SCROLL = 2;

    private final byte[] modesEPSON = {0x01, 0x02, 0x03};
    private final byte[] modesCD5220 = {0x11, 0x12, 0x13};
    private final byte[] modesDATECS = {0x01, 0x02, 0x03};

    private int mode;

    public SetMode(int mode) {
        this.mode = mode;
    }

    private SetMode() {
        //Empty constructor
    }

    @Override
    protected void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException {
        switch (mode) {
            case MODE_OVERWRITE:
            case MODE_VERTICAL_SCROLL:
            case MODE_HORIZONTAL_SCROLL:
                break;
            default:
                throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
        }
        try {
            byte[] commandBuffer = null;
            switch (device.getProtocol()) {
                case Display.PROTOCOL_EPSON: {
                    commandBuffer = new byte[]{ 0x1F, modesEPSON[mode] };
                    break;
                }
                case Display.PROTOCOL_DSP800F: {
                    commandBuffer = new byte[]{};
                    break;
                }
                case Display.PROTOCOL_CD5220: {
                    commandBuffer = new byte[]{ 0x1B, modesCD5220[mode] };
                    break;
                }
                case Display.PROTOCOL_DATECS: {
                    commandBuffer = new byte[]{ 0x1F, modesDATECS[mode] };
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
        if (device.getProtocol() != Display.PROTOCOL_DSP800F) {
            super.execute(device);
        }
    }

}
