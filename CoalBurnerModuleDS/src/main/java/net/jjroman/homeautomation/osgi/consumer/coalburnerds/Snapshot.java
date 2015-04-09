package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * Created by Jan on 09/04/2015.
 */
public class Snapshot implements EnvironmentImmutableSnapshot {
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

    private Snapshot(Builder builder){
        standbyTimeout = builder.standbyTimeout;
        standbyFanHeadStart = builder.standbyFanHeadStart;
        standbyDispenserRunTime = builder.standbyDispenserRunTime;
        standbyFanAfterDispensedTime = builder.standbyFanAfterDispensedTime;
        activeFanAfterDispensedTime = builder.activeFanAfterDispensedTime;
        waterTankCurrentTemperature = builder.waterTankCurrentTemperature;
        waterTankActiveMaxTemperature = builder.waterTankActiveMaxTemperature;
        waterTankStandbyMinTemperature = builder.waterTankStandbyMinTemperature;
        activeFanHeadStart = builder.activeFanHeadStart;
        activeDispenserRunTime = builder.activeDispenserRunTime;
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

    public static class Builder {

        private long standbyTimeout = 0 ;
        private long standbyFanHeadStart = 0 ;
        private long standbyDispenserRunTime = 0 ;
        private long standbyFanAfterDispensedTime = 0 ;
        private long activeFanAfterDispensedTime = 0 ;
        private double waterTankCurrentTemperature = 0 ;
        private double waterTankActiveMaxTemperature = 0 ;
        private double waterTankStandbyMinTemperature = 0 ;
        private long activeFanHeadStart = 0 ;
        private long activeDispenserRunTime = 0 ;

        public Builder setStandbyTimeout(long standbyTimeout) {
            this.standbyTimeout = standbyTimeout;
            return this;
        }

        public Builder setStandbyFanHeadStart(long standbyFanHeadStart) {
            this.standbyFanHeadStart = standbyFanHeadStart;
            return this;
        }

        public Builder setStandbyDispenserRunTime(long standbyDispenserRunTime) {
            this.standbyDispenserRunTime = standbyDispenserRunTime;
            return this;
        }

        public Builder setStandbyFanAfterDispensedTime(long standbyFanAfterDispensedTime) {
            this.standbyFanAfterDispensedTime = standbyFanAfterDispensedTime;
            return this;
        }

        public Builder setActiveFanAfterDispensedTime(long activeFanAfterDispensedTime) {
            this.activeFanAfterDispensedTime = activeFanAfterDispensedTime;
            return this;
        }

        public Builder setWaterTankCurrentTemperature(double waterTankCurrentTemperature) {
            this.waterTankCurrentTemperature = waterTankCurrentTemperature;
            return this;
        }

        public Builder setWaterTankActiveMaxTemperature(double waterTankActiveMaxTemperature) {
            this.waterTankActiveMaxTemperature = waterTankActiveMaxTemperature;
            return this;
        }

        public Builder setWaterTankStandbyMinTemperature(double waterTankStandbyMinTemperature) {
            this.waterTankStandbyMinTemperature = waterTankStandbyMinTemperature;
            return this;
        }

        public Builder setActiveFanHeadStart(long activeFanHeadStart) {
            this.activeFanHeadStart = activeFanHeadStart;
            return this;
        }

        public Builder setActiveDispenserRunTime(long activeDispenserRunTime) {
            this.activeDispenserRunTime = activeDispenserRunTime;
            return this;
        }

        public EnvironmentImmutableSnapshot build(){
            return new Snapshot(this);
        }
    }
}
