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

import ru.unpos.hw.service.acquiring.sber.commands.CommandPrototype;

/**
 *
 * @author scream3r
 */
public class PilotCommandException extends PilotException {

    private CommandPrototype command;
    private int code;
    private String description;

    public PilotCommandException(CommandPrototype command, int code, String description) {
        super("Command name: " + (command != null ? command.getCommandName() : null) +
              "; Code: " + code +
              "; Description: " + description + ".");
        this.command = command;
        this.code = code;
        this.description = description;
    }

    public CommandPrototype getCommand() {
        return command;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}