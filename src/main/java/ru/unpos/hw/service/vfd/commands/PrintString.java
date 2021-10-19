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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * 
 * @author scream3r
 */
public class PrintString extends CombinedCommandPrototype {

    private String stringForPrinting;
    private int position;
    private boolean moveCursor;

    public PrintString(String stringForPrinting) {
        this.stringForPrinting = stringForPrinting;
    }
    
    public PrintString(String stringForPrinting, int position) {
        this.stringForPrinting = stringForPrinting;
        this.position = position;
        moveCursor = true;
    }

    @Override
    public void executeCommand(Display device) throws DisplayDriverException {
        if (moveCursor) {
            new CursorToPosition(position).executeCommand(device);
        }
        new PrintSimpleString(stringForPrinting).executeCommand(device);
    }

    public class PrintSimpleString extends BasicCommandPrototype {

        private String stringForPrinting;
        private final HashMap<Integer, Integer> charsTable = new HashMap<Integer, Integer>();

        public PrintSimpleString(String stringForPrinting) {
            this.stringForPrinting = stringForPrinting;
            charsTable.put(0x81, 0xDA); //Б
            charsTable.put(0x83, 0xDB); //Г
            charsTable.put(0x84, 0xDC); //Д
            charsTable.put(0x86, 0xDD); //Ж
            charsTable.put(0x87, 0xDE); //З
            charsTable.put(0x88, 0xDF); //И
            charsTable.put(0x89, 0xE0); //Й
            charsTable.put(0x8B, 0xE1); //Л
            charsTable.put(0x8F, 0xE2); //П
            charsTable.put(0x93, 0xE3); //У
            charsTable.put(0x94, 0xE4); //Ф
            charsTable.put(0x96, 0xE5); //Ц
            charsTable.put(0x97, 0xE6); //Ч
            charsTable.put(0x98, 0xE7); //Ш
            charsTable.put(0x99, 0xE8); //Щ
            charsTable.put(0x9A, 0xE9); //Ъ
            charsTable.put(0x9B, 0xEA); //Ы
            charsTable.put(0x9D, 0xEB); //Э
            charsTable.put(0x9E, 0xEC); //Ю
            charsTable.put(0x9F, 0xED); //Я
        }

        @Override
        protected void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException {
            if (stringForPrinting == null) {
                throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_NULL_NOT_PERMITTED);
            }
            try {
                byte[] buffer = null;
                switch (device.getEncoding()) {
                    case Display.ENCODING_WITHOUT: {
                        buffer = stringForPrinting.getBytes();
                        break;
                    }
                    case Display.ENCODING_CP866: {
                        buffer = stringForPrinting.getBytes("cp866");
                        break;
                    }
                    case Display.ENCODING_JAPANESE_POSIFLEX: {
                        buffer = encodeForPosiflex(device, stringForPrinting);
                        break;
                    }
                }
                outputStream.write(buffer);
            } catch (Exception ex) {
                throw new DisplayDriverException(device, "prepareCommand()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
            }
        }

        @Override
        public void executeCommand(Display device) throws DisplayDriverException {
            super.execute(device);
        }

        private byte[] encodeForPosiflex(Display device, String str) throws DisplayDriverException {
            str = str.toUpperCase();
            str = str.replace('А', 'A')
                     .replace('В', 'B')
                     .replace('Е', 'E')
                     .replace('Ё', 'E')
                     .replace('К', 'K')
                     .replace('М', 'M')
                     .replace('Н', 'H')
                     .replace('О', 'O')
                     .replace('Р', 'P')
                     .replace('С', 'C')
                     .replace('Т', 'T')
                     .replace('Х', 'X')
                     .replace('Ь', 'b');
            try {
                byte[] buffer = str.getBytes("cp866");
                for (int i = 0; i < buffer.length; i++) {
                    if (charsTable.containsKey(buffer[i] & 0xFF)) {
                        buffer[i] = charsTable.get(buffer[i] & 0xFF).byteValue();
                    }
                }
                return buffer;
            } catch (UnsupportedEncodingException ex) {
                throw new DisplayDriverException(device, "encodeForPosiflex()", DisplayDriverException.DRIVER_INCORRECT_PARAMETER);
            }
        }

    }

}
