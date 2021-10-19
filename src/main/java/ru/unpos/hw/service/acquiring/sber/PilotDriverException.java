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
package ru.unpos.hw.service.acquiring.sber;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author scream3r
 */
public class PilotDriverException extends PilotException {

    final public static int DRIVER_CAN_NOT_RUN_SB_PILOT_PROCESS = -1;
    final public static int DRIVER_CAN_NOT_WAIT_FOR_SB_PILOT_PROCESS_TERMINATION = -2;
    final public static int DRIVER_CAN_NOT_READ_TRANSACTION_INFO_FILE = -3;
    final public static int DRIVER_TRANSACTION_INFO_FILE_IS_EMPTY = -4;
    final public static int DRIVER_CAN_NOT_READ_SLIP_FILE = -5;
    final public static int DRIVER_NULL_NOT_PERMITTED = -6;
    final public static int DRIVER_INCORRECT_PARAMETER = -7;
    
    private static final ConcurrentHashMap<Integer, String> errorsTable = new ConcurrentHashMap<Integer, String>();

    static {
        errorsTable.put(DRIVER_CAN_NOT_RUN_SB_PILOT_PROCESS, "Не удалось запустить процесс sb_pilot");
        errorsTable.put(DRIVER_CAN_NOT_WAIT_FOR_SB_PILOT_PROCESS_TERMINATION, "Не удалось дождаться завершения процесса sb_pilot");
        errorsTable.put(DRIVER_CAN_NOT_READ_TRANSACTION_INFO_FILE, "Не удалось прочитать файл с информацией о транзакции");
        errorsTable.put(DRIVER_TRANSACTION_INFO_FILE_IS_EMPTY, "Файл с информацией о транзакции пуст");
        errorsTable.put(DRIVER_CAN_NOT_READ_SLIP_FILE, "Не удалось прочитать файл с чеком");
        errorsTable.put(DRIVER_NULL_NOT_PERMITTED, "Null параметр недопустим");
        errorsTable.put(DRIVER_INCORRECT_PARAMETER, "Некорректный параметр");
    }

    private String methodName;
    private int code;
    private String description;

    public PilotDriverException(String methodName, int code) {
        this(methodName, code, "");
    }

    public PilotDriverException(String methodName, int code, String description) {
        super("Method name: " + methodName +
              "; Code: " + code +
              "; Description: " + getDescriptionByCode(code) +
              ((description != null && !description.isEmpty()) ? "; Exception comment: " + description : "") +
              ".");
        this.methodName = methodName;
        this.code = code;
        this.description = description;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getCode() {
        return code;
    }

    public String getExceptionType() {
        return getDescriptionByCode(code);
    }

    public String getDescriptionByCode() {
        return description;
    }

    public static String getDescriptionByCode(int code) {
        String description = errorsTable.get(code);
        return description != null ? description : "Неизвестная ошибка";
    }
}
