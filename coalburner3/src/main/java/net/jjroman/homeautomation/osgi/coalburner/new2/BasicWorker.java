package net.jjroman.homeautomation.osgi.coalburner.new2;

import net.jjroman.homeautomation.osgi.measureservice.api.MeasureConsumer;
import net.jjroman.homeautomation.osgi.measureservice.api.MeasureName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jan on 25/04/2015.
 */
public class BasicWorker implements Callable<WorkerExecutionStats>, MeasureConsumer<Double> {
    private final static Logger LOGGER = LoggerFactory.getLogger(BasicWorker.class);
    private final int cfgHeatFanHeadstart;
    private final int cfgHeatDelayBeforeDispensing;
    private final int cfgHeatDelayAfterDispensing;
    private final int cfgHeatDispensing;

    private final double cfgHeatCutOffTemp;
    private final double cfgHeatStartTriggerTemp;

    private final int cfgMaintainIdleThreshold;
    private final int cfgMaintainFanBeforeDispensing;
    private final int cfgMaintainDispensing;
    private final int cfgMaintainFanAfterDispensing;



    private volatile TurnedOnState currentState = TurnedOnState.MAINTAIN;
    private volatile boolean turnedOn = true;
    private volatile boolean running = false;

    private final CoalBurnerExternals coalBurnerExternals;

    private BasicWorker(Builder builder) {
        // TODO validation ...

        this.cfgHeatFanHeadstart = builder.cfgHeatFanHeadstart;
        this.cfgHeatDelayBeforeDispensing = builder.cfgHeatDelayBeforeDispensing;
        this.cfgHeatDelayAfterDispensing = builder.cfgHeatDelayAfterDispensing;
        this.cfgHeatDispensing = builder.cfgHeatDispensing;

        this.cfgMaintainIdleThreshold = builder.cfgMaintainIdleThreshold;
        this.cfgMaintainFanBeforeDispensing = builder.cfgMaintainFanBeforeDispensing;
        this.cfgMaintainDispensing = builder.cfgMaintainDispensing;
        this.cfgMaintainFanAfterDispensing = builder.cfgMaintainFanAfterDispensing;
        this.coalBurnerExternals = builder.coalBurnerExternals;

        this.cfgHeatCutOffTemp = builder.cfgHeatCutOffTemp;
        this.cfgHeatStartTriggerTemp = builder.cfgHeatStartTriggerTemp;
/*
        // VALIDATION
        if(cfgHeatCutOffTemp <= cfgHeatStartTriggerTemp){
            throw new Misconfiguration("Heat cut off temperature need to be higher than start heat temperature");
        }
        if(cfgHeatFanHeadstart < 0){
            throw new Misconfiguration("Heat fan headstart cannot by less then 0");
        }
        if(cfgHeatFanHeadstart > 60){
            throw new Misconfiguration("Heat fan headstart cannot by more then 60");
        }
        // TODO validation framework ...
*/

    }

    public static class Builder {
        private int cfgHeatFanHeadstart;
        private int cfgHeatDelayBeforeDispensing;
        private int cfgHeatDelayAfterDispensing;
        private int cfgHeatDispensing;

        private int cfgMaintainIdleThreshold;
        private int cfgMaintainFanBeforeDispensing;
        private int cfgMaintainDispensing;
        private int cfgMaintainFanAfterDispensing;

        private double cfgHeatCutOffTemp;
        private double cfgHeatStartTriggerTemp;

        private final CoalBurnerExternals coalBurnerExternals;

        public Builder(CoalBurnerExternals coalBurnerExternals) {
            this.coalBurnerExternals = coalBurnerExternals;
        }

        public Builder heatFanHeadstart(int heatFanHeadstart) {
            this.cfgHeatFanHeadstart = heatFanHeadstart;
            return this;
        }

        public Builder heatDelayBeforeDispensing(int DelayBeforeDispensing) {
            this.cfgHeatDelayBeforeDispensing = DelayBeforeDispensing;
            return this;
        }

        public Builder heatDelayAfterDispensing(int heatDelayAfterDispensing) {
            this.cfgHeatDelayAfterDispensing = heatDelayAfterDispensing;
            return this;
        }

        public Builder heatDispensing(int heatDispensing) {
            this.cfgHeatDispensing = heatDispensing;
            return this;
        }

        public Builder maintainIdleThreshold(int maintainIdleThreshold) {
            this.cfgMaintainIdleThreshold = maintainIdleThreshold;
            return this;
        }

