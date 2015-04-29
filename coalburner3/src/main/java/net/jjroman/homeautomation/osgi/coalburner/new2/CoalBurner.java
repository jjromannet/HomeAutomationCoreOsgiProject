package net.jjroman.homeautomation.osgi.coalburner.new2;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigChangeConsumer;
import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import net.jjroman.homeautomation.osgi.measureservice.api.MeasureName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jan on 24/04/2015.
 */
public class CoalBurner implements ConfigChangeConsumer{

    private static final Logger LOGGER = LoggerFactory.getLogger(CoalBurner.class);
    private final DoubleMeasure doubleMeasure;
    private final CoalBurnerExternals coalBurnerExternals;
    private ExecutorService executorService = null;
    Future<WorkerExecutionStats> currentlyScheduledFeature = null;
    BasicWorker currentBasicWorker = null;


    public CoalBurner(CoalBurnerExternals coalBurnerExternals, DoubleMeasure doubleMeasure){
        this.coalBurnerExternals = coalBurnerExternals;
        this.doubleMeasure = doubleMeasure;
    }

    private synchronized void stopCurrentWorker(){
        // cancel previous execution
        if(currentlyScheduledFeature != null && currentBasicWorker != null) {
            int toGracefullStop = currentBasicWorker.turnOff(); // gracefull stop
            LOGGER.info(String.format("gracefull stop of current worker will take up to %d seconds", toGracefullStop) );
            try {
                int counter = 0;
                while(counter++ < toGracefullStop && !currentlyScheduledFeature.isDone() ){
                    TimeUnit.SECONDS.sleep(1);
                }
                if(currentlyScheduledFeature.isDone()) {
                    LOGGER.info(String.format("Worker gracefully stopped in: %d seconds", counter));
                }else{
                    LOGGER.warn(String.format("Could not stop worker gracefully timeouted after %d seconds", counter));
                }
            } catch (InterruptedException ie) {
                LOGGER.warn("Worker graceful stop interupted, will try to stop forcefully", ie);
                Thread.currentThread().interrupt();
            }
            if (!currentlyScheduledFeature.isDone()) {
                LOGGER.warn("Try to to stop worker forcefully");
                currentlyScheduledFeature.cancel(true);
            }else{
                LOGGER.info("Worker stopped gracefully");
            }
        }else{
            LOGGER.info("First execution no currently scheduled feature or worker");
        }
    }


    public void init(){
        executorService = Executors.newSingleThreadExecutor();
    }

    public void deinit(){
        stopCurrentWorker();
        executorService.shutdownNow();
    }



    @Override
    public synchronized void updateConfig(Map<String, String> newImmutableMap) {

        BasicWorker bw = new BasicWorker.Builder(coalBurnerExternals)
                .heatDelayAfterDispensing(Integer.parseInt(newImmutableMap.get("heatDelayAfterDispensing")))
                .heatDelayBeforeDispensing(Integer.parseInt(newImmutableMap.get("heatDelayBeforeDispensing")))
                .heatDispensing(Integer.parseInt(newImmutableMap.get("heatDispensing")))
                .heatFanHeadstart(Integer.parseInt(newImmutableMap.get("heatFanHeadstart")))
                .maintainDispensing(Integer.parseInt(newImmutableMap.get("maintainDispensing")))
                .maintainFanAfterDispensing(Integer.parseInt(newImmutableMap.get("maintainFanAfterDispensing")))
                .maintainFanBeforeDispensing(Integer.parseInt(newImmutableMap.get("maintainFanBeforeDispensing")))
                .maintainIdleThreshold(Integer.parseInt(newImmutableMap.get("maintainIdleThreshold")))
                .heatCutOff(Double.parseDouble(newImmutableMap.get("heatCutOffTemp")))
                .heatStartTrigger(Double.parseDouble(newImmutableMap.get("heatStartTriggerTemp")))
                .build();

        doubleMeasure.subscribeToMeasureChange(bw, MeasureName.WATER_TANK);

        stopCurrentWorker();

        currentBasicWorker = bw;
        currentlyScheduledFeature = executorService.submit(currentBasicWorker);
        // TODO Monitor time in the queue

    }



}
