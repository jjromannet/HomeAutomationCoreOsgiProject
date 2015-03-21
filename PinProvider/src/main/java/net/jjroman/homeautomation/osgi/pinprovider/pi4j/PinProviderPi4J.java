package net.jjroman.homeautomation.osgi.pinprovider.pi4j;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

/**
 * Created by Jan on 27/02/2015.
 */
public class PinProviderPi4J implements IGPIOPin{

    private final GpioPinDigitalOutput pin;
    private final GpioController gpioController;

    public PinProviderPi4J(GpioController gpioController, HiLoPinState initialPinState, Pin pin, String name) {
        this.pin = gpioController.provisionDigitalOutputPin(pin, name, translate(initialPinState));
        this.gpioController = gpioController;
    }

    public HiLoPinState getState() {
        return translate(pin.getState());
    }

    public void setState(HiLoPinState value) {
        pin.setState(translate(value));
    }

    public void dispose(){
        setState(HiLoPinState.LOW);
        gpioController.unprovisionPin(pin);
    }

    private HiLoPinState translate(PinState pinState){
        switch (pinState){
            case HIGH:
                return HiLoPinState.HIGH;
            case LOW:
            default:
                return HiLoPinState.LOW;
        }
        //throw new Exception("Unknown PinState");
    }

    private PinState translate(HiLoPinState hiLoPinState){
        switch (hiLoPinState){
            case HIGH:
                return PinState.HIGH;
            case LOW:
            default:
                return PinState.LOW;
        }
        //throw new Exception("Unknown HiLoPinState");
    }

    GpioPinDigitalOutput getPin(){
        return pin;
    }
}
