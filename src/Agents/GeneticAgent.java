package Agents;

import Main.Enterprise;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Preston Tang
 */
public class GeneticAgent implements Comparable<GeneticAgent> {

    private double mutationRate;
    private int[] chromosome;

    private int currentIndex;

    private double score;

    private final Enterprise ship;

    public GeneticAgent(Enterprise ship, int length, double mutationRate) {
        chromosome = new int[length];
        Arrays.fill(chromosome, 0);

        this.mutationRate = mutationRate;
        this.currentIndex = 0;
        this.ship = ship;
    }

    public void action() {
        if (currentIndex < chromosome.length) {
            if (!ship.finished) {
                ship.setBlasting(chromosome[currentIndex] == 1);
                currentIndex++;
            }
        }
    }

    public void randomize() {
        for (int i = 0; i < chromosome.length; i++) {
            //Will return either 0 or 1
            chromosome[i] = (int) Math.round(Math.random());
        }
    }

    public int[] getGenes() {
        return chromosome;
    }

    public int getGeneAtIndex(int index) {
        return chromosome[index];
    }

    public int[][] crossover(GeneticAgent b, int numChild) {
        int[][] childChromosomes = new int[numChild + 2][b.getGenes().length];

        for (int r = 0; r < childChromosomes.length - 2; r++) {
            for (int c = 0; c < childChromosomes[r].length; c++) {
                childChromosomes[r][c] = new Random().nextInt(100) < 50 ? chromosome[c] : b.getGenes()[c];
            }
            childChromosomes[r] = mutate(childChromosomes[r].clone());
        }

        childChromosomes[6] = this.getGenes();
        childChromosomes[7] = b.getGenes();

        return childChromosomes;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public int[] mutate(int[] genes) {
        for (int i = 0; i < genes.length; i++) {
            if (new Random().nextInt(100) <= mutationRate * 100) {
                genes[i] = genes[i] == 1 ? 0 : 1;
            }
        }

        return genes;
    }

    public void setGenes(int[] genes) {
        this.chromosome = genes;
    }

    public double getMutationRate() {
        return this.mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getScore() {
        return score;
    }

    public void determineScore() {
        this.score = ((ship.getTerminalVelocity() - (ship.getLandingSpeed() < 1.5 ? 0 : ship.getLandingSpeed())) * 15) + (ship.getFuel() * 0.55);
    }

    public Enterprise getShip() {
        return this.ship;
    }

    @Override
    public int compareTo(GeneticAgent t) {
        if (this.getScore() < t.getScore()) {
            return -1;
        } else if (t.getScore() < this.getScore()) {
            return 1;
        }
        return 0;
    }
}
