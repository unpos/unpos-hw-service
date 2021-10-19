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
package ru.unpos.hw.service.acquiring.sber.commands;

import ru.unpos.hw.service.acquiring.sber.Pilot;
import ru.unpos.hw.service.acquiring.sber.PilotCommandException;
import ru.unpos.hw.service.acquiring.sber.PilotDriverException;

import java.util.ArrayList;

/**
 *
 * @author scream3r
 */
public abstract class CommandPrototype {

    public final String getCommandName() {
        return this.getClass().getSimpleName();
    }

    public void executeCommand(Pilot device) throws PilotDriverException, PilotCommandException {
        if (device == null) {
            throw new PilotDriverException(getCommandName() + ".executeCommand()", PilotDriverException.DRIVER_NULL_NOT_PERMITTED);
        }
        device.executeCommand(this);
    }

    public abstract int getCommandCode();

    public abstract ArrayList<String> getCommandArgs(Pilot device) throws PilotDriverException;

    public abstract boolean shouldReadInfo();

    public abstract boolean shouldReadSlip();

}
