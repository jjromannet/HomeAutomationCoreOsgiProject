package net.jjroman.homeautomation.osgi.pinconsumer.pi4j;

import net.jjroman.homeautomation.osgi.configservice.api.ConfigService;
import net.jjroman.homeautomation.osgi.measureservice.api.DoubleMeasure;
import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;
import org.osgi.service.log.LogService;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Main Logic for controlling Coal burner is defined here.
 *
 * Coal burner have two engines
 * 1) Fan - which blows air into coal burning chamber
 * 2) Dispenser - which dispense
 * TODO: shutdown hooks: http://stackoverflow.com/questions/2975248/java-how-to-handle-a-sigterm
 *       http://stackoverflow.com/questions/4911745/if-i-type-ctrl-c-on-the-command-line-will-the-finally-block-in-java-still-execu
 *
 *       http://programmers.stackexchange.com/questions/185953/junit-testing-in-multithread-application
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
    // TODO create config enum and enum set of required configurations
    public static final String CONFIG_GOTO_ACTIVE_TEMP= "goto.active.temperature";
    public static final String CONFIG_GOTO_STANDBY_TEMP = "goto.standby.temperature";


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
            final TimeUnit defaultTimeUnit = TimeUnit.SECONDS;
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
                        defaultTimeUnit.sleep((long)standbyPauseUnit);
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
        logService.log(LogService.LOG_DEBUG, String.format("Curent coal burner status %s ", currentState));
        logService.log(LogService.LOG_DEBUG, String.format("Curent water tank temperature %f ", currentTemp));
        double value;
        switch (currentState){
                case STANDBY:
                    value = Double.parseDouble(configService.getConfigValueByNamespaceAndKey(CONFIG_NAMESPACE, CONFIG_GOTO_ACTIVE_TEMP));
                    logService.log(LogService.LOG_DEBUG, String.format("Temperature to go active: %f ", value));
                    if (value > currentTemp) {
                        currentState = currentState.toggle();
                        logService.log(LogService.LOG_DEBUG, String.format("Current state updated: %s ", currentState));
                    }
                break;
                case ACTIVE:
                    value = Double.parseDouble(configService.getConfigValueByNamespaceAndKey(CONFIG_NAMESPACE, CONFIG_GOTO_STANDBY_TEMP));
                    logService.log(LogService.LOG_DEBUG, String.format("Temperature to go standby: %f ", value));
                    if (value < currentTemp) {
                        currentState = currentState.toggle();
                        logService.log(LogService.LOG_DEBUG, String.format("Current state updated: %s ", currentState));
                    }
                break;
                default:
                    throw new UnsupportedOperationException("Unknown current state");
        }
    }

    private void executeCycle(DispensingCycleDescriptor dispensingCycleDescriptor) throws InterruptedException{
        fanPin.setState(HiLoPinState.HIGH);
        dispensingCycleDescriptor.sleepFanHeadStart();
        dispenserPin.setState(HiLoPinState.HIGH);
        dispensingCycleDescriptor.sleepDispenserRunTime();
        dispenserPin.setState(HiLoPinState.LOW);
        dispensingCycleDescriptor.sleepFanDelayedStop();
    }

    private static class DispensingCycleDescriptor{
        private final long fanHeadStart;
        private final long dispenserRunTime;
        private final long fanDelayedStop;
        private final TimeUnit timeUnit;

        public DispensingCycleDescriptor(long fanHeadStart, long dispenserRunTime, long fanDelayedStop, TimeUnit timeUnit) {
            this.fanHeadStart = fanHeadStart;
            this.dispenserRunTime = dispenserRunTime;
            this.fanDelayedStop = fanDelayedStop;
            this.timeUnit = timeUnit;
        }

        public void sleepFanHeadStart() throws InterruptedException{
            timeUnit.sleep(fanHeadStart);
        }

        public void sleepDispenserRunTime() throws InterruptedException{
            timeUnit.sleep(dispenserRunTime);
        }

        public void sleepFanDelayedStop() throws InterruptedException{
            timeUnit.sleep(fanDelayedStop);
        }
    }


}
