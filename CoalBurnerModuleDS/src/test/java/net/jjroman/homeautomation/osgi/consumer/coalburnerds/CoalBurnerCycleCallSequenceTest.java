package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import net.jjroman.homeautomation.osgi.pinservice.api.HiLoPinState;
import net.jjroman.homeautomation.osgi.pinservice.api.IGPIOPin;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.service.blueprint.reflect.MapEntry;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Jan on 08/04/2015.
 */
public class CoalBurnerCycleCallSequenceTest {

    @Test
    public void testActiveSequence() throws InterruptedException {
        // prepare environment snapshot
        long fanHeadStart = 1;
        long fanBehind = 2;
        long dispenserRunTime = 3;
        EnvironmentImmutableSnapshot environmentImmutableSnapshot =
                new Snapshot.Builder()
                        .setActiveFanAfterDispensedTime(fanBehind)
                        .setActiveFanHeadStart(fanHeadStart)
                        .setActiveDispenserRunTime(dispenserRunTime)
                .build();

        MockGpioPin fan = new MockGpioPin();
        MockGpioPin dispenser = new MockGpioPin();
        CycleExecutor cycleExecutor = new ActiveCycleExecutor(fan, dispenser);

        cycleExecutor.executeCycle(environmentImmutableSnapshot);
        long executionFinished = System.currentTimeMillis();

        Map.Entry<Long, HiLoPinState> fanFirstStateChange = fan.getStateChangeLogEntry(0);
        Map.Entry<Long, HiLoPinState> dispenserFirstStateChange = dispenser.getStateChangeLogEntry(0);
        Map.Entry<Long, HiLoPinState> dispenserSecondStateChange = dispenser.getStateChangeLogEntry(1);
        long fanStartTime = fanFirstStateChange.getKey();
        long dispenserStartTime = dispenserFirstStateChange.getKey();

        //in millis.
        long fanHeadStartReceived = Math.round( (dispenserStartTime - fanStartTime)/1000 );
        long fanTotalRunTimeReceived = Math.round( (executionFinished - fanStartTime)/1000 );
        long dispenserRunTimeReceived = Math.round((dispenserSecondStateChange.getKey() - dispenserStartTime) / 1000);

        assertEquals("First call on fan set state to high", HiLoPinState.HIGH, fanFirstStateChange.getValue());
        assertEquals("First call on dispenser set state to high", HiLoPinState.HIGH, dispenserFirstStateChange.getValue());
        assertEquals("Second call on dispenser set state to low", HiLoPinState.LOW, dispenserSecondStateChange.getValue());

        assertTrue("Fan started before dispenser", fanStartTime < dispenserStartTime);

        assertEquals("Fan head start equals whats given", fanHeadStart, fanHeadStartReceived);
        assertEquals("Fan run time equals finish - start", fanHeadStart + fanBehind + dispenserRunTime, fanTotalRunTimeReceived);

        assertEquals("dispenser run time equals", dispenserRunTime,dispenserRunTimeReceived);
    }

}
