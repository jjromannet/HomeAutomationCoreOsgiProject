package net.jjroman.homeautomation.osgi.coalburner;

/**
 * Created by Jan on 11/04/2015.
 */
public class CoalBurner implements HeatSource {

    private static class CoalBurnerIdle implements SpecialisedState{
        private static final State STATE = State.IDLE;

        @Override
        public State getGeneric() {
            return STATE;
        }
    }

    private static class CoalBurnerActive implements SpecialisedState{
        private static final State STATE = State.ACTIVE;
        @Override
        public State getGeneric() {
            return STATE;
        }
    }

    private static class CoalBurnerOff implements  SpecialisedState{
        private static final State STATE = State.OFF;
        @Override
        public State getGeneric() {
            return STATE;
        }
    }
}
