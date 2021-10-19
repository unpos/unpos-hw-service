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
public class Init extends CombinedCommandPrototype {

    @Override
    public void executeCommand(Display device) throws DisplayDriverException {
        new InitDisplay().executeCommand(device);
        new SetFontAndCodeTable().executeCommand(device);
    }

    private class InitDisplay extends BasicCommandPrototype {

        @Override
        protected void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException {
            try {
                byte[] commandBuffer = null;
                switch (device.getProtocol()) {
                    case Display.PROTOCOL_EPSON: {
                        commandBuffer = new byte[]{ 0x1B, 0x40 };
                        break;
                    }
                    case Display.PROTOCOL_DSP800F: {
                        commandBuffer = new byte[]{ 0x04, 0x01, 0x25, 0x17 };
                        break;
                    }
                    case Display.PROTOCOL_CD5220: {
                        commandBuffer = new byte[]{ 0x1B, 0x40 };
                        break;
                    }
                    case Display.PROTOCOL_DATECS: {
                        commandBuffer = new byte[]{ 0x1B, 0x40 };
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

    private class SetFontAndCodeTable extends BasicCommandPrototype {

        @Override
        protected void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException {
            try {
                byte[] commandBuffer = null;
                switch (device.getProtocol()) {
                    case Display.PROTOCOL_EPSON: {
                        commandBuffer = new byte[]{};
                        break;
                    }
                    case Display.PROTOCOL_DSP800F: {
                        commandBuffer = new byte[]{};
                        break;
                    }
                    case Display.PROTOCOL_CD5220: {
                        commandBuffer = new byte[]{ 0x1B, 0x63, 0x52 };
                        break;
                    }
                    case Display.PROTOCOL_DATECS: {
                        commandBuffer = new byte[]{ 0x1B, 0x74, 0x01 };
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
            if (device.getProtocol() == Display.PROTOCOL_CD5220 || device.getProtocol() == Display.PROTOCOL_DATECS) {
                super.execute(device);
            }
        }

    }

}
