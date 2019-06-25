package Source;

import java.util.List;

public class Mira extends Perceptron {


    protected int numberOfImages = 60000;
    protected int maxImageIndex = 0;
    private static final int NUMBER_OF_TRAINS = 100000;


    public Mira(int[] labels, List<int[][]> images) {
        super(labels, images);
    }

    public Mira(String trainData) {
        super(trainData);
    }

    {
        for (int i = 0; i < Label.values().length; i++)
            weight.add(new float[Factors.NUMBER_OF_FACTORS]);
    }

    @Override
    protected void updateWeights(int image, Label decidedLabel) {
        float tau = getTau(decidedLabel.ordinal() , labels[image], image);

        addToWeight(decidedLabel.ordinal(), factorsList[image].getFactors(), -tau);
        addToWeight(labels[image], factorsList[image].getFactors(), tau);
    }

    protected void addToWeight(int label, float[] factors, double tau) {
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            weight.get(label)[i] += tau * factors[i];
    }

    protected Float getTau(int decidedLabel , int actualLabel ,int image ){
        float tau;
        final float maxUpdate = 10;
        //TODO ?????

        float[] w = new float[Factors.NUMBER_OF_FACTORS];

        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            w[i] = weight.get(decidedLabel)[i] - weight.get(actualLabel)[i];

        float dotProduct1 = 0;
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            dotProduct1 += w[i] * factorsList[image].getFactors()[i];
        dotProduct1 ++;

        float dotProduct2 = 0;
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            dotProduct2 += factorsList[image].getFactors()[i] * factorsList[image].getFactors()[i];
        dotProduct2 *= 2;
        if (dotProduct2 == 0)
            tau = maxUpdate;
        else {
            tau = dotProduct1 / dotProduct2;
            if (tau > maxUpdate)
                tau = maxUpdate;
        }
        return tau;
    }


}
