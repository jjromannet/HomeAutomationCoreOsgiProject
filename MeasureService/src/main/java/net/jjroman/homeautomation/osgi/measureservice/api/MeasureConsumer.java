package net.jjroman.homeautomation.osgi.measureservice.api;

/**
 * Created by Jan on 29/04/2015.
 */
public interface MeasureConsumer<T> {

    void updateMeasure(Double newValue, MeasureName name);

}
