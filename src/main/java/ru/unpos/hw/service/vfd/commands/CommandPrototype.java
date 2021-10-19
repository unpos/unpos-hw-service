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

/**
 *
 * @author scream3r
 */
public abstract class CommandPrototype {

    public final String getCommandName() {
        return this.getClass().getSimpleName();
    }

    public abstract void executeCommand(Display device) throws DisplayDriverException;

}
