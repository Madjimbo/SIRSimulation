import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        int N = 100;
        int T = 1000;
        int runs = 100;

        double[] betaValues = {0.1, 0.3, 0.6};
        double[] gammaValues = {0.05, 0.1, 0.2};

        for (double beta : betaValues) {
            for (double gamma : gammaValues) {

                int[] sumS = new int[T];
                int[] sumI = new int[T];
                int[] sumR = new int[T];

                for (int run = 0; run < runs; run++) {
                    Simulation sim = new Simulation(N);
                    for (int t = 0; t < T; t++) {
                        int[] counts = sim.countStates();
                        sumS[t] += counts[0];
                        sumI[t] += counts[1];
                        sumR[t] += counts[2];
                        sim.simulateStep(beta, gamma);
                    }
                }

                List<Integer> avgS = new ArrayList<>();
                List<Integer> avgI = new ArrayList<>();
                List<Integer> avgR = new ArrayList<>();
                for (int t = 0; t < T; t++) {
                    avgS.add(sumS[t] / runs);
                    avgI.add(sumI[t] / runs);
                    avgR.add(sumR[t] / runs);
                }

                String filename = String.format("SIR_beta_%.2f_gamma_%.2f.csv", beta, gamma);
                filename = filename.replace("0.", "0_").replace(".csv", "") + ".csv";
                FileWriter writer = new FileWriter(filename);
                writer.write("S,I,R\n");
                for (int t = 0; t < T; t++) {
                    writer.write(avgS.get(t) + "," + avgI.get(t) + "," + avgR.get(t) + "\n");
                }
                writer.close();

                System.out.println("Saved file: " + filename);
            }
        }
    }
}
