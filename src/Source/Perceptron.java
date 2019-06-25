package Source;

import java.util.List;

public class Perceptron extends Classifier {
    protected int numberOfImages = 10000;
    protected Factors[] factorsList = new Factors[numberOfImages];
    protected int maxImageIndex = 0;
    protected static final int NUMBER_OF_TRAINS = 2;


    public Perceptron(int[] labels, List<int[][]> images) {
        super(labels, images);

        for (int image = 0; image < numberOfImages; image++)
        {
            factorsList[image] = new Factors(images.get(image));
            factorsList[image].getFactors();
        }
    }
    {
        for (int i = 0; i < Label.values().length; i++)
            weight.add(new float[Factors.NUMBER_OF_FACTORS]);
    }

    public Perceptron(String trainData) {
        super(trainData);
    }

    public Perceptron() {
    }

    protected Label decideLabel(Factors factors) {
        float maxDotProduct = 0, dotProduct = 0;
        Label ret = null;
        boolean first = true;
        float[] floatFactors = factors.getFactors();

        for (Label label : Label.values())
        {
            dotProduct = 0;
            for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
                dotProduct += weight.get(label.ordinal())[i] * floatFactors[i];
            if (first || dotProduct > maxDotProduct)
            {
                maxDotProduct = dotProduct;
                ret = label;
            }
            first = false;
        }
        return ret;
    }
    protected Label decideLabel(int factorIndex)
    {
        return decideLabel(factorsList[factorIndex]);
    }
    @Override
    public Label test(int[][] image) {
        Factors factors = new Factors(image);
        return decideLabel(factors);
    }

    @Override
    public void train() {
//        int[] wrongLabelsCnt = new int[10];

        boolean changed = true;
        for (int train = 0; train < NUMBER_OF_TRAINS && changed; train++)
        {
            changed = false;
/*            for (int i = 0; i < wrongLabelsCnt.length; i++)
                wrongLabelsCnt[i] = 0;*/
            int wrongCnt = 0;
            for (int image = 0; image < numberOfImages; image++)
            {
                maxImageIndex = Math.max(maxImageIndex, image);
                Label decidedLabel = decideLabel(image);
                if (decidedLabel.ordinal() != labels[image])
                {
                    wrongCnt++;
//                    wrongLabelsCnt[decidedLabel.ordinal()]++;
                    changed = true;
                    updateWeights(image, decidedLabel);
                }
                if (image %1000 == 0 )
                    System.out.println(image);
            }
            System.out.println("Wrong decisions: " + wrongCnt);
            /*for (int i = 0; i < wrongLabelsCnt.length; i++)
                System.out.println("label " + i + " wrongs :" + wrongLabelsCnt[i]);*/

            /*for (int i = 0; i < weight.size(); i++)
            {
                System.out.printf("%d |", i);
                for (int j = 0; j < weight.get(i).length; j++)
                    System.out.printf("%f ", weight.get(i)[j]);
                System.out.println();
            }*/
        }
    }

    protected void updateWeights(int image, Label decidedLabel) {

        addToWeight(decidedLabel.ordinal(), factorsList[image].getFactors(), -1);
        addToWeight(labels[image], factorsList[image].getFactors(), 1);
    }

    // adds factor array multiplicated by coefficient to weight of the specified label.
    protected void addToWeight(int label, float[] factors, int coeff) {
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            weight.get(label)[i] += coeff * factors[i];
    }

}
