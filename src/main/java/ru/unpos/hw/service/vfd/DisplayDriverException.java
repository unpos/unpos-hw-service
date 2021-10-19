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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author scream3r
 */
@JsonIgnoreProperties({ "cause", "command", "localizedMessage", "message", "methodName", "stackTrace", "suppressed" })
public class DisplayDriverException extends Exception {

    final public static int DRIVER_COMMUNICATION_ERROR = -1;
    final public static int DRIVER_CONNECTION_NOT_ESTABLISHED = -2;
    final public static int DRIVER_CONNECTION_ALREADY_ESTABLISHED = -3;
    
    final public static int DRIVER_NULL_NOT_PERMITTED = -4;
    final public static int DRIVER_INCORRECT_PARAMETER = -5;
    final public static int DRIVER_PARAMETER_IS_OUT_OF_RANGE = -6;
    
    final public static int DRIVER_COMMAND_LENGTH_INCORRECT = -7;
    final public static int DRIVER_ANSWER_LENGTH_INCORRECT = -8;

    final public static int SERIAL_PORT_BUSY = -9;
    final public static int SERIAL_PORT_NOT_FOUND = -10;
    final public static int SERIAL_PORT_PERMISSION_DENIED = -11;
    final public static int SERIAL_PORT_INCORRECT = -12;
    final public static int SERIAL_PORT_PARAMETER_INCORRECT = -13;
    final public static int SERIAL_PORT_INTERFACE_ERROR = -14;

    private static final ConcurrentHashMap<Integer, String> errorsTable = new ConcurrentHashMap<Integer, String>();

    static {
        errorsTable.put(DRIVER_COMMUNICATION_ERROR, "Ошибка связи");
        errorsTable.put(DRIVER_CONNECTION_NOT_ESTABLISHED, "Соединение с устройством не установлено");
        errorsTable.put(DRIVER_CONNECTION_ALREADY_ESTABLISHED, "Соединение с устройством уже установлено");
        errorsTable.put(DRIVER_NULL_NOT_PERMITTED, "Null параметр недопустим");
        errorsTable.put(DRIVER_INCORRECT_PARAMETER, "Некорректный параметр");
        errorsTable.put(DRIVER_PARAMETER_IS_OUT_OF_RANGE, "Параметр вне диапазона");
        errorsTable.put(DRIVER_COMMAND_LENGTH_INCORRECT, "Некорректная длина комманды");
        errorsTable.put(DRIVER_ANSWER_LENGTH_INCORRECT, "Некорректная длина ответного сообщения");
        errorsTable.put(SERIAL_PORT_BUSY, "Последовательный порт занят");
        errorsTable.put(SERIAL_PORT_NOT_FOUND, "Последовательный порт не найден");
        errorsTable.put(SERIAL_PORT_PERMISSION_DENIED, "Недостаточно прав для работы с последовательным портом");
        errorsTable.put(SERIAL_PORT_INCORRECT, "Некорректный последовательный порт");
        errorsTable.put(SERIAL_PORT_PARAMETER_INCORRECT, "Некорректный параметр последовательного порта");
        errorsTable.put(SERIAL_PORT_INTERFACE_ERROR, "Интерфейсная ошибка последовательного порта");
    }

    private String portName;
    private String methodName;
    private int code;
    private String description;

    public DisplayDriverException(Display device, String methodName, int code) {
        super("Port name: " + (device != null ? device.getPortName() : null) +
              "; Method name: " + methodName +
              "; Code: " + code +
              "; Description: " + getExceptionTypeByCode(code) +
              ".");
        this.portName = (device != null ? device.getPortName() : null);
        this.methodName = methodName;
        this.code = code;
        this.description = getExceptionTypeByCode(code);
    }

    public String getPortName() {
        return portName;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getExceptionTypeByCode(int exceptionCode) {
        String exceptionType = errorsTable.get(exceptionCode);
        return exceptionType != null ? exceptionType : "Неизвестная ошибка";
    }
}
