package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * Created by Jan on 09/04/2015.
 */
public class SnapshotBuilder {

    private long standbyTimeout;
    private long standbyFanHeadStart;
    private long standbyDispenserRunTime;
    private long standbyFanAfterDispensedTime;
    private long activeFanAfterDispensedTime;
    private double waterTankCurrentTemperature;
    private double waterTankActiveMaxTemperature;
    private double waterTankStandbyMinTemperature;
    private long activeFanHeadStart;
    private long activeDispenserRunTime;

    public SnapshotBuilder setStandbyTimeout(long standbyTimeout) {
        this.standbyTimeout = standbyTimeout;
        return this;
    }

    public SnapshotBuilder setStandbyFanHeadStart(long standbyFanHeadStart) {
        this.standbyFanHeadStart = standbyFanHeadStart;
        return this;
    }

    public SnapshotBuilder setStandbyDispenserRunTime(long standbyDispenserRunTime) {
        this.standbyDispenserRunTime = standbyDispenserRunTime;
        return this;
    }

    public SnapshotBuilder setStandbyFanAfterDispensedTime(long standbyFanAfterDispensedTime) {
        this.standbyFanAfterDispensedTime = standbyFanAfterDispensedTime;
        return this;
    }

    public SnapshotBuilder setActiveFanAfterDispensedTime(long activeFanAfterDispensedTime) {
        this.activeFanAfterDispensedTime = activeFanAfterDispensedTime;
        return this;
    }

    public SnapshotBuilder setWaterTankCurrentTemperature(double waterTankCurrentTemperature) {
        this.waterTankCurrentTemperature = waterTankCurrentTemperature;
        return this;
    }

    public SnapshotBuilder setWaterTankActiveMaxTemperature(double waterTankActiveMaxTemperature) {
        this.waterTankActiveMaxTemperature = waterTankActiveMaxTemperature;
        return this;
    }

    public SnapshotBuilder setWaterTankStandbyMinTemperature(double waterTankStandbyMinTemperature) {
        this.waterTankStandbyMinTemperature = waterTankStandbyMinTemperature;
        return this;
    }

    public SnapshotBuilder setActiveFanHeadStart(long activeFanHeadStart) {
        this.activeFanHeadStart = activeFanHeadStart;
        return this;
    }

    public SnapshotBuilder setActiveDispenserRunTime(long activeDispenserRunTime) {
        this.activeDispenserRunTime = activeDispenserRunTime;
        return this;
    }

    public EnvironmentImmutableSnapshot build(){
        return new EnvironmentImmutableSnapshot() {

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
        };
    }
}
