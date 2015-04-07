package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

/**
 * Class performing active cycle on GPIO pins
 * Created by Jan on 06/04/2015.
 */
public class ActiveCycleExecutor extends AbstractCycleExecutor{

    public ActiveCycleExecutor(IGPIOPin fanPin, IGPIOPin dispenserPin) {
        super(fanPin, dispenserPin);
    }

    @Override
        public void executeCycle(EnvironmentImmutableSnapshot environmentSnapshot) {
            throw new UnsupportedOperationException();
        }
}
