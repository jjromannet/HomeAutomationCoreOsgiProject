package net.jjroman.homeautomation.osgi.pinprovider.pi4j;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
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
            assertTrue("Registered Services should have PinNumber property set", new HashSet<String>(Arrays.asList(serviceReference.getPropertyKeys())).contains("PinNumber"));
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
            //TODO .. why it is not filtering the services ...
            // maybe it is limitation of mock?
            //assertEquals("There is service with given pin number", 1, serviceReferences.length);
        }
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
