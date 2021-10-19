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

import jssc.SerialNativeInterface;
import ru.unpos.hw.service.acquiring.sber.commands.CommandPrototype;
import ru.unpos.hw.service.acquiring.sber.commands.ReturnSale;
import ru.unpos.hw.service.acquiring.sber.commands.Rollback;
import ru.unpos.hw.service.acquiring.sber.commands.Sale;
import ru.unpos.hw.service.acquiring.sber.types.TransactionData;
import ru.unpos.hw.service.acquiring.sber.types.TransactionInfo;

import javax.inject.Singleton;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author scream3r
 */
@Singleton
public class Pilot {

    public static final String wrapperVersion = "0.15";  //26.06.15
    public static final String wrapperMinorSuffix = "1"; //26.06.15
    public static final int INTERMEDIATE_CODE = 4353;

    private CommandExecutor executor;
    private TransactionData lastTransactionData;
    private double lastSum;

    private String pilotWorkingDirectory = System.getProperty("user.home") + "/sb_pilot/";
    private File pilotBin = new File(pilotWorkingDirectory + "sb_pilot");

    private String resultFileName = "e";
    private String slipFileName = "p";

    private Exception lastException;

    public static String getWrapperVersion() {
        return wrapperVersion + "." + wrapperMinorSuffix;
    }

    public TransactionData getLastTransactionData() {
        return lastTransactionData;
    }

    public double getLastSum() {
        return lastSum;
    }

    public Exception getLastException() {
        return lastException;
    }

    protected void setLastException(Exception lastException) {
        this.lastException = lastException;
    }

    private void checkBinaries() throws PilotDriverException {
        
    }

    public CommandExecutor getCommandExecutor() {
        if (executor == null) {
            executor = new CommandExecutor(this, false);
        }
        return executor;
    }

    public synchronized TransactionData executeCommand(CommandPrototype command) throws PilotDriverException, PilotCommandException {
        checkBinaries();
        TransactionData transactionData = new TransactionData();
        //if (command instanceof Rollback && lastTransactionData == null) {
            //return transactionData;
        //}
        ArrayList<String> args = new ArrayList<String>();
        if (SerialNativeInterface.getOsType() == SerialNativeInterface.OS_LINUX) {
            args.add("env");
            args.add("TERM=xterm");
        }
        args.add(pilotWorkingDirectory + "sb_pilot");
        args.add("" + command.getCommandCode());
        ArrayList<String> commandArgs = command.getCommandArgs(this);
        if (commandArgs != null) {
            args.addAll(commandArgs);
        }
        try {
            boolean readInfo = command.shouldReadInfo();
            boolean readSlip = command.shouldReadSlip();

            ProcessBuilder processBuilder = new ProcessBuilder(args.toArray(new String[args.size()]));
            processBuilder.directory(new File(pilotWorkingDirectory));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            new StreamGobbler(process.getInputStream()).start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                readInfo = true;
            }
            if (readInfo) {
                transactionData.setTransactionInfo(readInfo(command));
            }
            if ((readInfo && !transactionData.getTransactionInfo().isIntermediate() && readSlip) || (!readInfo && readSlip)) {
                transactionData.setSlip(readSlip());
            }
        } catch (IOException ex) {
            throw new PilotDriverException("executeCommand()", PilotDriverException.DRIVER_CAN_NOT_RUN_SB_PILOT_PROCESS, ex.getMessage());
        } catch (InterruptedException ex) {
            throw new PilotDriverException("executeCommand()", PilotDriverException.DRIVER_CAN_NOT_WAIT_FOR_SB_PILOT_PROCESS_TERMINATION, ex.getMessage());
        }
        if (command instanceof Sale || command instanceof ReturnSale) {
            lastTransactionData = transactionData;
            lastSum = (command instanceof Sale) ? ((Sale)command).getSum() : ((ReturnSale)command).getSum();
        } else if (command instanceof Rollback) {
            lastTransactionData = null;
            lastSum = 0;
        }
        return transactionData;
    }

    private TransactionInfo readInfo(CommandPrototype command) throws PilotDriverException, PilotCommandException {
        BufferedReader reader = null;
        try {
            String charset = SerialNativeInterface.getOsType() == SerialNativeInterface.OS_LINUX ? "KOI8-R" : "CP866";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(pilotWorkingDirectory + "e"), charset));
            ArrayList<String> list = new ArrayList<String>();
            String str = null;
            while ((str = reader.readLine()) != null) {
                list.add(str);
            }
            if (list.size() > 0) {
                String[] resultArray = list.get(0).split(",");
                int resultCode = Integer.valueOf(resultArray[0]);
                String resultMessage = resultArray.length > 1 ? resultArray[1] : "";
                if (resultCode != 0 && resultCode != INTERMEDIATE_CODE) {
                    throw new PilotCommandException(command, resultCode, resultMessage);
                }
                boolean intermediate = resultCode == INTERMEDIATE_CODE;
                String cardNumber = list.get(1);
                String cardExpDate = list.get(2);
                String authCode = list.get(3);
                String operationNumber = list.get(4);
                String cardType = list.get(5);
                String cardSB = "0";//list.get(6);
                String terminalNumber = list.get(7);
                String timeStamp = list.get(8);
                String RRN = list.get(9);
                String SHA1 = list.get(10);
                return new TransactionInfo(intermediate, cardNumber, cardExpDate, authCode, operationNumber, cardType, cardSB, terminalNumber, timeStamp, RRN, SHA1);
            } else {
                throw new PilotDriverException("readInfo()", PilotDriverException.DRIVER_TRANSACTION_INFO_FILE_IS_EMPTY);
            }
        } catch (IOException ex) {
            throw new PilotDriverException("readInfo()", PilotDriverException.DRIVER_CAN_NOT_READ_TRANSACTION_INFO_FILE, ex.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    //Do nothing
                }
            }
        }
    }

    private String readSlip() throws PilotDriverException {
        String slip = "";
        BufferedReader reader = null;
        try {
            String charset = SerialNativeInterface.getOsType() == SerialNativeInterface.OS_LINUX ? "KOI8-R" : "CP866";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(pilotWorkingDirectory + "p"), charset));
            boolean firstLine = true;
            String str = null;
            while ((str = reader.readLine()) != null) {
                if (!firstLine) {
                    str = ("\n" + str);
                } else {
                    firstLine = false;
                }
                slip += str;
            }
        } catch (IOException ex) {
            throw new PilotDriverException("readSlip()", PilotDriverException.DRIVER_CAN_NOT_READ_SLIP_FILE, ex.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    //Do nothing
                }
            }
        }
        return slip;
    }

    private class StreamGobbler extends Thread {

        private InputStream input;

        public StreamGobbler(InputStream input) {
            this.input = input;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                while (!interrupted() && reader.readLine() != null) {
                    //Do nothing
                }
            } catch (IOException ex) {
                //Do nothing
            }
        }
    }
}
