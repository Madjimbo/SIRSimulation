import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        int N = 100;
        int T = 4000;
        int runs = 100;

        double[] betaValues = {0.40, 0.35, 0.99};
        double[] gammaValues = {0.25, 0.14, 0.50};

        double vaccinationRate = 0.0; // 20% are vaccinated at the start

        for (int i = 0; i < betaValues.length; i++) {
            double beta = betaValues[i];
            double gamma = gammaValues[i];

            int[] sumS = new int[T];
            int[] sumI = new int[T];
            int[] sumR = new int[T];

            for (int run = 0; run < runs; run++) {
                Simulation sim = new Simulation(N, vaccinationRate);

                for (int t = 0; t < T; t++) {
                    // --- Quarantine Logic: every 100 steps, start immediately
                    if (t % 100 == 0) {
                        Optional<Agent> candidate = sim.getPopulation().stream()
                                .filter(a -> a.getState() == Agent.State.SUSCEPTIBLE && a.getStrategy() == Agent.Strategy.LONER)
                                .findAny();
                        candidate.ifPresent(a -> a.setState(Agent.State.RECOVERED));
                    }

                    // --- Vaccination Logic: starts after t = 1000
                    if (t >= 1000 && t % 100 == 0) {
                        Optional<Agent> candidate = sim.getPopulation().stream()
                                .filter(a -> a.getState() == Agent.State.SUSCEPTIBLE && a.getStrategy() == Agent.Strategy.LONER)
                                .findAny();
                        candidate.ifPresent(a -> a.setState(Agent.State.RECOVERED));
                    }

                    int[] counts = sim.countStates();
                    sumS[t] += counts[0];
                    sumI[t] += counts[1];
                    sumR[t] += counts[2];

                    sim.simulateStep(beta, gamma);
                }
            }

            // Save averaged results
            String filename = String.format("SIR_vaxquar_%.0f_beta_%.2f_gamma_%.2f.csv", vaccinationRate * 100, beta, gamma)
                    .replace("0.", "0_").replace(".csv", "") + ".csv";

            FileWriter writer = new FileWriter(filename);
            writer.write("S,I,R\n");

            for (int t = 0; t < T; t++) {
                int avgS = sumS[t] / runs;
                int avgI = sumI[t] / runs;
                int avgR = sumR[t] / runs;
                writer.write(avgS + "," + avgI + "," + avgR + "\n");
            }

            writer.close();
            System.out.println("Saved file: " + filename);
        }
    }
}
