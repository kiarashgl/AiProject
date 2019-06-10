package Source;

import java.util.List;

public class Perceptron extends Classifier {
    private int numberOfImages;

    //    private static final int NUMBER_OF_TRAINS = 1000 * 1000 * 1000;
    public Perceptron(int[] labels, List<int[][]> images) {
        super(labels, images);
        numberOfImages = images.size();
        for (int i = 0; i < Label.values().length; i++)
            weight.add(new double[Factors.NUMBER_OF_FACTORS]);
    }

    private Label decideLabel(Factors factors) {
        double maxDotProduct = Double.MIN_VALUE, dotProduct = 0;
        Label ret = Label.TSHIRT_TOP;
        for (Label label : Label.values())
        {
            for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
                dotProduct += weight.get(label.ordinal())[i] * factors.getFactors()[i];
            if (dotProduct > maxDotProduct)
            {
                maxDotProduct = dotProduct;
                ret = label;
            }
        }
        return ret;
    }

    @Override
    public Label test(int[][] image) {
        Factors factors = new Factors(image);
        return decideLabel(factors);
    }

    @Override
    public void train() {
        int labelsCnt[] = new int[10];
        boolean changed = false;
        do
        {
            changed = false;
            for (int image = 0; image < numberOfImages; image++)
            {
                labelsCnt[labels[image]]++;
                Factors factors = new Factors(images.get(image));
                Label decidedLabel = decideLabel(factors);
                if (decidedLabel.ordinal() != labels[image])
                {
                    changed = true;
                    addToWeight(decidedLabel.ordinal(), factors.getFactors(), -1);
                    addToWeight(labels[image], factors.getFactors(), 1);
                }
                if (image % 1000 == 0)
                {
                    for (int i = 0; i < labelsCnt.length; i++)
                    {
                        System.out.printf("%d : %d|", i, labelsCnt[i]);
                        for (int j = 0; j < weight.get(i).length; j++)
                            System.out.printf("%f ", weight.get(i)[j]);
                        System.out.println();
                    }
                    System.out.println("salam");
                }
            }
        } while (changed);
    }

    // adds factor array multiplicated by coefficient to weight of the specified label.
    private void addToWeight(int label, double[] factors, int coeff) {
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            weight.get(label)[i] += coeff * factors[i];
    }

}
