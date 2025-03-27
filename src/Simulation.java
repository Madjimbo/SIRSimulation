import java.util.*;

public class Simulation {
    public enum Topology {
        ALL_TO_ALL, RANDOM_GRAPH
    }

    private final List<Agent> population;
    private final Random random;
    private final Topology topology;
    private final Map<Agent, List<Agent>> adjacencyList;

    public Simulation(int N, Topology topology) {
        this.population = new ArrayList<>();
        this.random = new Random();
        this.topology = topology;
        this.adjacencyList = new HashMap<>();

        // Create agents and adjacency map
        for (int i = 0; i < N; i++) {
            Agent agent = new Agent();
            population.add(agent);
            adjacencyList.put(agent, new ArrayList<>());
        }

        // Infect one random agent
        int infectedIndex = random.nextInt(N);
        population.get(infectedIndex).setState(Agent.State.INFECTED);

        // Create graph if RANDOM_GRAPH
        if (topology == Topology.RANDOM_GRAPH) {
            double connectionProbability = 0.01;
            int totalEdges = 0;

            for (int i = 0; i < N; i++) {
                for (int j = i + 1; j < N; j++) {
                    if (random.nextDouble() < connectionProbability) {
                        Agent a = population.get(i);
                        Agent b = population.get(j);
                        adjacencyList.get(a).add(b);
                        adjacencyList.get(b).add(a);
                        totalEdges += 2;
                    }
                }
            }

            double averageDegree = totalEdges / (double) N;
            System.out.printf("Generated Random Graph: Avg Degree = %.2f%n", averageDegree);
        }
    }

    public void simulateStep(double beta, double gamma) {
        if (topology == Topology.ALL_TO_ALL) {
            Agent a1 = population.get(random.nextInt(population.size()));
            Agent a2 = population.get(random.nextInt(population.size()));

            if (a1.getState() == Agent.State.INFECTED && a2.getState() == Agent.State.SUSCEPTIBLE && random.nextDouble() < beta) {
                a2.setState(Agent.State.INFECTED);
            } else if (a2.getState() == Agent.State.INFECTED && a1.getState() == Agent.State.SUSCEPTIBLE && random.nextDouble() < beta) {
                a1.setState(Agent.State.INFECTED);
            }
        } else if (topology == Topology.RANDOM_GRAPH) {
            List<Agent> newlyInfected = new ArrayList<>();
            for (Agent agent : population) {
                if (agent.getState() == Agent.State.INFECTED) {
                    for (Agent neighbour : adjacencyList.get(agent)) {
                        if (neighbour.getState() == Agent.State.SUSCEPTIBLE && random.nextDouble() < beta) {
                            newlyInfected.add(neighbour);
                        }
                    }
                }
            }
            for (Agent agent : newlyInfected) {
                agent.setState(Agent.State.INFECTED);
            }
        }

        // Recovery
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
