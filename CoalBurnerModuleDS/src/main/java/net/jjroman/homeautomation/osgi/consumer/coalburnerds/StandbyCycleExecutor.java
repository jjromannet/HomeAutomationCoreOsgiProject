package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jan on 06/04/2015.
 */
public class StandbyCycleExecutor extends AbstractCycleExecutor {
    public StandbyCycleExecutor(IGPIOPin fanPin, IGPIOPin dispenserPin) {
        super(fanPin, dispenserPin);
    }

    @Override
    public void executeCycle(EnvironmentImmutableSnapshot environmentSnapshot) throws InterruptedException {
        try{
            fanPin.setState(HiLoPinState.LOW);
            TimeUnit.SECONDS.wait(environmentSnapshot.getActiveFanHeadStart());
            dispenserPin.setState(HiLoPinState.HIGH);
            TimeUnit.SECONDS.wait(environmentSnapshot.getActiveDispenserRunTime());
            dispenserPin.setState(HiLoPinState.LOW);
            TimeUnit.SECONDS.wait(environmentSnapshot.getActiveFanAfterDispensedTime());
        }finally {
            fanPin.setState(HiLoPinState.LOW);
            dispenserPin.setState(HiLoPinState.LOW);
        }
    }
}
