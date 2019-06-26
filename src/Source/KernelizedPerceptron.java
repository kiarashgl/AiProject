package Source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class KernelizedPerceptron extends Perceptron {
    //    float[][] kernelList = new float[numberOfImages][numberOfImages];
    public KernelizedPerceptron(int[] labels, List<int[][]> images) {
        super(labels, images);
        weight.clear();
        for (int i = 0; i < Label.values().length; i++)
            weight.add(new float[numberOfImages]);
        //        for (int i = 0; i < numberOfImages; i++)
        //            for (int j = 0; j < numberOfImages; j++)
        //                kernelList[i][j] = kernel(factorsList[i], factorsList[j]);
    }



    private double kernel(int iFactorIndex, int testFactorIndex) {
        double ret = 0;
        float[] iFactorFloat = factorsList[iFactorIndex].getFactors(),
                testFactorFloat = factorsList[testFactorIndex].getFactors();
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            ret += iFactorFloat[i] * testFactorFloat[i];
        return (ret + 1) * (ret + 1);
    }

    @Override
    public Label test(int[][] image) {
        Factors factors = new Factors(image);
        return decideLabel(factors);
    }

    private double kernel(int iFactorIndex, Factors test) {
        double ret = 0;
        float[] iFactorFloat = factorsList[iFactorIndex].getFactors(),
                testFactorFloat = test.getFactors();
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            ret += iFactorFloat[i] * testFactorFloat[i];
        return (ret + 1) * (ret + 1);
    }
    @Override
    protected Label decideLabel(Factors factors) {
        double maxDotProduct = 0, dotProduct = 0;
        Label ret = null;
        boolean first = true;

        for (Label label : Label.values())
        {
            dotProduct = 0;
            for (int image = 0; image <= maxImageIndex; image++)
            {
                float alpha = weight.get(label.ordinal())[image];
                if (alpha != 0)
                    dotProduct += weight.get(label.ordinal())[image] * kernel(image, factors);
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
    protected Label decideLabel(int factorsIndex) {
        double maxDotProduct = 0, dotProduct = 0;
        Label ret = null;
        boolean first = true;

        for (Label label : Label.values())
        {
            dotProduct = 0;
            for (int image = 0; image <= maxImageIndex; image++)
            {
                float alpha = weight.get(label.ordinal())[image];
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

    @Override
    public void printWeightsToFile(String trainData) {
        PrintWriter printWriter = null;
        try
        {
            printWriter = new PrintWriter(trainData);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        assert printWriter != null;

        printWriter.println(numberOfImages);
        printWriter.println(weight.size());
        for (int i = 0; i < weight.size(); i++)
        {
            float[] factors = weight.get(i);
            printWriter.printf("Label %d :\n", i);
            for (float factor : factors)
                printWriter.print(factor + " ");
            printWriter.println();
        }


        printWriter.flush();
        printWriter.close();
    }
    void loadWeightsFromFile(String trainData)
    {
        File trainDataFile = new File(trainData);
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(trainDataFile);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        assert scanner != null;
        // Here I tried to read the input train data file properly :)
        int imagesCnt = Integer.parseInt(scanner.nextLine());
        int weightCnt = Integer.parseInt(scanner.nextLine());
        weight.clear();
        for (int i = 0; i < Label.values().length; i++)
            weight.add(new float[imagesCnt]);

        for (int i = 0; i < weightCnt; i++)
        {
            int weightInd = Integer.parseInt(scanner.nextLine().trim().split(" ")[1]);
            for (int j = 0; j < imagesCnt; j++)
            {
                float weightParameter = Float.parseFloat(scanner.next());
                weight.get(weightInd)[j] = weightParameter;
            }
            scanner.nextLine();
        }
        System.out.println("salam");
    }
    public KernelizedPerceptron(String trainData) {
        super();
        loadWeightsFromFile(trainData);
    }


}
