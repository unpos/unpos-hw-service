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
public class Rollback extends CommandPrototype {

    @Override
    public int getCommandCode() {
        return 13;
    }

    @Override
    public ArrayList<String> getCommandArgs(Pilot device) throws PilotDriverException {
        double sum = device.getLastSum();
        if (sum < 0) {
            throw new PilotDriverException("getCommandArgs()", PilotDriverException.DRIVER_INCORRECT_PARAMETER);
        }
        ArrayList<String> args = new ArrayList<String>();
        args.add("" + (long)((sum + 0.005)*100));//Round "sum" value up to 2-symbols and convert to "long"
        if (device.getLastTransactionData() != null) {
            args.add(device.getLastTransactionData().getTransactionInfo().getAuthCode());
        }
        return args;
    }

    @Override
    public boolean shouldReadInfo() {
        return true;
    }

    @Override
    public boolean shouldReadSlip() {
        return true;
    }

}
