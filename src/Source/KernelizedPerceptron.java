package Source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class KernelizedPerceptron extends Perceptron {
    private ArrayList<HashSet<Integer>> nonZeroAlpha = new ArrayList<>();
    public KernelizedPerceptron(int[] labels, List<int[][]> images) {
        super(labels, images);
        weight.clear();
        for (int i = 0; i < Label.values().length; i++)
        {
            weight.add(new double[numberOfImages]);
            nonZeroAlpha.add(new HashSet<>());
        }
    }

    private double kernel(int iFactorIndex, int testFactorIndex) {
        double ret = 0;
        double[] iFactorDouble = factorsList[iFactorIndex].getFactors(),
                testFactorDouble = factorsList[testFactorIndex].getFactors();
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            ret += iFactorDouble[i] * testFactorDouble[i];
        return (ret + 1) * (ret + 1);
    }

    @Override
    public Label test(int[][] image) {
        Factors factors = new Factors(image);
        factors.normalize(mean, minValue, maxValue);
        return decideLabel(factors);
    }

    private double kernel(int iFactorIndex, Factors test) {
        double ret = 0;
        double[] iFactorDouble = factorsList[iFactorIndex].getFactors(),
                testFactorDouble = test.getFactors();
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            ret += iFactorDouble[i] * testFactorDouble[i];
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
            for (int image : nonZeroAlpha.get(label.ordinal()))
            {
                double alpha = weight.get(label.ordinal())[image];
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
            for (int image : nonZeroAlpha.get(label.ordinal()))
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
        if (Math.abs (weight.get(decidedLabel.ordinal())[image]-0) > 1e-6)
            nonZeroAlpha.get(decidedLabel.ordinal()).add(image);
        weight.get(labels[image])[image]++;
        if (Math.abs (weight.get(labels[image])[image]-0) > 1e-6)
            nonZeroAlpha.get(labels[image]).add(image);
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
            double[] factors = weight.get(i);
            printWriter.printf("Label %d :\n", i);
            for (double factor : factors)
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
        numberOfImages = imagesCnt;
        nonZeroAlpha.clear();
        for(int i = 0; i < weight.size(); i++)
            nonZeroAlpha.add(new HashSet<>());
        weight.clear();
        for (int i = 0; i < Label.values().length; i++)
            weight.add(new double[imagesCnt]);

        for (int i = 0; i < weightCnt; i++)
        {
            int weightInd = Integer.parseInt(scanner.nextLine().trim().split(" ")[1]);
            for (int j = 0; j < imagesCnt; j++)
            {
                double weightParameter = Double.parseDouble(scanner.next());
                weight.get(weightInd)[j] = weightParameter;
                if (Math.abs(weightParameter - 0) > 1e-6)
                    nonZeroAlpha.get(i).add(j);
            }
            scanner.nextLine();
        }
        System.out.println("salam");
        scanner.close();

        maxImageIndex = numberOfImages - 1;
    }
    public KernelizedPerceptron(String trainData) {
        super();
        loadWeightsFromFile(trainData);
    }


}
