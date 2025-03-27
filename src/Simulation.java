import java.util.*;

public class Simulation {
    private final List<Agent> population;
    private final Random random;

    public Simulation(int N) {
        this.population = new ArrayList<>();
        this.random = new Random();
        for (int i = 0; i < N; i++) {
            population.add(new Agent());
        }
        // Infect one random agent
        int infectedIndex = random.nextInt(N);
        population.get(infectedIndex).setState(Agent.State.INFECTED);
    }

    public void simulateStep(double beta, double gamma) {
        // Infection step
        Agent a1 = population.get(random.nextInt(population.size()));
        Agent a2 = population.get(random.nextInt(population.size()));

        if (a1.getState() == Agent.State.INFECTED && a2.getState() == Agent.State.SUSCEPTIBLE && random.nextDouble() < beta) {
            a2.setState(Agent.State.INFECTED);
        } else if (a2.getState() == Agent.State.INFECTED && a1.getState() == Agent.State.SUSCEPTIBLE && random.nextDouble() < beta) {
            a1.setState(Agent.State.INFECTED);
        }

        // Recovery step
        Agent candidate = population.get(random.nextInt(population.size()));
        if (candidate.getState() == Agent.State.INFECTED && random.nextDouble() < gamma) {
            candidate.setState(Agent.State.RECOVERED);
        }
    }

    public int[] countStates() {
        int[] counts = new int[3]; // [S, I, R]
        for (Agent agent : population) {
            switch (agent.getState()) {
                case SUSCEPTIBLE -> counts[0]++;
                case INFECTED -> counts[1]++;
                case RECOVERED -> counts[2]++;
            }
        }
        return counts;
    }
}