        public Builder maintainFanBeforeDispensing(int maintainFanBeforeDispensing) {
            this.cfgMaintainFanBeforeDispensing = maintainFanBeforeDispensing;
            return this;
        }

        public Builder maintainDispensing(int maintainDispensing) {
            this.cfgMaintainDispensing = maintainDispensing;
            return this;
        }

        public Builder maintainFanAfterDispensing(int maintainFanAfterDispensing) {
            this.cfgMaintainFanAfterDispensing = maintainFanAfterDispensing;
            return this;
        }

        public Builder heatCutOff(double temperature){
            this.cfgHeatCutOffTemp = temperature;
            return this;
        }
        public Builder heatStartTrigger(double temperature){
            this.cfgHeatStartTriggerTemp = temperature;
            return this;
        }

        public BasicWorker build(){
            return new BasicWorker(this);
        }

    }


    public synchronized boolean isRunning() {
        return running;
    }
    /**
     * Returns maximum timeout for gracefull stop
     * @return
     */
    public synchronized int turnOff(){
        this.turnedOn = false;
        int heatCycleDuration = cfgHeatFanHeadstart + cfgHeatDelayBeforeDispensing + cfgHeatDispensing + cfgHeatDelayAfterDispensing;
        int maintainCycleDuration = cfgMaintainFanBeforeDispensing + cfgMaintainDispensing + cfgMaintainFanAfterDispensing;
        return Math.max(heatCycleDuration, maintainCycleDuration );
    }

    private void heat(){
        this.currentState = TurnedOnState.HEAT;
    }

    private void maintain(){
        this.currentState = TurnedOnState.MAINTAIN;
    }

    @Override
    public synchronized void updateMeasure(Double newValue, MeasureName name) {
        if(MeasureName.WATER_TANK.equals(name)){
            if(newValue > cfgHeatCutOffTemp){
                LOGGER.info("Changing to maintain state");
                this.maintain();
            }else if(newValue < cfgHeatStartTriggerTemp){
                LOGGER.info("Changing to heat state");
                this.heat();
            }else{
                LOGGER.info("Remain in current state");
            }
        }

    }

    @Override
    public WorkerExecutionStats call() throws Exception {
        try {
            LOGGER.info("Worker started");
            while (turnedOn) {
                running = true;
                if (TurnedOnState.HEAT.equals(currentState) && turnedOn) {
                    // todo ENTERED HEAT STATE EVENT
                    coalBurnerExternals.fanStart();
                    TimeUnit.SECONDS.sleep(cfgHeatFanHeadstart);
                    while (TurnedOnState.HEAT.equals(currentState) && turnedOn) {
                        TimeUnit.SECONDS.sleep(cfgHeatDelayBeforeDispensing);
                        coalBurnerExternals.dispenserStart();
                        TimeUnit.SECONDS.sleep(cfgHeatDispensing);
                        coalBurnerExternals.dispenserStop();
                        TimeUnit.SECONDS.sleep(cfgHeatDelayAfterDispensing);
                    }
                    coalBurnerExternals.fanStop();
                } else if (TurnedOnState.MAINTAIN.equals(currentState)) {
                    // todo ENTERED HEAT STATE EVENT
                    int counter = 0;
                    while (TurnedOnState.MAINTAIN.equals(currentState) && turnedOn) {
                        counter++;
                        TimeUnit.SECONDS.sleep(1);
                        if (counter > cfgMaintainIdleThreshold) {
                            counter = 0;
                            coalBurnerExternals.fanStart();
                            TimeUnit.SECONDS.sleep(cfgMaintainFanBeforeDispensing);
                            coalBurnerExternals.dispenserStart();
                            TimeUnit.SECONDS.sleep(cfgMaintainDispensing);
                            coalBurnerExternals.dispenserStop();
                            TimeUnit.SECONDS.sleep(cfgMaintainFanAfterDispensing);
                            coalBurnerExternals.fanStop();
                        }
                    }
                } else {
                    LOGGER.error("Unsupported state");
                    TimeUnit.SECONDS.sleep(1);
                }
            }
            return new WorkerExecutionStats();
        }catch (InterruptedException e) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();
            return new WorkerExecutionStats();
        }finally {
            running = false;
            coalBurnerExternals.fanStop();
            coalBurnerExternals.dispenserStop();
            LOGGER.info("Worker finished");
        }
    }
}
