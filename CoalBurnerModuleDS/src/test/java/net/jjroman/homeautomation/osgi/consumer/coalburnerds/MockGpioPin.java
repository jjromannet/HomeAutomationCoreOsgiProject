package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jan on 08/04/2015.
 */
public class MockGpioPin implements IGPIOPin {

    HiLoPinState mystate = HiLoPinState.LOW;

    Map<Long, HiLoPinState> stateChangeLog = new LinkedHashMap<>();

    @Override
    public HiLoPinState getState() {
        return mystate;
    }

    @Override
    public void setState(HiLoPinState value) {
        this.mystate = value;
        stateChangeLog.put(System.currentTimeMillis(), value);
    }

    Map.Entry<Long, HiLoPinState> getStateChangeLogEntry(int index){
        return null;
        //stateChangeLog.entrySet().iterator().
    }
}
