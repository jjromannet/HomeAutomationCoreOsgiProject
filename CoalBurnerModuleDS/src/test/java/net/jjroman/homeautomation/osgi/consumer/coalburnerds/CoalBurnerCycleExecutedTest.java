package net.jjroman.homeautomation.osgi.consumer.coalburnerds;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * If Active - active cycle executed and standby not executed   - active = yes, standby = no
 * If Standby - threshold -> below                              - active = no, standby = no
 * If Standby - threshold -> above                              - active = no, standby = yes
 * Created by Jan on 06/04/2015.
 */
public class CoalBurnerCycleExecutedTest {

    CoalBurnerControlLogic coalBurnerControlLogic = new CoalBurnerControlLogic();

    @Test
    public void testActiveCycleExecutedWhenActive() throws InterruptedException {

        MockCycleExecutor activeCycleExecutor = new MockCycleExecutor();
        MockCycleExecutor standbyCycleExecutor = new MockCycleExecutor();
        coalBurnerControlLogic.executeCycle(CoalBurnerState.ACTIVE,0,null, standbyCycleExecutor, activeCycleExecutor);
        assertEquals("Active cycle not executed", true, activeCycleExecutor.isCycleExecuted());
        assertEquals("Standby cycle executed",    false,  standbyCycleExecutor.isCycleExecuted());

        assertEquals("Active cycle turned-off",    false,  activeCycleExecutor.isCycleTurnedOff());
        assertEquals("Standby cycle turned-off",    true,  standbyCycleExecutor.isCycleTurnedOff());


    }

    @Test
    public void testActiveCycleNotExecutedStandbyCycleNotExecutedWhenStandbyAndBelow() throws InterruptedException {
        long counter = Math.round(Long.MAX_VALUE * Math.random());
        long currentCounter = counter - Math.round(counter * Math.random());

        EnvironmentImmutableSnapshot environmentImmutableSnapshot = new EnvironmentImmutableSnapshotBasic(counter, 0,0,0,0,0,0,0,0,0);

        MockCycleExecutor activeCycleExecutor = new MockCycleExecutor();
        MockCycleExecutor standbyCycleExecutor = new MockCycleExecutor();
        coalBurnerControlLogic.executeCycle(CoalBurnerState.STANDBY, currentCounter, environmentImmutableSnapshot, standbyCycleExecutor, activeCycleExecutor);
        assertEquals("Active cycle not executed", false, activeCycleExecutor.isCycleExecuted());
        assertEquals("Standby cycle executed", false, standbyCycleExecutor.isCycleExecuted());

        assertEquals("Active cycle turned-off",    true,  activeCycleExecutor.isCycleTurnedOff());
        assertEquals("Standby cycle turned-off",    false,  standbyCycleExecutor.isCycleTurnedOff());
    }

    @Test
    public void testInStandbyOnlyStandbyCycleIsExecutedWhenCounterAbove() throws InterruptedException {
        long counter = Math.round(Integer.MAX_VALUE * Math.random());
        long currentCounter = counter + Math.round(Integer.MAX_VALUE * Math.random());

        EnvironmentImmutableSnapshot environmentImmutableSnapshot = new EnvironmentImmutableSnapshotBasic(counter, 0,0,0,0,0,0,0,0,0);

        MockCycleExecutor activeCycleExecutor = new MockCycleExecutor();
        MockCycleExecutor standbyCycleExecutor = new MockCycleExecutor();
        coalBurnerControlLogic.executeCycle(CoalBurnerState.STANDBY, currentCounter, environmentImmutableSnapshot, standbyCycleExecutor, activeCycleExecutor);
        assertEquals("Active cycle not executed", false, activeCycleExecutor.isCycleExecuted());
        assertEquals("Standby cycle executed", true, standbyCycleExecutor.isCycleExecuted());

        assertEquals("Active cycle turned-off",    true,  activeCycleExecutor.isCycleTurnedOff());
        assertEquals("Standby cycle turned-off",    false,  standbyCycleExecutor.isCycleTurnedOff());
    }



}
