package net.jjroman.homeautomation.osgi.pinservice.api;

/**
 * Enum that reflects available GPIO pins
 * Created by Jan on 08/03/2015.
 */
public enum AvailableGPIO {
    PIN_00(0),
    PIN_01(1),
    PIN_02(2),
    PIN_03(3);

    private final int number;

    private AvailableGPIO(int number){
        this.number = number;
    }

    public int getPinNumber(){
        return number;
    }
}
