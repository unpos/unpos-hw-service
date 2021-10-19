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
package ru.unpos.hw.service.acquiring.sber.types;

import ru.unpos.hw.service.acquiring.sber.PilotException;
import ru.unpos.hw.service.acquiring.sber.commands.CommandPrototype;

/**
 *
 * @author scream3r
 */
public abstract class CommandExecutorTask {

    private CommandPrototype command;

    public CommandExecutorTask(CommandPrototype command) {
        this.command = command;
    }

    public CommandPrototype getCommand() {
        return command;
    }

    public abstract void onSuccess(TransactionData transactionData);

    public abstract void onException(PilotException ex);

}
