package net.jjroman.homeautomation.osgi.measureservice.temp.consumer;

import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import org.osgi.service.log.LogService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Periodic Measure Consumer. Consumes Measure service and prints out reading of it every second.
 * Created by Jan on 13/03/2015.
 */
public class MeasureConsumer {
    private volatile DoubleMeasure doubleMeasure;
    private volatile LogService logService;

    private AtomicBoolean run = new AtomicBoolean();


    private final Thread workerThread;
    public MeasureConsumer(){
        workerThread = new Thread(new Runnable(){

            public void run() {
                logService.log(LogService.LOG_DEBUG, "run() called");
                while(run.get()){
                    try {
                        Thread.sleep(1000);
                        logService.log(LogService.LOG_INFO, String.format("Current Reading: %f%n", doubleMeasure.getValue()));
                    } catch (InterruptedException e) {
                        run.set(false);
                        logService.log(LogService.LOG_WARNING, "forcibly interrupted", e);
                        break;
                    }
                }
            }
        });
    }

    private void serviceAdded(){
        logService.log(LogService.LOG_DEBUG, "serviceAdded() called");
        run.set(true);
        logService.log(LogService.LOG_DEBUG, "run set to: " + run.get());
        workerThread.start();
    }
    private void serviceRemoved(){
        logService.log(LogService.LOG_DEBUG, "serviceRemoved() called");
        run.set(false);
        logService.log(LogService.LOG_DEBUG, "run set to: " + run.get());
        try {
            logService.log(LogService.LOG_DEBUG, "is Alive?");
            if(workerThread.isAlive()) {
                logService.log(LogService.LOG_DEBUG, "yes is Alive");
                workerThread.join(2000);
                logService.log(LogService.LOG_DEBUG, "Joined");
            }
        } catch (InterruptedException e) {
            logService.log(LogService.LOG_WARNING, "forcibly interrupted", e);
            workerThread.interrupt();
        }
    }
}
