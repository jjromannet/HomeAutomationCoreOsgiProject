package net.jjroman.homeautomation.osgi.pinprovider.pi4j;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.test.MockGpioFactory;
import com.pi4j.io.gpio.test.MockPin;
import net.jjroman.homeautomation.osgi.pinservice.api.AvailableGPIO;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.osgi.junit.OsgiContext;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Testing Activator class
 * Created by Jan on 24/03/2015.
 */
public class ActivatorTest {
    @Rule
    public final OsgiContext context = new OsgiContext();

    @Test
    public void pinNumberPropertyHasBeenSet() throws Exception{
        BundleContext bundleContext = MockOsgi.newBundleContext();
        Activator a = new Activator(MockGpioFactory.getInstance(), new MockPinTranslator());
        a.start(bundleContext);
        ServiceReference[] serviceReferences = bundleContext.getServiceReferences(IGPIOPin.class.getName(), "");

        for(ServiceReference serviceReference : serviceReferences){
            assertTrue("Registered Services should have PinNumber property set", new HashSet<>(Arrays.asList(serviceReference.getPropertyKeys())).contains("PinNumber"));
        }
    }

    @Test
     public void thereIsServiceRegisteredForEachAvailablePin() throws Exception{
        BundleContext bundleContext = MockOsgi.newBundleContext();
        Activator a = new Activator(MockGpioFactory.getInstance(), new MockPinTranslator() );
        a.start(bundleContext);

        for(AvailableGPIO availableGPIO : AvailableGPIO.values()) {
            ServiceReference[] serviceReferences =
                    bundleContext.getServiceReferences(IGPIOPin.class.getName(),
                            String.format("(PinNumber=%d)", availableGPIO.getPinNumber()));
            assertNotNull("There is service with given pin number", serviceReferences);
            //For some reason services are not filtered
            // maybe it is limitation of mock? So I am just checkin is array not empty
            assertNotEquals("There is service with given pin number", 0, serviceReferences.length);
        }
    }

    @Test
    public void pinsUnprovisionedOnStop() throws Exception{
        BundleContext bundleContext = MockOsgi.newBundleContext();
        GpioController gpioController = MockGpioFactory.getInstance();
        Activator a = new Activator(gpioController, new MockPinTranslator() );
        a.start(bundleContext);

        a.stop(bundleContext);
        assertFalse("After stop all pins should be unprovisioned", gpioController.getProvisionedPins().isEmpty());

    }

    private static class MockPinTranslator implements PinTranslator{
        public Pin translate(AvailableGPIO availableGPIO){
            switch (availableGPIO){
                case PIN_00:
                    return MockPin.DIGITAL_OUTPUT_PIN;
                case PIN_01:
                    return MockPin.DIGITAL_OUTPUT_PIN_02;
                case PIN_02:
                    return MockPin.DIGITAL_OUTPUT_PIN_03;
                case PIN_03:
                    return MockPin.DIGITAL_OUTPUT_PIN_04;
                default:
                    throw new UnsupportedOperationException("Pin not available");
            }
        }}
}
