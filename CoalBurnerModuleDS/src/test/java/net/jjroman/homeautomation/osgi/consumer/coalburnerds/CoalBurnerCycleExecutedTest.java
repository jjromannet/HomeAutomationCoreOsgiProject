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
    public void testActiveCycleExecutedWhenActive(){

        MockCycleExecutor activeCycleExecutor = new MockCycleExecutor();
        MockCycleExecutor standbyCycleExecutor = new MockCycleExecutor();
        coalBurnerControlLogic.executeCycle(CoalBurnerState.ACTIVE,0,null, standbyCycleExecutor, activeCycleExecutor);
        assertEquals("Active cycle not executed", true, activeCycleExecutor.isCycleExecuted());
        assertEquals("Standby cycle executed",    false,  standbyCycleExecutor.isCycleExecuted());

    }

    @Test
    public void testActiveCycleNotExecutedStandbyCycleNotExecutedWhenStandbyAndBelow(){
        long counter = Math.round(Long.MAX_VALUE * Math.random());
        long currentCounter = counter - Math.round(counter * Math.random());

        EnvironmentImmutableSnapshot environmentImmutableSnapshot = new EnvironmentImmutableSnapshotBasic(counter, 0,0,0,0,0,0,0,0,0);

        MockCycleExecutor activeCycleExecutor = new MockCycleExecutor();
        MockCycleExecutor standbyCycleExecutor = new MockCycleExecutor();
        coalBurnerControlLogic.executeCycle(CoalBurnerState.STANDBY, currentCounter, environmentImmutableSnapshot, standbyCycleExecutor, activeCycleExecutor);
        assertEquals("Active cycle not executed", false, activeCycleExecutor.isCycleExecuted());
        assertEquals("Standby cycle executed",    false, standbyCycleExecutor.isCycleExecuted());

    }

    @Test
    public void testActiveCycleNotExecutedStandbyCycleExecutedWhenStandbyAndAbove(){
        long counter = Math.round(Integer.MAX_VALUE * Math.random());
        long currentCounter = counter + Math.round(Integer.MAX_VALUE * Math.random());

        EnvironmentImmutableSnapshot environmentImmutableSnapshot = new EnvironmentImmutableSnapshotBasic(counter, 0,0,0,0,0,0,0,0,0);

        MockCycleExecutor activeCycleExecutor = new MockCycleExecutor();
        MockCycleExecutor standbyCycleExecutor = new MockCycleExecutor();
        coalBurnerControlLogic.executeCycle(CoalBurnerState.STANDBY, currentCounter, environmentImmutableSnapshot, standbyCycleExecutor, activeCycleExecutor);
        assertEquals("Active cycle not executed", false, activeCycleExecutor.isCycleExecuted());
        assertEquals("Standby cycle executed",    true,  standbyCycleExecutor.isCycleExecuted());

    }



}
