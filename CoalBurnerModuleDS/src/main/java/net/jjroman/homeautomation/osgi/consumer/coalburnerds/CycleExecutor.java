package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * Created by Jan on 06/04/2015.
 */
public interface CycleExecutor {
    void executeCycle(EnvironmentImmutableSnapshot environmentSnapshot);
}
