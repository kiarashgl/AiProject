package Source;

import java.util.List;

public class Perceptron extends Classifier {
    private int numberOfImages;

    //    private static final int NUMBER_OF_TRAINS = 1000 * 1000 * 1000;
    public Perceptron(int[] labels, List<int[][]> images) {
        super(labels, images);
        numberOfImages = images.size();
    }

    private Label decideLabel(Factors factors) {
        double maxDotProduct = Double.MIN_VALUE, dotProduct = 0;
        Label ret = Label.TSHIRT_TOP;
        for (Label label : Label.values())
        {
            for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            {
                double[] doubleFactors = factors.getFactors();
                dotProduct += weight.get(label.ordinal())[i] * doubleFactors[i];
            }
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
        int[] wrongLabelsCnt = new int[10];
        Factors[] factorsList = new Factors[numberOfImages];
        for (int image = 0; image < numberOfImages; image++)
        {
            factorsList[image] = new Factors(images.get(image));
            factorsList[image].getFactors();
        }
        boolean changed = false;
        do
        {
            changed = false;
            for (int i = 0; i < wrongLabelsCnt.length; i++)
                wrongLabelsCnt[i] = 0;
            int wrongCnt = 0;
            for (int image = 0; image < numberOfImages; image++)
            {
                Label decidedLabel = decideLabel(factorsList[image]);
                if (decidedLabel.ordinal() != labels[image])
                {
                    wrongCnt++;
                    wrongLabelsCnt[decidedLabel.ordinal()]++;
                    changed = true;
                    addToWeight(decidedLabel.ordinal(), factorsList[image].getFactors(), -1);
                    addToWeight(labels[image], factorsList[image].getFactors(), 1);
                }
            }
            System.out.println("Wrong decisions: " + wrongCnt);
            for (int i = 0; i < wrongLabelsCnt.length; i++)
                System.out.println("label " + i + " wrongs :" +wrongLabelsCnt[i]);

            /*for (int i = 0; i < weight.size(); i++)
            {
                System.out.printf("%d |", i);
                for (int j = 0; j < weight.get(i).length; j++)
                    System.out.printf("%f ", weight.get(i)[j]);
                System.out.println();
            }*/
        } while (changed);
    }

    // adds factor array multiplicated by coefficient to weight of the specified label.
    private void addToWeight(int label, double[] factors, int coeff) {
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            weight.get(label)[i] += coeff * factors[i];
    }

}
