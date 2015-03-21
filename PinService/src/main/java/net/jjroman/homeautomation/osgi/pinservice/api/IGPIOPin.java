package net.jjroman.homeautomation.osgi.pinservice.api;

/**
 * Main interface abstracting GPIO Pin capable of being set to two states
 * Created by Jan on 27/02/2015.
 */
public interface IGPIOPin {

    public HiLoPinState getState();

    public void setState(HiLoPinState value);

}
