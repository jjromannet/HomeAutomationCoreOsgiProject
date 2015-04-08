package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * Coal burner works in cycles this interface enables abstraction over concrete object that perform sequence of actions
 * to fullfill execution of one cycle.
 * Created by Jan on 06/04/2015.
 */
public interface CycleExecutor {
    void executeCycle(EnvironmentImmutableSnapshot environmentSnapshot) throws InterruptedException;
    void turnOff();
}
