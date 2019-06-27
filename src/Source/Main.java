package Source;

import java.util.List;

public class Main {
    public final static String PROJECT_ROOT = "C:/Users/Kiarash/Desktop/AI/AiProject";
    public final static String TRAIN_LABELS_ADDRESS = PROJECT_ROOT + "/src/Resource/Train/train-labels-idx1-ubyte";
    public final static String TRAIN_IMAGES_ADDRESS = PROJECT_ROOT + "/src/Resource/Train/train-images-idx3-ubyte";
    public final static String TEST_LABELS_ADDRESS = PROJECT_ROOT + "/src/Resource/Test/t10k-labels-idx1-ubyte";
    public final static String TEST_IMAGES_ADDRESS = PROJECT_ROOT + "/src/Resource/Test/t10k-images-idx3-ubyte";
    public final static String TRAIN_DATA = PROJECT_ROOT + "/src/Resource/Train/train_data2.txt";
    public final static String TRAIN_DATA_MIRA = PROJECT_ROOT + "/src/Resource/Train/train_data_mira2.txt";
    public final static String TRAIN_DATA_KERNEL = PROJECT_ROOT + "/src/Resource/Train/train_data_kernel.txt";

    public static void testP()
    {


//        Perceptron testPerceptron = new Perceptron(TRAIN_DATA);
//        Perceptron testPerceptron = new Perceptron(trainLabels, trainImages);
//        perceptron.loadWeightsFromFile(TRAIN_DATA);
        int wrongDecisions = 0;
        for (int i = 0; i < testImages.size(); i++)
        {
            int[][] image = testImages.get(i);
            Label decidedLabel = perceptron.test(image);
            if (decidedLabel.ordinal() != testLabels[i])
            {
                wrongDecisions++;
                System.out.println(i);
            }
        }
        System.out.println("Test Wrong decisions: " + wrongDecisions);

    }
    static int[] testLabels = MnistReader.getLabels(TEST_LABELS_ADDRESS);
    static List<int[][]> testImages = MnistReader.getImages(TEST_IMAGES_ADDRESS);
    static int[] trainLabels = MnistReader.getLabels(TRAIN_LABELS_ADDRESS);
    static List<int[][]> trainImages = MnistReader.getImages(TRAIN_IMAGES_ADDRESS);
    static KernelizedPerceptron perceptron = new KernelizedPerceptron(trainLabels, trainImages);

    public static void main(String[] args) {
        perceptron.loadWeightsFromFile(TRAIN_DATA);
        perceptron.train();
//



//        KernelizedPerceptron kp = new KernelizedPerceptron(TRAIN_DATA);

//        kp.train();
//        kp.printWeightsToFile(TRAIN_DATA_KERNEL);
//        kp = new KernelizedPerceptron(TRAIN_DATA_KERNEL);



        /*Mira mira = new Mira(trainLabels,trainImages);
        mira.train();
        mira.printWeightsToFile(TRAIN_DATA_MIRA);
//        Mira mira = new Mira(TRAIN_DATA_MIRA);
        int[] testLabels = MnistReader.getLabels(TEST_LABELS_ADDRESS);
        List<int[][]> testImages = MnistReader.getImages(TEST_IMAGES_ADDRESS);
        int wrongDecisions = 0;
        for (int i = 0; i < testImages.size(); i++)
        {
            int[][] image = testImages.get(i);
            Label decidedLabel = mira.test(image);
            if (decidedLabel.ordinal() != testLabels[i])
                wrongDecisions++;
        }
        System.out.println("Wrong decisions: " + wrongDecisions);*/
    }
}
