package net.jjroman.homeautomation.osgi.coalburner.new2;

import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

/**
 * Created by Jan on 25/04/2015.
 */
public class CoalBurnerExternalsRPIGpio implements CoalBurnerExternals {

    private final IGPIOPin fanPin;
    private final IGPIOPin dispenserPin;

    public CoalBurnerExternalsRPIGpio(IGPIOPin fanPin, IGPIOPin dispenserPin) {
        this.fanPin = fanPin;
        this.dispenserPin = dispenserPin;
    }

    @Override
    public void fanStart() {
        fanPin.setState(HiLoPinState.HIGH);
    }

    @Override
    public void fanStop() {
        fanPin.setState(HiLoPinState.LOW);
    }

    @Override
    public void dispenserStart() {
        dispenserPin.setState(HiLoPinState.HIGH);
    }

    @Override
    public void dispenserStop() {
        dispenserPin.setState(HiLoPinState.LOW);
    }
}
