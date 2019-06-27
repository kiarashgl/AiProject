package Source;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public abstract class Classifier {
    protected int[] labels;
    protected List<int[][]> images = new LinkedList<>();
    protected ArrayList<double[]> weight = new ArrayList<>();
    protected double[] mean = new double[Factors.NUMBER_OF_FACTORS], minValue = new double[Factors.NUMBER_OF_FACTORS],
        maxValue = new double[Factors.NUMBER_OF_FACTORS];
    protected int heldOutCnt = 0;
    protected int numberOfImages = 60000 - heldOutCnt ;
    protected Factors[] factorsList = new Factors[numberOfImages];

    public Classifier(int[] labels, List<int[][]> images) {
        this.labels = labels;
        this.images = images;
    }

    public Classifier(String trainData) {
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
        int weightCnt = Integer.parseInt(scanner.nextLine());
        if (weight.size() == 0)
        {
            for (int i = 0; i < Label.values().length; i++)
                weight.add(new double[Factors.NUMBER_OF_FACTORS]);
        }
        for (int i = 0; i < weightCnt; i++)
        {
            int weightInd = Integer.parseInt(scanner.nextLine().trim().split(" ")[1]);
            for (int j = 0; j < Factors.NUMBER_OF_FACTORS; j++)
            {
                double weightParameter = Double.parseDouble(scanner.next());
                weight.get(weightInd)[j] = weightParameter;
            }
            scanner.nextLine();
        }
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
        {
            mean[i] = Double.parseDouble(scanner.next());
            minValue[i] = Double.parseDouble(scanner.next());
            maxValue[i] = Double.parseDouble(scanner.next());
        }
    }

    public Classifier() {
    }
    void normalize()
    {
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
        {
            maxValue[i] = Double.MIN_VALUE;
            minValue[i] = Double.MAX_VALUE;
        }
        for (Factors factors : factorsList)
        {
            double[] factorList = factors.getFactors();
            for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            {
                mean[i] += factorList[i];
                maxValue[i] = (factorList[i] > maxValue[i] ? factorList[i] : maxValue[i]);
                minValue[i] = (factorList[i] < minValue[i] ? factorList[i] : minValue[i]);
            }
        }
        for (int i = 0; i < mean.length; i++)
            mean[i] /= numberOfImages;
        for (Factors factors : factorsList)
            factors.normalize(mean, minValue, maxValue);
    }
    abstract public void train();

    abstract public Label test(int[][] image);

    public void printWeights() {
        for (int i = 0; i < weight.size(); i++)
        {
            double[] factors = weight.get(i);
            System.out.printf("Factor #%d :\n", i);
            for (int ind = 0; ind < factors.length; ind++)
                System.out.println("factor " + ind + " = " + factors[ind]);
        }
    }

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
        printWriter.println(weight.size());
        for (int i = 0; i < weight.size(); i++)
        {
            double[] factors = weight.get(i);
            printWriter.printf("Factor %d :\n", i);
            for (double factor : factors)
                printWriter.print(factor + " ");
            printWriter.println();
        }
        for (int i = 0; i < Factors.NUMBER_OF_FACTORS; i++)
            printWriter.println(mean[i] + " " + minValue[i] + " " + maxValue[i]);
        printWriter.flush();
        printWriter.close();
    }
}
