package net.jjroman.homeautomation.osgi.pinservice.api;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Jan on 25/03/2015.
 */
public class AvailableGPIOTest {
    @Test
    public void pinNumbersAssignedCorrectly(){
        assertEquals(AvailableGPIO.PIN_00.getPinNumber(), 0);
        assertEquals(AvailableGPIO.PIN_01.getPinNumber(), 1);
        assertEquals(AvailableGPIO.PIN_02.getPinNumber(), 2);
        assertEquals(AvailableGPIO.PIN_03.getPinNumber(), 3);
    }
}
