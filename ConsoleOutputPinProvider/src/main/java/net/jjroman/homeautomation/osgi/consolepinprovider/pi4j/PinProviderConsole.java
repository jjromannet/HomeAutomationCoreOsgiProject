package net.jjroman.homeautomation.osgi.consolepinprovider.pi4j;

import net.jjroman.homeautomation.osgi.pinservice.api.AvailableGPIO;
import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

/**
 * PinProvider that logs pin state changes in the console output
 * Created by Jan on 09/03/2015.
 */
public class PinProviderConsole implements IGPIOPin{
    private final AvailableGPIO availableGPIO;
    private HiLoPinState state;
    public PinProviderConsole(AvailableGPIO availableGPIO) {
        this.availableGPIO = availableGPIO;
        state = HiLoPinState.LOW;
    }

    @Override
    public HiLoPinState getState() {
        return state;
    }

    @Override
    public void setState(HiLoPinState value) {

        System.out.print(String.format("Pin %d set to: %s (was: %s)%n", availableGPIO.getPinNumber(), value.toString(), getState().toString()));
        state = value;
    }
}
