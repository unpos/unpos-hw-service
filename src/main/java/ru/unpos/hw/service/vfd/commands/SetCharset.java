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
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author scream3r
 */
public class SetCharset extends BasicCommandPrototype {
    
    public static final int CHARSET_ASCII = 0;
    public static final int CHARSET_CANADIAN_FRENCH = 1;
    public static final int CHARSET_KATAKANA = 2;
    public static final int CHARSET_MULTILINGUAL = 3;
    public static final int CHARSET_NORDIC = 4;
    public static final int CHARSET_PORTUGUESE = 5;
    public static final int CHARSET_RUSSIA = 6;
    public static final int CHARSET_SLAVONIC = 7;
    
    private static final ConcurrentHashMap<Integer, Integer> charsetsEPSON = new ConcurrentHashMap<Integer, Integer>();
    private static final ConcurrentHashMap<Integer, Integer> charsetsCD5220 = new ConcurrentHashMap<Integer, Integer>();
    private static final ConcurrentHashMap<Integer, Integer> charsetsDATECS = new ConcurrentHashMap<Integer, Integer>();
    
    static {
        charsetsEPSON.put(CHARSET_ASCII, 0x00);
        charsetsEPSON.put(CHARSET_CANADIAN_FRENCH, 0x04);
        charsetsEPSON.put(CHARSET_KATAKANA, 0x01);
        charsetsEPSON.put(CHARSET_MULTILINGUAL, 0x02);
        charsetsEPSON.put(CHARSET_NORDIC, 0x05);
        charsetsEPSON.put(CHARSET_PORTUGUESE, 0x03);
        charsetsEPSON.put(CHARSET_RUSSIA, 0x06);
        charsetsEPSON.put(CHARSET_SLAVONIC, 0x07);
        
        charsetsCD5220.put(CHARSET_ASCII, 0x41);
        charsetsCD5220.put(CHARSET_KATAKANA, 0x4A);
        charsetsCD5220.put(CHARSET_RUSSIA, 0x52);
        charsetsCD5220.put(CHARSET_SLAVONIC, 0x4C);
        
        charsetsDATECS.put(CHARSET_ASCII, 0x00);
        charsetsDATECS.put(CHARSET_MULTILINGUAL, 0x01);
    }
    
    private int charset;

    public SetCharset(int charset) {
        this.charset = charset;
    }

    private SetCharset() {
        //Empty constructor
    }

    @Override
    protected void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException {
        switch (charset) {
            case CHARSET_ASCII:
            case CHARSET_CANADIAN_FRENCH:
            case CHARSET_KATAKANA:
            case CHARSET_MULTILINGUAL:
            case CHARSET_NORDIC:
            case CHARSET_PORTUGUESE:
            case CHARSET_RUSSIA:
            case CHARSET_SLAVONIC:
                break;
            default:
                throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
        }
        try {
            byte[] commandBuffer = new byte[]{};
            switch (device.getProtocol()) {
                case Display.PROTOCOL_EPSON: {
                    Integer charsetByte = charsetsEPSON.get(charset);
                    if (charsetByte != null) {
                        commandBuffer = new byte[]{ 0x1B, 0x74, charsetByte.byteValue() };
                    }
                    break;
                }
                case Display.PROTOCOL_DSP800F: {
                    break;
                }
                case Display.PROTOCOL_CD5220: {
                    Integer charsetByte = charsetsCD5220.get(charset);
                    if (charsetByte != null) {
                        commandBuffer = new byte[]{ 0x1B, 0x63, charsetByte.byteValue() };
                    }
                    break;
                }
                case Display.PROTOCOL_DATECS: {
                    Integer charsetsByte = charsetsDATECS.get(charset);
                    if (charsetsByte != null) {
                        commandBuffer = new byte[]{ 0x1B, 0x74, charsetsByte.byteValue() };
                    }
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
