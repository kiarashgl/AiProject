package Source;

import java.util.List;

public class KernelizedPerceptron extends Perceptron {
    //    double[][] kernelList = new double[numberOfImages][numberOfImages];
    public KernelizedPerceptron(int[] labels, List<int[][]> images) {
        super(labels, images);
        weight.clear();
        for (int i = 0; i < Label.values().length; i++)
            weight.add(new double[numberOfImages]);
        //        for (int i = 0; i < numberOfImages; i++)
        //            for (int j = 0; j < numberOfImages; j++)
        //                kernelList[i][j] = kernel(factorsList[i], factorsList[j]);
    }


    public KernelizedPerceptron(String trainData) {
        super(trainData);
    }

    private double kernel(int iFactorIndex, int testFactorIndex) {
        double ret = 0;
        double[] iFactorDouble = factorsList[iFactorIndex].getFactors(),
                testFactorDouble = factorsList[testFactorIndex].getFactors();
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            ret += iFactorDouble[i] * testFactorDouble[i];
        return ret;
    }

    @Override
    protected Label decideLabel(int factorsIndex) {
        double maxDotProduct = 0, dotProduct = 0;
        Label ret = null;
        boolean first = true;

        for (Label label : Label.values())
        {
            dotProduct = 0;
            for (int image = 0; image <= maxImageIndex; image++)
            {
                double alpha = weight.get(label.ordinal())[image];
                if (alpha != 0)
                    dotProduct += weight.get(label.ordinal())[image] * kernel(image, factorsIndex);
            }
            if (first || dotProduct > maxDotProduct)
            {
                maxDotProduct = dotProduct;
                ret = label;
            }
            first = false;
        }
        return ret;
    }

    @Override
    protected void updateWeights(int image, Label decidedLabel) {
        weight.get(decidedLabel.ordinal())[image]--;
        weight.get(labels[image])[image]++;
    }
}
