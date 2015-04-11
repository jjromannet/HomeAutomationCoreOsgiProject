package net.jjroman.homeautomation.osgi.coalburner;

import org.omg.PortableInterceptor.ACTIVE;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jan on 11/04/2015.
 */
public class Manager {

    private volatile State currentState = State.OFF;

    // EVENT GENERATOR - OBSERVE CONFIGS and MEASURES ...
    public void eventGenerator(){
        //getConfigSnapshot();

        double idleMinimum = 55;
        double activeMaximum = 65;
        boolean turnedOn = true;

        // get Measure
        double measuredTemp = 70;

        // THERE HAVE TO BE ONLY ONE EVENT GENERATED AT ONCE
        // CASE 1
        if(turnedOn){
            currentState = Event.TURNED_ON.dispatch(currentState);
        }else{
            currentState = Event.TURNED_OFF.dispatch(currentState);
        }

        // CASE 2


        if(measuredTemp > activeMaximum){
            currentState = Event.TEMPERATURE_RAISED_ABOVE_MAXIMUM.dispatch(currentState);
        }

        if(measuredTemp < idleMinimum){
            currentState = Event.TEMPERATURE_DROPPED_BELOW_MINIMUM.dispatch(currentState);
        }

    }


    private void off() throws InterruptedException{
        while (State.OFF.equals(currentState)){
            TimeUnit.SECONDS.wait(1);
        }
    }
    private void active(long fanHeadStart, long dispenserRunTime, long fanAfterDispensed, TimeUnit timeUnit)
        throws InterruptedException
    {
        fanStart();
        timeUnit.wait(fanHeadStart);
        while (State.ACTIVE.equals(currentState)) {
            dispenserStart();
            timeUnit.wait(dispenserRunTime);
            dispenserStop();
            timeUnit.wait(fanAfterDispensed);
        }
        fanStop();
    }

    private void idle(long maximumInactive, long dispenserRunTime, long fanAfterDispensed, TimeUnit timeUnit)
        throws InterruptedException
    {
        int timeout = 0;
        while(State.IDLE.equals(currentState)) {
            if(timeout++ < maximumInactive ) {
                timeUnit.wait(1);
            }else{
                fanStart();

                dispenserStart();
                timeUnit.wait(dispenserRunTime);
                dispenserStop();
                timeUnit.wait(fanAfterDispensed);

                fanStop();
                timeout = 0;
            }
        }
    }
    // WORKING THREAD
    public void workingThread() throws InterruptedException {
        long activeFanHeadStart = 0;
        long activeDispenserRunTime = 5;
        long activeFanAfterDispensed = 25;

        long idleMaximumInactive = 120;
        long idleDispenserRunTime = 10;
        long idleFanAfterDispensed = 60;

        TimeUnit timeUnit = TimeUnit.SECONDS;
        try {
            switch (currentState){
                case OFF:
                    off();
                break;
                case ACTIVE:
                    active(activeFanHeadStart, activeDispenserRunTime, activeFanAfterDispensed, timeUnit);
                break;
                case IDLE:
                    idle(idleMaximumInactive, idleDispenserRunTime, idleFanAfterDispensed, timeUnit);
                break;
            }
        }finally{
            fanStop();
            dispenserStop();
        }


    }

    private void dispenserStop() {
    }

    private void dispenserStart() {

    }

    private void fanStart() {
    }

    private void fanStop() {
    }
}
