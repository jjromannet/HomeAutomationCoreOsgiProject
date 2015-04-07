package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Testing coal burner state changin loginc
 * STANDBY -> ACTIVE - when current temp is lower than standby min
 * STANDBY -> STANDBY - when current temp is higher than standby min
 * ACTIVE -> ACTIVE - when current temp is lower than active max
 * ACTIVE -> STANDBY - when current temp is higher than active max
 * Created by Jan on 06/04/2015.
 */
public class CoalBurnerStateTransitionsTest {

    CoalBurnerControlLogic coalBurnerControlLogic = new CoalBurnerControlLogic();

    /**
     * Active temperature MAX(%f) is a maximum temperature that coal burner can run for ACTIVE state.
     * If current temp (%f) is over this then CB should go into standby state.
     */
    @Test
    public void testChangeToStandbyIfCurrentTempGreaterThanThreshold(){

        EnvironmentImmutableSnapshot environmentImmutableSnapshot =  getEnvSnapshot(70d);
        assertEquals(
                String.format("Active temperature MAX(%f) is a maximum temperature that coal burner can run for ACTIVE state. " +
                        "If current temp (%f) is over this then CB should go into standby state.",
                        environmentImmutableSnapshot.getWaterTankActiveMaxTemperature(),
                        environmentImmutableSnapshot.getWaterTankCurrentTemperature() ),
                CoalBurnerState.STANDBY,
                coalBurnerControlLogic.calculateCurrentState(CoalBurnerState.ACTIVE, environmentImmutableSnapshot));
    }

    /**
     * If current temperature is lower than coal burner standby minimum - turn on active state
     */
    @Test
    public void testChangeToActiveIfCurrentTempLowerThanStandbyMin(){
        EnvironmentImmutableSnapshot environmentImmutableSnapshot =  getEnvSnapshot(49.9d);
        assertEquals(
                String.format("If current temperature(%f) is lower than coal burner standby minimum (%f) - turn on active state",
                        environmentImmutableSnapshot.getWaterTankCurrentTemperature(),
                        environmentImmutableSnapshot.getWaterTankStandbyMinTemperature()),
                CoalBurnerState.ACTIVE,
                coalBurnerControlLogic.calculateCurrentState(CoalBurnerState.STANDBY, environmentImmutableSnapshot));
    }

    @Test
    public void testDontChangeIfCurrentTemperatureIsWithinThreshold(){
        EnvironmentImmutableSnapshot environmentImmutableSnapshot =  getEnvSnapshot(55d);

        CoalBurnerState coalBurnerState = CoalBurnerState.ACTIVE;

        assertEquals(
                String.format("If current temperature(%f) is within min (%f) and max (%f) than coal burner should not change current state (%s)",
                        environmentImmutableSnapshot.getWaterTankCurrentTemperature(),
                        environmentImmutableSnapshot.getWaterTankStandbyMinTemperature(),
                        environmentImmutableSnapshot.getWaterTankActiveMaxTemperature(),
                        coalBurnerState),

                coalBurnerState,
                coalBurnerControlLogic.calculateCurrentState(coalBurnerState, environmentImmutableSnapshot));

        coalBurnerState = CoalBurnerState.STANDBY;
        assertEquals(
                String.format("If current temperature(%f) is within min (%f) and max (%f) than coal burner should not change current state (%s)",
                        environmentImmutableSnapshot.getWaterTankCurrentTemperature(),
                        environmentImmutableSnapshot.getWaterTankStandbyMinTemperature(),
                        environmentImmutableSnapshot.getWaterTankActiveMaxTemperature(),
                        coalBurnerState),
                coalBurnerState,
                coalBurnerControlLogic.calculateCurrentState(coalBurnerState, environmentImmutableSnapshot));

    }

    private EnvironmentImmutableSnapshot getEnvSnapshot(double currentTemp){

        final double minStandbyTemp = 50.0d;
        final double maxActiveTemp = 69.9d;

        return new EnvironmentImmutableSnapshotBasic(0,0,0,0,0,currentTemp,maxActiveTemp,minStandbyTemp,0,0);
    }
}
