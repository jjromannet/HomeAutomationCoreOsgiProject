package net.jjroman.homeautomation.osgi.coalburner;

/**
 * Created by Jan on 11/04/2015.
 */
public enum Event {
    TEMPERATURE_DROPPED_BELOW_MINIMUM{
        @Override
        public State dispatch(State state) {
            return state.processTemperatureDropped();
        }
    },
    TEMPERATURE_RAISED_ABOVE_MAXIMUM{
        @Override
        public State dispatch(State state) {
            return state.processTemperatureRaised();
        }
    },
    TURNED_OFF{
        @Override
        public State dispatch(State state) {
            return state.processTurnOff();
        }
    },
    TURNED_ON{
        @Override
        public State dispatch(State state) {
            return state.processTurnOn();
        }
    };

    public abstract State dispatch(State state);
    public State dispatch(SpecialisedState stateProvider){
        return this.dispatch(stateProvider.getGeneric());
    }
}
