package Source;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public abstract class Classifier {
    protected int[] labels;
    protected List<int[][]> images = new LinkedList<>();
    protected ArrayList<float[]> weight = new ArrayList<>();

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
        for (int i = 0; i < weightCnt; i++)
        {
            int weightInd = Integer.parseInt(scanner.nextLine().trim().split(" ")[1]);
            for (int j = 0; j < Factors.NUMBER_OF_FACTORS; j++)
            {
                float weightParameter = Float.parseFloat(scanner.next());
                weight.get(weightInd)[j] = weightParameter;
            }
            scanner.nextLine();
        }

    }

    abstract public void train();

    abstract public Label test(int[][] image);

    public void printWeights() {
        for (int i = 0; i < weight.size(); i++)
        {
            float[] factors = weight.get(i);
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
            float[] factors = weight.get(i);
            printWriter.printf("Factor %d :\n", i);
            for (float factor : factors)
                printWriter.print(factor + " ");
            printWriter.println();
        }
        printWriter.flush();
        printWriter.close();
    }
}
