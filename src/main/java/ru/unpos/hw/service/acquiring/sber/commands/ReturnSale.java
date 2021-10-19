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
public class ReturnSale extends CommandPrototype {

    private double sum;
    private int cardType;
    private String track2;

    public ReturnSale(double sum, int cardType, String track2) {
        this.sum = sum;
        this.cardType = cardType;
        this.track2 = track2;
    }

    public double getSum() {
        return sum;
    }

    @Override
    public int getCommandCode() {
        return 3;
    }

    @Override
    public ArrayList<String> getCommandArgs(Pilot device) throws PilotDriverException {
        if (sum < 0) {
            throw new PilotDriverException("getCommandArgs()", PilotDriverException.DRIVER_INCORRECT_PARAMETER);
        }
        ArrayList<String> args = new ArrayList<String>();
        args.add("" + (long)((sum + 0.005)*100));//Round "sum" value up to 2-symbols and convert to "long"
        args.add("" + cardType);
        if (track2 != null) {
            args.add(track2);
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
