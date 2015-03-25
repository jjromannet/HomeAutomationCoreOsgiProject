package net.jjroman.homeautomation.osgi.pinprovider.pi4j;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import net.jjroman.homeautomation.osgi.pinservice.api.AvailableGPIO;

/**
 * Implementation of translator for hardware Rasberry pins using pi4j
 * Created by Jan on 25/03/2015.
 */
class RaspiPinTranslator implements PinTranslator {

    @Override
    public Pin translate(AvailableGPIO availableGPIO) {
        if(availableGPIO == null){
            throw new UnsupportedOperationException("Pin not available");
        }
        switch (availableGPIO) {
            case PIN_00:
                return RaspiPin.GPIO_00;
            case PIN_01:
                return RaspiPin.GPIO_01;
            case PIN_02:
                return RaspiPin.GPIO_02;
            case PIN_03:
                return RaspiPin.GPIO_03;
            default:
                throw new UnsupportedOperationException("Pin not available");
        }
    }
}