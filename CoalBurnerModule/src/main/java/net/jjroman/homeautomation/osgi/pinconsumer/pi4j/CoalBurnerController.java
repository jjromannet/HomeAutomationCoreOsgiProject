package net.jjroman.homeautomation.osgi.pinconsumer.pi4j;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;
import org.osgi.service.log.LogService;

/**
 * Main Logic for controlling Coal burner is defined here.
 *
 * Coal burner have two engines
 * 1) Fan - which blows air into coal burning chamber
 * 2) Dispenser - which dispense coal
 *
 * Created by Jan on 14/03/2015.
 */
public class CoalBurnerController implements Runnable{

    private volatile IGPIOPin fanPin;
    private volatile IGPIOPin dispenserPin;
    private volatile DoubleMeasure coalBurnerWaterTank;
    private volatile ConfigService configService;
    private volatile LogService logService;

    private volatile RunningState currentState = RunningState.STANDBY;

    private Thread thread;

    private static final String CONFIG_NAMESPACE = "coalburner";
    private static final String CONFIG_GOTO_ACTIVE_TEMP= "goto.active.temperature";
    private static final String CONFIG_GOTO_STANDBY_TEMP = "goto.standby.temperature";


    private enum TurnedState{
        OFF,
        ON
    }

    private enum RunningState{
        ACTIVE,
        STANDBY;
        public RunningState toggle(){
            if(this.equals(ACTIVE))
                return STANDBY;
            return ACTIVE;
        }
    }

    public void start(){
        logService.log(LogService.LOG_INFO, "Starting coal burner working thread");
        synchronized (this){
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop(){
        logService.log(LogService.LOG_INFO, "Stopping coal burner working thread");
        synchronized (this) {
            thread.interrupt();
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                logService.log(LogService.LOG_ERROR, "Stop method interrupted while waiting for worker thread to stop. There may be running thread leak.");
            } finally {
                thread = null;
            }
        }
    }

    @Override
    public void run() {
        try {
            final TimeUnit defaultTimeUnit = TimeUnit.SECOND;
        // CONFIG CONTEXT -> Config KEY
            final DispensingCycleDescriptor active = new DispensingCycleDescriptor(1,4,0, defaultTimeUnit);
            final DispensingCycleDescriptor standby = new DispensingCycleDescriptor(5,4,10, defaultTimeUnit);

            final int standbyPauseUnit = 1;
            final int standbyMaxPause = 60;


            int timeFromLastActive = 0;



            try {
                while (!Thread.interrupted()) {
                    if (RunningState.ACTIVE.equals(currentState)) {
                        executeCycle(active);
                        timeFromLastActive = 0;
                    } else {
                        fanPin.setState(HiLoPinState.LOW);
                        timeFromLastActive += standbyPauseUnit;
                        if(timeFromLastActive > standbyMaxPause){
                            executeCycle(standby);
                            timeFromLastActive = 0;
                        }
                        Thread.sleep((long)standbyPauseUnit * defaultTimeUnit.getMilisecondMultiplier());
                    }
                    // ACTIVE ...
                    // START FAN
                    // WAIT FOR X1 SECONDS
                    // START DISPENSER
                    // WAIT FOR X2 SECONDS
                    // STOP DISPENSER

                    // CHECK TEMP
                    // LOWER THAN T1 -> ACTIVE
                    // ABOVE EQUAL T1 -> STANDBY

                    // STANDBY ...
                    updateStatus();
                }
            } catch (InterruptedException e) {
                logService.log(LogService.LOG_WARNING, "forcibly interrupted", e);
            }
        }finally {
            fanPin.setState(HiLoPinState.LOW);
            dispenserPin.setState(HiLoPinState.LOW);
        }
    }

    private void updateStatus(){
        double currentTemp = coalBurnerWaterTank.getValue();
        double value;
        switch (currentState){
                case STANDBY:
                    value = Double.parseDouble(configService.getConfigValueByNamespaceAndKey(CONFIG_NAMESPACE, CONFIG_GOTO_ACTIVE_TEMP));
                    if (value > currentTemp)
                        currentState = currentState.toggle();
                break;
                case ACTIVE:
                    value = Double.parseDouble(configService.getConfigValueByNamespaceAndKey(CONFIG_NAMESPACE, CONFIG_GOTO_STANDBY_TEMP));
                    if (value < currentTemp)
                        currentState = currentState.toggle();
                break;
                default:
                    throw new UnsupportedOperationException("Unknown current state");
        }
    }

    private void executeCycle(DispensingCycleDescriptor dispensingCycleDescriptor) throws InterruptedException{
        fanPin.setState(HiLoPinState.HIGH);
        Thread.sleep(dispensingCycleDescriptor.getFanHeadStartInMilis());
        dispenserPin.setState(HiLoPinState.HIGH);
        Thread.sleep(dispensingCycleDescriptor.getDispenserRunTimeInMilis());
        dispenserPin.setState(HiLoPinState.LOW);
        Thread.sleep(dispensingCycleDescriptor.getFanDelayedStopInMilis());
    }

    private enum TimeUnit{
        MILISECOND(1),
        SECOND(1000),
        MINUTE(60*1000),
        HOUR(60*60*1000);

        private final int milisecondMultiplier;
        private TimeUnit(int milisecondMultiplier){
            this.milisecondMultiplier = milisecondMultiplier;
        }

        public int getMilisecondMultiplier(){
            return milisecondMultiplier;
        }
    }

    private static class DispensingCycleDescriptor{
        private final int fanHeadStart;
        private final int dispenserRunTime;
        private final int fanDelayedStop;

        public DispensingCycleDescriptor(int fanHeadStart, int dispenserRunTime, int fanDelayedStop, TimeUnit timeUnit) {
            this.fanHeadStart = fanHeadStart * timeUnit.getMilisecondMultiplier();
            this.dispenserRunTime = dispenserRunTime * timeUnit.getMilisecondMultiplier();
            this.fanDelayedStop = fanDelayedStop * timeUnit.getMilisecondMultiplier();
        }

        public int getFanHeadStartInMilis() {
            return fanHeadStart;
        }

        public int getDispenserRunTimeInMilis() {
            return dispenserRunTime;
        }

        public int getFanDelayedStopInMilis() {
            return fanDelayedStop;
        }
    }


}
