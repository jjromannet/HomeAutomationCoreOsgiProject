package net.jjroman.homeautomation.osgi.measureservice.temp.consumer;

import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jan on 13/03/2015.
 */
public class MeasureConsumer {
    private volatile DoubleMeasure doubleMeasure;

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
                        e.printStackTrace();
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
            e.printStackTrace();
            System.out.println("force interrupted");
            workerThread.interrupt();
        }
    }
}
