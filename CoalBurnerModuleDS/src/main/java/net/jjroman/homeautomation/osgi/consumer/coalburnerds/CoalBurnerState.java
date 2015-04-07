package net.jjroman.homeautomation.osgi.consumer.coalburnerds;

/**
 * This enum should be used to define current state of coalburner
 * ACTIVE - when water tank temperature is below given temp and coal burner actively trying to increase the temperature
 * STANDBY - when max temperature condition has been satisfied but water temp is still above minimum, then coal burner
 * executes empty - cycles - but once in a while it performs maintenance cycle to maintain fire in coal burner fire
 * chamber
 * Created by Jan on 06/04/2015.
 */
public enum CoalBurnerState {
    ACTIVE,
    STANDBY
}
