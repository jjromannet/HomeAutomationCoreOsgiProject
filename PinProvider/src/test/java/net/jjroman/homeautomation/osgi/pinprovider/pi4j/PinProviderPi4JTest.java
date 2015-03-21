package net.jjroman.homeautomation.osgi.pinprovider.pi4j;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import com.pi4j.io.gpio.test.MockGpioFactory;
import com.pi4j.io.gpio.test.MockPin;
import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jan on 20/03/2015.
 */

public class PinProviderPi4JTest {


    private PinProviderPi4J getPinProvider(HiLoPinState state){
        GpioController controller = MockGpioFactory.getInstance();
        return new PinProviderPi4J(controller, state, MockPin.DIGITAL_OUTPUT_PIN, "");

    }


    @Test
    public void initialStateCorrectlySetToLow() {
        PinProviderPi4J pinProviderPi4J = getPinProvider(HiLoPinState.LOW);
        assertTrue(pinProviderPi4J.getPin().isLow());
    }

    @Test
    public void initialStateCorrectlySetToHigh() {
        PinProviderPi4J pinProviderPi4J = getPinProvider(HiLoPinState.HIGH);
        assertTrue(pinProviderPi4J.getPin().isHigh());
    }

    @Test(expected = GpioPinExistsException.class)
    public void failDoubleInitialisationOfSamePin() {
        GpioController controller = MockGpioFactory.getInstance();

        PinProviderPi4J pinProviderPi4J = new PinProviderPi4J(controller, HiLoPinState.HIGH, MockPin.DIGITAL_OUTPUT_PIN, "");
        pinProviderPi4J = new PinProviderPi4J(controller, HiLoPinState.HIGH, MockPin.DIGITAL_OUTPUT_PIN, "");

    }

    @Test
    public void setPinToHigh() {
        PinProviderPi4J pinProviderPi4J = getPinProvider(HiLoPinState.LOW);
        pinProviderPi4J.setState(HiLoPinState.HIGH);
        assertTrue(pinProviderPi4J.getPin().isHigh());
    }

    @Test
    public void setPinToLow() {
        PinProviderPi4J pinProviderPi4J = getPinProvider(HiLoPinState.HIGH);
        pinProviderPi4J.setState(HiLoPinState.LOW);
        assertTrue(pinProviderPi4J.getPin().isLow());

    }

    @Test
    public void pinProvisionedUponInstantiate() {
        GpioController controller = MockGpioFactory.getInstance();
        PinProviderPi4J pinProviderPi4J = new PinProviderPi4J(controller, HiLoPinState.HIGH, MockPin.DIGITAL_OUTPUT_PIN, "");
        assertTrue(controller.getProvisionedPins().contains(pinProviderPi4J.getPin()));
    }

    @Test
    public void pinUnprovisionedUponDisposal() {
        GpioController controller = MockGpioFactory.getInstance();
        PinProviderPi4J pinProviderPi4J = new PinProviderPi4J(controller, HiLoPinState.LOW, MockPin.DIGITAL_OUTPUT_PIN, "");
        pinProviderPi4J.dispose();
        assertFalse(controller.getProvisionedPins().contains(pinProviderPi4J.getPin()));
    }


    @Test
    public void pinSetToLowUponDisposal() {
        PinProviderPi4J pinProviderPi4J = getPinProvider(HiLoPinState.HIGH);
        pinProviderPi4J.dispose();
        assertTrue(pinProviderPi4J.getPin().isLow());
    }

}
