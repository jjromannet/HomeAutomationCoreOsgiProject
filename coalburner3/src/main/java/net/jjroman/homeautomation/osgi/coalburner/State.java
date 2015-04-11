package net.jjroman.homeautomation.osgi.coalburner;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Jan on 11/04/2015.
 */
public enum State {

    ACTIVE{
        @Override
        public State processTemperatureDropped(){
            super.stop();
            IDLE.start();
            return IDLE;
        }
        public State processTemperatureDropped(Map<State, Payload> payloads){
            super.stop(payloads.get(this));
            IDLE.start(payloads.get(IDLE));
            return IDLE;
        }

        @Override
        public State processTurnOff() {
            super.stop();
            OFF.start();
            return OFF;
        }
    },
    IDLE{
        @Override
        public State processTemperatureRaised(){
            super.stop();
            ACTIVE.start();
            return ACTIVE;
        }

        @Override
        public State processTurnOff() {
            super.stop();
            OFF.start();
            return OFF;
        }
    },
    OFF{
        @Override
        public State processTurnOn() {
            super.stop();
            IDLE.start();
            return IDLE;
        }
    };

    public State processTemperatureDropped(){return this;}
    public State processTemperatureRaised(){return this;}
    public State processTurnOff(){return this;}
    public State processTurnOn(){return this;}

    private static volatile State onlyStartedState = OFF;

    protected synchronized void start(Payload payload){
        start();
        payload.start();
    }
    protected synchronized void stop(Payload payload){
        stop();
        payload.stop();
    }
    protected synchronized void start(){
        if(onlyStartedState != null) throw new UnsupportedOperationException("Only one state can be started at once");
        onlyStartedState = this;
    }
    protected synchronized void stop(){
        if(!isRunning()) throw new UnsupportedOperationException("Cannot stop thread that is not running");
        onlyStartedState = null;
    }
    protected synchronized boolean isRunning(){
        return this.equals(onlyStartedState);
    }
}
