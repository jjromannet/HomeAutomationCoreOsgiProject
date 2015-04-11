package net.jjroman.homeautomation.osgi.coalburner.states;

import java.util.Properties;

/**
 * Created by Jan on 11/04/2015.
 */
public interface State {

    State transit(Properties properties);

    boolean transitionRequired(Properties inputs);
}
