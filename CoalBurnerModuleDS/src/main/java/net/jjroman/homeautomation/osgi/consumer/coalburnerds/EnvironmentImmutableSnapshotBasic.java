package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * EnvironmentImmutableSnapshotBasic deliver immutability by operation only on primitive properties which are set
 * upon construction. There is no this reference leak in constructor and all properties are final.
 * Created by Jan on 06/04/2015.
 */
public class EnvironmentImmutableSnapshotBasic implements EnvironmentImmutableSnapshot {
    private final long standbyTimeout;
    private final long standbyFanHeadStart;
    private final long standbyDispenserRunTime;
    private final long standbyFanAfterDispensedTime;
    private final long activeFanAfterDispensedTime;
    private final double waterTankCurrentTemperature;
    private final double waterTankActiveMaxTemperature;
    private final double waterTankStandbyMinTemperature;
    private final long activeFanHeadStart;
    private final long activeDispenserRunTime;

    public EnvironmentImmutableSnapshotBasic(long standbyTimeout, long standbyFanHeadStart, long standbyDispenserRunTime, long standbyFanAfterDispensedTime, long activeFanAfterDispensedTime, double waterTankCurrentTemperature, double waterTankActiveMaxTemperature, double waterTankStandbyMinTemperature, long activeFanHeadStart, long activeDispenserRunTime) {
        this.standbyTimeout = standbyTimeout;
        this.standbyFanHeadStart = standbyFanHeadStart;
        this.standbyDispenserRunTime = standbyDispenserRunTime;
        this.standbyFanAfterDispensedTime = standbyFanAfterDispensedTime;
        this.activeFanAfterDispensedTime = activeFanAfterDispensedTime;
        this.waterTankCurrentTemperature = waterTankCurrentTemperature;
        this.waterTankActiveMaxTemperature = waterTankActiveMaxTemperature;
        this.waterTankStandbyMinTemperature = waterTankStandbyMinTemperature;
        this.activeDispenserRunTime = activeDispenserRunTime;
        this.activeFanHeadStart = activeFanHeadStart;
    }

    @Override
    public long getStandbyTimeout() {
        return standbyTimeout;
    }

    @Override
    public long getStandbyFanHeadStart() {
        return standbyFanHeadStart;
    }

    @Override
    public long getStandbyDispenserRunTime() {
        return standbyDispenserRunTime;
    }

    @Override
    public long getStandbyFanAfterDispensedTime() {
        return standbyFanAfterDispensedTime;
    }

    @Override
    public long getActiveFanHeadStart() {
        return activeFanHeadStart;
    }

    @Override
    public long getActiveDispenserRunTime() {
        return activeDispenserRunTime;
    }

    @Override
    public long getActiveFanAfterDispensedTime() {
        return activeFanAfterDispensedTime;
    }

    @Override
    public double getWaterTankCurrentTemperature() {
        return waterTankCurrentTemperature;
    }

    @Override
    public double getWaterTankActiveMaxTemperature() {
        return waterTankActiveMaxTemperature;
    }

    @Override
    public double getWaterTankStandbyMinTemperature() {
        return waterTankStandbyMinTemperature;
    }
}
