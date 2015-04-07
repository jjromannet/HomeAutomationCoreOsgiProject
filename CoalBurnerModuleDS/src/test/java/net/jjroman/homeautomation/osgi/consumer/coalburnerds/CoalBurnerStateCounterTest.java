package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testin standby counter
 * If ACTIVE then reset counter
 * If STANDBY increment counter
 * If STANDBY & Counter reached the max - reset counter
 * Created by Jan on 06/04/2015.
 */
public class CoalBurnerStateCounterTest {

    CycleExecutor cycleExecutor = new MockCycleExecutor();

    CoalBurnerControlLogic coalBurnerControlLogic = new CoalBurnerControlLogic();

    @Test
    public void testResetCounterWhenActiveState(){
        long counter = Math.round(Long.MAX_VALUE * Math.random());
        long expected = 0;
        assertEquals("Counter should be set to 0 when state is ACTIVE",
                expected,
                coalBurnerControlLogic.executeCycle(CoalBurnerState.ACTIVE, counter, null,cycleExecutor,cycleExecutor));

    }

    @Test
    public void testCounterShouldBeIncrementedWhenStandbyAndLessThenThreshold(){
        long maxThreshold = Math.round(Integer.MAX_VALUE * Math.random());
        long counter = Math.round(Integer.MAX_VALUE * Math.random()) % (maxThreshold - 1);
        long expected = counter + 1;

        EnvironmentImmutableSnapshot environmentImmutableSnapshot = new EnvironmentImmutableSnapshotBasic(maxThreshold, 0,0,0,0,0,0,0,0,0);

        assertEquals(String.format("Counter (%d) should be incremented (%d) when state is STANDBY and max threshold (%d)", counter, expected, maxThreshold),
                expected,
                coalBurnerControlLogic.executeCycle(CoalBurnerState.STANDBY, counter, environmentImmutableSnapshot, cycleExecutor,cycleExecutor));

    }

    @Test
    public void testCounterShouldBeResetWhenStandbyAndMoreThenThreshold(){
        long maxThreshold = Math.round(Integer.MAX_VALUE * Math.random());
        long counter = Math.round(Integer.MAX_VALUE * Math.random()) + maxThreshold;
        long expected = 0;

        EnvironmentImmutableSnapshot environmentImmutableSnapshot = new EnvironmentImmutableSnapshotBasic(maxThreshold, 0,0,0,0,0,0,0,0,0);

        assertEquals(String.format("Counter (%d) should be incremented (%d) when state is STANDBY",counter, expected),
                expected,
                coalBurnerControlLogic.executeCycle(CoalBurnerState.ACTIVE, counter, environmentImmutableSnapshot, cycleExecutor,cycleExecutor));

    }
}
