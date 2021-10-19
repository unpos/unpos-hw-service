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
public class SetFont extends BasicCommandPrototype {
    
    public static final int FONT_USA = 0;
    public static final int FONT_DENMARK_I = 1;
    public static final int FONT_DENMARK_II = 2;
    public static final int FONT_FRANCE = 3;
    public static final int FONT_GERMANY = 4;
    public static final int FONT_ITALY = 5;
    public static final int FONT_JAPAN = 6;
    public static final int FONT_SLAVONIC = 7;
    public static final int FONT_NORWAY = 8;
    public static final int FONT_RUSSIA = 9;
    public static final int FONT_SPAIN = 10;
    public static final int FONT_UK = 11;
    public static final int FONT_SWEDEN = 12;
    
    private static final ConcurrentHashMap<Integer, Integer> fontsEPSON = new ConcurrentHashMap<Integer, Integer>();
    private static final ConcurrentHashMap<Integer, Integer> fontsDSP800F = new ConcurrentHashMap<Integer, Integer>();
    private static final ConcurrentHashMap<Integer, Integer> fontsCD5220 = new ConcurrentHashMap<Integer, Integer>();
    private static final ConcurrentHashMap<Integer, Integer> fontsDATECS = new ConcurrentHashMap<Integer, Integer>();
    
    static {
        fontsEPSON.put(FONT_USA, 0x00);
        fontsEPSON.put(FONT_DENMARK_I, 0x04);
        fontsEPSON.put(FONT_DENMARK_II, 0x0A);
        fontsEPSON.put(FONT_FRANCE, 0x01);
        fontsEPSON.put(FONT_GERMANY, 0x02);
        fontsEPSON.put(FONT_ITALY, 0x06);
        fontsEPSON.put(FONT_JAPAN, 0x08);
        fontsEPSON.put(FONT_SLAVONIC, 0x0B);
        fontsEPSON.put(FONT_NORWAY, 0x09);
        fontsEPSON.put(FONT_RUSSIA, 0x0C);
        fontsEPSON.put(FONT_SPAIN, 0x07);
        fontsEPSON.put(FONT_UK, 0x03);
        fontsEPSON.put(FONT_SWEDEN, 0x05);
        
        fontsDSP800F.put(FONT_USA, 0x30);
        fontsDSP800F.put(FONT_DENMARK_I, 0x34);
        fontsDSP800F.put(FONT_DENMARK_II, 0x3A);
        fontsDSP800F.put(FONT_FRANCE, 0x31);
        fontsDSP800F.put(FONT_GERMANY, 0x32);
        fontsDSP800F.put(FONT_ITALY, 0x36);
        fontsDSP800F.put(FONT_JAPAN, 0x38);
        fontsDSP800F.put(FONT_NORWAY, 0x39);
        fontsDSP800F.put(FONT_SPAIN, 0x37);
        fontsDSP800F.put(FONT_UK, 0x33);
        fontsDSP800F.put(FONT_SWEDEN, 0x35);
        
        fontsCD5220.put(FONT_USA, 0x41);
        fontsCD5220.put(FONT_DENMARK_I, 0x44);
        fontsCD5220.put(FONT_DENMARK_II, 0x45);
        fontsCD5220.put(FONT_FRANCE, 0x46);
        fontsCD5220.put(FONT_GERMANY, 0x47);
        fontsCD5220.put(FONT_ITALY, 0x49);
        fontsCD5220.put(FONT_JAPAN, 0x4A);
        fontsCD5220.put(FONT_SLAVONIC, 0x4C);
        fontsCD5220.put(FONT_NORWAY, 0x4E);
        fontsCD5220.put(FONT_RUSSIA, 0x52);
        fontsCD5220.put(FONT_SPAIN, 0x53);
        fontsCD5220.put(FONT_UK, 0x55);
        fontsCD5220.put(FONT_SWEDEN, 0x57);
        
        fontsDATECS.put(FONT_USA, 0x00);
        fontsDATECS.put(FONT_DENMARK_I, 0x04);
        fontsDATECS.put(FONT_DENMARK_II, 0x0A);
        fontsDATECS.put(FONT_FRANCE, 0x01);
        fontsDATECS.put(FONT_GERMANY, 0x02);
        fontsDATECS.put(FONT_ITALY, 0x06);
        fontsDATECS.put(FONT_JAPAN, 0x08);
        fontsDATECS.put(FONT_NORWAY, 0x09);
        fontsDATECS.put(FONT_SPAIN, 0x07);
        fontsDATECS.put(FONT_UK, 0x03);
        fontsDATECS.put(FONT_SWEDEN, 0x05);
    }
    
    private int font;

    public SetFont(int font) {
        this.font = font;
    }

    private SetFont() {
        //Empty constructor
    }

    @Override
    protected void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException {
        switch (font) {
            case FONT_USA:
            case FONT_DENMARK_I:
            case FONT_DENMARK_II:
            case FONT_FRANCE:
            case FONT_GERMANY:
            case FONT_ITALY:
            case FONT_JAPAN:
            case FONT_SLAVONIC:
            case FONT_NORWAY:
            case FONT_RUSSIA:
            case FONT_SPAIN:
            case FONT_UK:
            case FONT_SWEDEN:
                break;
            default:
                throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
        }
        try {
            byte[] commandBuffer = new byte[]{};
            switch (device.getProtocol()) {
                case Display.PROTOCOL_EPSON: {
                    Integer fontByte = fontsEPSON.get(font);
                    if (fontByte != null) {
                        commandBuffer = new byte[]{ 0x1B, 0x52, fontByte.byteValue() };
                    }
                    break;
                }
                case Display.PROTOCOL_DSP800F: {
                    Integer fontByte = fontsDSP800F.get(font);
                    if (fontByte != null) {
                        commandBuffer = new byte[]{ 0x04, 0x01, 0x49, fontByte.byteValue(), 0x17 };
                    }
                    break;
                }
                case Display.PROTOCOL_CD5220: {
                    Integer fontByte = fontsCD5220.get(font);
                    if (fontByte != null) {
                        commandBuffer = new byte[]{ 0x1B, 0x66, fontByte.byteValue() };
                    }
                    break;
                }
                case Display.PROTOCOL_DATECS: {
                    Integer fontByte = fontsDATECS.get(font);
                    if (fontByte != null) {
                        commandBuffer = new byte[]{ 0x1B, 0x52, fontByte.byteValue() };
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
        super.execute(device);
    }

}
