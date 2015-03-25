package net.jjroman.homeautomation.osgi.pinprovider.pi4j;

import com.pi4j.io.gpio.Pin;
import net.jjroman.homeautomation.osgi.pinservice.api.AvailableGPIO;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Testing pin mapping for Raspberry pi Hardware accessed via pi4j
 * Created by Jan on 25/03/2015.
 */
public class RaspiPinTranslatorTest {

    @Test
    public void pinMappedToPinsOfSameNumbers(){
        PinTranslator pinTranslator = new RaspiPinTranslator();
        for(AvailableGPIO availableGPIO : AvailableGPIO.values()){
            Pin pin = pinTranslator.translate(availableGPIO);
            assertEquals("pin maped to pin of same address", availableGPIO.getPinNumber(), pin.getAddress());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void exceptionThrownForNull(){
        PinTranslator pinTranslator = new RaspiPinTranslator();
        pinTranslator.translate(null);
    }

}
