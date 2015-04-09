package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * Worker thread to glue control logic with cycle executor
 * Created by Jan on 06/04/2015.
 */
class CoalBurnerRunnable implements Runnable {
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
            EnvironmentImmutableSnapshot environmentSnapshot = new EnvironmentImmutableSnapshotBasic(120,0,0,0,0,0,0,0,0,0);
            CoalBurnerControlLogic coalBurnerControlLogic = new CoalBurnerControlLogic();

            currentState = coalBurnerControlLogic.calculateCurrentState(currentState, environmentSnapshot);
            try {
                standbyCounter = coalBurnerControlLogic.executeCycle(currentState, standbyCounter, environmentSnapshot, standbyCycleExecutor, activeCycleExecutor);
            }catch (InterruptedException ie){
                ie.printStackTrace(System.out);
            }

    }
}
