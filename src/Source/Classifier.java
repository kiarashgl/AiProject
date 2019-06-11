package Source;

import java.util.ArrayList;
import java.util.List;

public abstract class Classifier {
    protected int[] labels;
    protected List<int[][]> images;
    protected ArrayList<double[]> weight = new ArrayList<>();

    public Classifier(int[] labels, List<int[][]> images) {
        this.labels = labels;
        this.images = images;
    }

    abstract public void train();

    abstract public Label test(int[][] image);

    public void printWeights() {
        for (int i = 0; i < weight.size(); i++)
        {
            double[] factors = weight.get(i);
            System.out.printf("Factor #%d :\n", i);
            for (int ind = 0; ind < factors.length; ind++)
                System.out.println("factor " + ind + " = " + factors[i]);
        }
    }
}
