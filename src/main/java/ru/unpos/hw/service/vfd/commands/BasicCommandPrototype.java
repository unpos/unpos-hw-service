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

/**
 *
 * @author scream3r
 */
public abstract class BasicCommandPrototype extends CommandPrototype {

    private ByteArrayOutputStream outputStream;
    
    /**
     * Получение данных команды в виде массива байт. Перед формированием массива байт
     * производится проверка данных в реализованном методе prepareCommand().
     *
     * @throws DisplayDriverException
     */
    public byte[] getCommandByteArray(Display device) throws DisplayDriverException {
        if (device == null) {
            throw new DisplayDriverException(device, "getCommandByteArray()", DisplayDriverException.DRIVER_NULL_NOT_PERMITTED);
        }
        outputStream = new ByteArrayOutputStream();
        prepareCommand(device, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Выполнить команду. Перед выполнением команды производится проверка
     * данных в реализованном методе prepareCommand().
     *
     * @throws DisplayDriverException
     */
    public void execute(Display device) throws DisplayDriverException {
        if (device == null) {
            throw new DisplayDriverException(device, "execute()", DisplayDriverException.DRIVER_NULL_NOT_PERMITTED);
        }
        device.excecuteCommand(this);
    }

    /**
     * Подготовка внутренних данных команды. Данный метод должен быть реализован в классе команды и производить
     * все необходимые проверки корректности входных данных. Если ошибок нет, то данные должны быть записаны в поток ByteArrayOutputStream.
     *
     * @param outputStream
     * @throws DisplayDriverException
     */
    protected abstract void prepareCommand(Display device, ByteArrayOutputStream outputStream) throws DisplayDriverException;
}
