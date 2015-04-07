package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

/**
 * Created by Jan on 06/04/2015.
 */
public class StandbyCycleExecutor extends AbstractCycleExecutor {
    public StandbyCycleExecutor(IGPIOPin fanPin, IGPIOPin dispenserPin) {
        super(fanPin, dispenserPin);
    }

    @Override
    public void executeCycle(EnvironmentImmutableSnapshot environmentSnapshot) {
        throw new UnsupportedOperationException();
    }
}
