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
import ru.unpos.hw.service.acquiring.sber.PilotDriverException;

import java.util.ArrayList;

/**
 *
 * @author scream3r
 */
public class ShowTerminalMenu extends CommandPrototype {

    @Override
    public int getCommandCode() {
        return 11;
    }

    @Override
    public ArrayList<String> getCommandArgs(Pilot device) throws PilotDriverException {
        return null;
    }

    @Override
    public boolean shouldReadInfo() {
        return false;
    }

    @Override
    public boolean shouldReadSlip() {
        return true;
    }

}
