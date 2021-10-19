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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author scream3r
 */
@JsonIgnoreProperties({ "cause", "command", "localizedMessage", "message", "methodName", "stackTrace", "suppressed" })
public class PilotException extends Exception {

    public PilotException(String message) {
        super(message);
    }

}
