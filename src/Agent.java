public class Agent {
    public enum State { SUSCEPTIBLE, INFECTED, RECOVERED }
    public enum Strategy { VACCINATED, LONER }

    private State state;
    private Strategy strategy;

    public Agent(Strategy strategy) {
        this.strategy = strategy;
        // Vaccinated agents start as RECOVERED, others as SUSCEPTIBLE
        this.state = (strategy == Strategy.VACCINATED) ? State.RECOVERED : State.SUSCEPTIBLE;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
