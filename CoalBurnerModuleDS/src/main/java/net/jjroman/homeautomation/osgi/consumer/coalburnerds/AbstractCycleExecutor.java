package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

/**
 * Created by Jan on 07/04/2015.
 */
public abstract class AbstractCycleExecutor implements CycleExecutor{

    private final IGPIOPin fanPin;
    private final IGPIOPin dispenserPin;

    public AbstractCycleExecutor(IGPIOPin fanPin, IGPIOPin dispenserPin) {
        this.fanPin = fanPin;
        this.dispenserPin = dispenserPin;
    }
}
