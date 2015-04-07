package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * This interface provide access for object that should be immutable and should provide configuration and measures
 * values from particular point of time.
 * Created by Jan on 06/04/2015.
 */
public interface EnvironmentImmutableSnapshot {
    long getStandbyTimeout();
    long getStandbyFanHeadStart();
    long getStandbyDispenserRunTime();
    long getStandbyFanAfterDispensedTime();


    long getActiveFanHeadStart();
    long getActiveDispenserRunTime();
    long getActiveFanAfterDispensedTime();

    double getWaterTankCurrentTemperature();
    double getWaterTankActiveMaxTemperature();
    double getWaterTankStandbyMinTemperature();


}
