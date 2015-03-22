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
    private volatile LogService m_log;

    private AtomicBoolean run = new AtomicBoolean();


    private final Thread workerThread;
    public MeasureConsumer(){
        workerThread = new Thread(new Runnable(){

            public void run() {
                System.out.println("run() called");
                while(run.get()){
                    try {
                        Thread.sleep(1000);
                        System.out.print(String.format("Current Reading: %f\n", doubleMeasure.getValue()));
                    } catch (InterruptedException e) {
                        run.set(false);
                        m_log.log(LogService.LOG_WARNING, "forcibly interrupted", e);
                        break;
                    }
                }
            }
        });
    }

    private void serviceAdded(){
        System.out.println("serviceAdded() called");
        run.set(true);
        System.out.println("run set to: " + run.get());
        workerThread.start();
    }
    private void serviceRemoved(){
        System.out.println("serviceRemoved() called");
        run.set(false);
        System.out.println("run set to: " + run.get());
        try {
            System.out.println("is Alive?");
            if(workerThread.isAlive()) {
                System.out.println("yes is Alive");
                workerThread.join(2000);
                System.out.println("Joined");
            }
        } catch (InterruptedException e) {
            m_log.log(LogService.LOG_WARNING, "forcibly interrupted", e);
            System.out.println("forcibly interrupted");
            workerThread.interrupt();
        }
    }
}
