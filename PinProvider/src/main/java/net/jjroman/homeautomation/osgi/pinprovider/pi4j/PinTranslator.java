package net.jjroman.homeautomation.osgi.pinprovider.pi4j;

import com.pi4j.io.gpio.Pin;
import net.jjroman.homeautomation.osgi.pinservice.api.AvailableGPIO;

/**
 * Created by Jan on 24/03/2015.
 */
public interface PinTranslator {
    Pin translate(AvailableGPIO availableGPIO);
}
