package net.jjroman.homeautomation.osgi.consolepinprovider.pi4j;

import net.jjroman.homeautomation.osgi.pinservice.api.AvailableGPIO;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Console Output Pin Provider activator class
 * Created by Jan on 09/03/2015.
 */
public class Activator implements BundleActivator {


    private static final String PROPERTY_NAME_PIN_NUMBER = "PinNumber";

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        for(AvailableGPIO availableGPIO : AvailableGPIO.values()){
            Dictionary<String, String> props = new Hashtable<String, String>();
            props.put(PROPERTY_NAME_PIN_NUMBER, String.valueOf(availableGPIO.getPinNumber()));
            bundleContext.registerService(
                    IGPIOPin.class.getName(), new PinProviderConsole(availableGPIO), props);
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        // called on bundle stop
    }
}
