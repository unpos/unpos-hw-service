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

/**
 *
 * @author scream3r
 */
public class TransactionData {

    private TransactionInfo transactionInfo;
    private String slip;
    private PilotException error;

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public String getSlip() {
        return slip;
    }

    public void setSlip(String slip) {
        this.slip = slip;
    }

    public PilotException getError() {
        return error;
    }

    public void setError(PilotException error) {
        this.error = error;
    }
}
