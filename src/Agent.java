
public class Agent {
    public enum State {SUSCEPTIBLE, INFECTED, RECOVERED}
    private State state;

    public Agent() {
        this.state = State.SUSCEPTIBLE;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }
}
