package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Worker thread to glue control logic with cycle executor
 * Created by Jan on 06/04/2015.
 */
class CoalBurnerRunnable implements Runnable {
    Logger logger = LoggerFactory.getLogger(CoalBurnerRunnable.class);

    volatile CoalBurnerState currentState = CoalBurnerState.STANDBY;
    volatile long standbyCounter = 0;

    private final CycleExecutor standbyCycleExecutor;
    private final CycleExecutor activeCycleExecutor;

    public CoalBurnerRunnable(CycleExecutor standbyCycleExecutor, CycleExecutor activeCycleExecutor) {
        this.standbyCycleExecutor = standbyCycleExecutor;
        this.activeCycleExecutor = activeCycleExecutor;
    }

    @Override
    public void run() {

        EnvironmentImmutableSnapshot environmentSnapshot =
                new SnapshotBuilder()
                        .setStandbyTimeout(120)
                        .build();
            CoalBurnerControlLogic coalBurnerControlLogic = new CoalBurnerControlLogic();

            currentState = coalBurnerControlLogic.calculateCurrentState(currentState, environmentSnapshot);
            try {
                standbyCounter = coalBurnerControlLogic.executeCycle(currentState, standbyCounter, environmentSnapshot, standbyCycleExecutor, activeCycleExecutor);
            }catch (InterruptedException ie){
                logger.warn("Thread interrupted", ie);
                Thread.currentThread().interrupt();
            }finally {
                standbyCycleExecutor.turnOff();
                activeCycleExecutor.turnOff();
            }

    }
}
