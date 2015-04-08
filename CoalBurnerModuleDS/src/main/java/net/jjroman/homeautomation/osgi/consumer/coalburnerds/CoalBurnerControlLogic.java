package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * Defines control logic for the coal burner
 * Created by Jan on 05/04/2015.
 */
public class CoalBurnerControlLogic{

    CoalBurnerState calculateCurrentState(CoalBurnerState currentState, EnvironmentImmutableSnapshot environmentSnapshot){
        if(CoalBurnerState.ACTIVE.equals(currentState) && environmentSnapshot.getWaterTankCurrentTemperature() >= environmentSnapshot.getWaterTankActiveMaxTemperature()){
            return  CoalBurnerState.STANDBY;
        }else if(CoalBurnerState.STANDBY.equals(currentState) && environmentSnapshot.getWaterTankCurrentTemperature() <= environmentSnapshot.getWaterTankStandbyMinTemperature()){
            return  CoalBurnerState.ACTIVE;
        }
        return currentState;
    }

    long executeCycle(CoalBurnerState coalBurnerState, long currentCounter, EnvironmentImmutableSnapshot environmentSnapshot, CycleExecutor standCycleExecutor, CycleExecutor activeCycleExecutor) throws InterruptedException {

        if (CoalBurnerState.ACTIVE.equals(coalBurnerState)) {
            standCycleExecutor.turnOff();
            currentCounter = 0;
            activeCycleExecutor.executeCycle(environmentSnapshot);
        } else if (CoalBurnerState.STANDBY.equals(coalBurnerState)) {
            activeCycleExecutor.turnOff();
            //standby
            currentCounter++;
            if (currentCounter >= environmentSnapshot.getStandbyTimeout()) {
                currentCounter = 0;
                standCycleExecutor.executeCycle(environmentSnapshot);
            }
        } else {
            throw new UnsupportedOperationException("CurrentState out of scope");
        }
            return currentCounter;
        }
}
