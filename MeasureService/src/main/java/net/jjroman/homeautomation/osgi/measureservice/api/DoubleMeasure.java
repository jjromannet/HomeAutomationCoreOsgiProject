package net.jjroman.homeautomation.osgi.measureservice.api;

/**
 * Measure providing double value
 * Created by Jan on 13/03/2015.
 */
public interface DoubleMeasure {
    double getValue();
    double subscribeToMeasureChange(MeasureConsumer<Double> consumer, MeasureName name);
}
