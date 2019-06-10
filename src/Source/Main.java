package Source;

import java.util.List;

public class Main {
    private final static String TRAIN_LABELS_ADDRESS = "C:\\Users\\Kiarash\\Desktop\\AI\\AiProject\\src\\Resource\\Train\\train-labels-idx1-ubyte";
    private final static String TRAIN_IMAGES_ADDRESS = "C:\\Users\\Kiarash\\Desktop\\AI\\AiProject\\src\\Resource\\Train\\train-images-idx3-ubyte";

    public static void main(String[] args) {
        int[] labels = MnistReader.getLabels(TRAIN_LABELS_ADDRESS);
        List<int[][]> images = MnistReader.getImages(TRAIN_IMAGES_ADDRESS);
        Perceptron perceptron = new Perceptron(labels, images);
        perceptron.train();
        perceptron.printWeights();
    }
}
