package Source;

import java.util.List;

public class Main {
    private final static String TRAIN_LABELS_ADDRESS = "C:\\Users\\Kiarash\\Desktop\\AI\\AiProject\\src\\Resource\\Train\\train-labels-idx1-ubyte";
    private final static String TRAIN_IMAGES_ADDRESS = "C:\\Users\\Kiarash\\Desktop\\AI\\AiProject\\src\\Resource\\Train\\train-images-idx3-ubyte";
    private final static String TEST_LABELS_ADDRESS = "C:\\Users\\Kiarash\\Desktop\\AI\\AiProject\\src\\Resource\\Test\\t10k-labels-idx1-ubyte";
    private final static String TEST_IMAGES_ADDRESS = "C:\\Users\\Kiarash\\Desktop\\AI\\AiProject\\src\\Resource\\Test\\t10k-images-idx3-ubyte";
    private final static String TRAIN_DATA = "C:\\Users\\Kiarash\\Desktop\\AI\\AiProject\\src\\Resource\\Train\\train_data.txt";//System.getProperty("user.dir") + "/src/Resource/Train/train_data.txt";

    public static void main(String[] args) {
        int[] trainLabels = MnistReader.getLabels(TRAIN_LABELS_ADDRESS);
        List<int[][]> trainImages = MnistReader.getImages(TRAIN_IMAGES_ADDRESS);
        Perceptron perceptron = new Perceptron(trainLabels, trainImages);
        perceptron.train();
        perceptron.printWeightsToFile(TRAIN_DATA);
        Perceptron testPerceptron = new Perceptron(TRAIN_DATA);
        int[] testLabels = MnistReader.getLabels(TEST_LABELS_ADDRESS);
        List<int[][]> testImages = MnistReader.getImages(TEST_IMAGES_ADDRESS);
        int wrongDecisions = 0;
        for (int i = 0; i < testImages.size(); i++)
        {
            int[][] image = testImages.get(i);
            Label decidedLabel = testPerceptron.test(image);
            if (decidedLabel.ordinal() != testLabels[i])
                wrongDecisions++;
        }
        System.out.println("Wrong decisions: " + wrongDecisions);
    }
}
