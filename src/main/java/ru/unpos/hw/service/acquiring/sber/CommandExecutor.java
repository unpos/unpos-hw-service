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

import ru.unpos.hw.service.acquiring.sber.types.CommandExecutorTask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author scream3r
 */
public class CommandExecutor {

    private Pilot pilot;
    private ExecutorService executor;

    protected CommandExecutor(Pilot pilot, final boolean setDaemon) {
        this.pilot = pilot;
        this.executor = Executors.newSingleThreadExecutor(new ThreadFactory() {

            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(setDaemon);
                return thread;
            }
        });
    }

    public void asyncExec(final CommandExecutorTask task) throws PilotDriverException {
        if (task == null) {
            throw new PilotDriverException("asyncExec()", PilotDriverException.DRIVER_NULL_NOT_PERMITTED);
        }
        executor.submit(new Runnable() {

            public void run() {
                executeTask(task);
            }
        });
    }

    public void syncExec(CommandExecutorTask task) throws PilotDriverException {
        if (task == null) {
            throw new PilotDriverException("syncExec()", PilotDriverException.DRIVER_NULL_NOT_PERMITTED);
        }
        executeTask(task);
    }

    private void executeTask(CommandExecutorTask task) {
        try {
            if (task.getCommand() == null) {
                throw new PilotDriverException("executeTask()", PilotDriverException.DRIVER_NULL_NOT_PERMITTED);
            }
            task.onSuccess(pilot.executeCommand(task.getCommand()));
        } catch (PilotException ex) {
            task.onException(ex);
        }
    }

    public boolean isShutdown() {
        return executor.isShutdown();
    }

    public void shutdown() {
        executor.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }
}
