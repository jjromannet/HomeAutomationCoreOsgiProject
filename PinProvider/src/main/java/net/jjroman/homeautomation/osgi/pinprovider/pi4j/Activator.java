package net.jjroman.homeautomation.osgi.pinprovider.pi4j;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import net.jjroman.homeautomation.osgi.pinservice.api.AvailableGPIO;
import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Activator for PinProvider service using Pi4J. FOr use in target environment.
 * Created by Jan on 27/02/2015.
 */
public class Activator implements BundleActivator {

    private List<PinProviderPi4J> pinProviders;

    private static final String PROPERTY_NAME_PIN_NUMBER = "PinNumber";

    private final GpioController gpioController;
    private final PinTranslator pinTranslator;

    public Activator() {
        this(GpioFactory.getInstance(), new RaspiPinTranslator());
    }

    public Activator(GpioController gpioController, PinTranslator pinTranslator) {
        super();
        this.gpioController = gpioController;
        this.pinTranslator = pinTranslator;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        pinProviders = new ArrayList<>();

        for (AvailableGPIO availableGPIO : AvailableGPIO.values()) {
            PinProviderPi4J pinProviderPi4J = new PinProviderPi4J(gpioController, HiLoPinState.LOW, pinTranslator.translate(availableGPIO), availableGPIO.name());
            Dictionary<String, String> props = new Hashtable<>();
            props.put(PROPERTY_NAME_PIN_NUMBER, String.valueOf(availableGPIO.getPinNumber()));
            bundleContext.registerService(
                    IGPIOPin.class.getName(), pinProviderPi4J, props);
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        for (PinProviderPi4J pinProviderPi4J : pinProviders) {
            pinProviderPi4J.dispose();
        }
        pinProviders = null;
    }
}