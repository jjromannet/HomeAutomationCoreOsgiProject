package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

/**
 * Created by Jan on 07/04/2015.
 */
public abstract class AbstractCycleExecutor implements CycleExecutor{

    protected final IGPIOPin fanPin;
    protected final IGPIOPin dispenserPin;

    public AbstractCycleExecutor(IGPIOPin fanPin, IGPIOPin dispenserPin) {
        this.fanPin = fanPin;
        this.dispenserPin = dispenserPin;
    }

    @Override
    public void turnOff(){
        fanPin.setState(HiLoPinState.LOW);
        dispenserPin.setState(HiLoPinState.LOW);
    }
}
