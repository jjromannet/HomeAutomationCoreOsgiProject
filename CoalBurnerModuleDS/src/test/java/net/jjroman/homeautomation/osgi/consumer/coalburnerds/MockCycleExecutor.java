package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * Mock cycle executor which records fact of being called so we can test functionality which operates on executors
 * Created by Jan on 06/04/2015.
 */
class MockCycleExecutor implements CycleExecutor{

    private boolean cycleExecuted = false;
    private boolean cycleTurnedOff = false;

    @Override
    public void executeCycle(EnvironmentImmutableSnapshot environmentSnapshot) {
        cycleExecuted = true;
    }

    @Override
    public void turnOff() {
        cycleTurnedOff = true;
    }

    public boolean isCycleExecuted() {
        return cycleExecuted;
    }
    public boolean isCycleTurnedOff() {
        return cycleTurnedOff;
    }
}