import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Queue;

public class Factors {
    class IntPair extends AbstractMap.SimpleEntry<Integer, Integer>{
        public IntPair(Integer key, Integer value) {
            super(key, value);
        }
    };
    public static final int NUMBER_OF_FACTORS = 10;
    private double[] factors = new double[NUMBER_OF_FACTORS];
    private int[][] image;
    private int height = image.length, width = image[0].length;

    public Factors(int[][] image) {
        this.image = image;
    }

    private void calcFactors() {
        double result;
        factors[0] = 1;
        int i = 1;
        result = topDownRatio();
        factors[i++] = result;
    }

    public double[] getFactors() {
        calcFactors();
        return factors;
    }
    // TODO: 09/06/2019 Implement all factors

    private double topDownRatio() {
        // TODO: 09/06/2019 Implement
        return 1;
    }

    //Calculate color change of consecutive pixels
    private double colorChange() {
        double ret = 0;
        int differenceSum = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width - 1; j++)
                if (image[i][j] > 0 && image[i][j + 1] > 0)
                    differenceSum += Math.abs(image[i][j] - image[i][j + 1]);
        for (int j = 0; j < width; j++)
            for (int i = 0; i < height - 1; i++)
                if (image[i][j] > 0 && image[i + 1][j] > 0)
                    differenceSum += Math.abs(image[i][j] - image[i + 1][j]);
        ret = differenceSum / (1.0 * (width - 1) * (height) + (width - 1) * height);
        return ret;
    }

    private double ratioOfPixelsUnderSecondaryDiagonal()
    {
        int pixelCount = 0, underDiagonalPixelCount = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
            {
                if (image[i][j] > 0)
                {
                    pixelCount++;
                    if(j >= width - i - 1)
                        underDiagonalPixelCount++;
                }
            }
        if (pixelCount == 0) pixelCount = 1;
        return underDiagonalPixelCount / (1.0 * pixelCount);
    }

    private IntPair findCenterOfMass()
    {
        double sumOfPoints = 0, weightedSumX = 0, weightedSumY = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
            {
                sumOfPoints += image[i][j];
                weightedSumX += i * image[i][j];
                weightedSumY += j * image[i][j];
            }
        weightedSumX /= sumOfPoints;
        weightedSumY /= sumOfPoints;
        return new IntPair((int)weightedSumX,(int)weightedSumY);
    }
    private double numberOfRings()
    {
        double[][] copyImage = new double[height][width];
        for(int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                copyImage[i][j] = image[i][j];
        int[] dx = {1, 0, -1, 0}, dy = {0,  1 , 0, -1};
        int rings = 0;

        Queue<IntPair> queue = new LinkedList<>();
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (copyImage[i][j] == 0)
                {
                    rings++;
                    queue.clear();
                    queue.add(new IntPair(i,j));
                    while (!queue.isEmpty())
                    {
                        IntPair cur = queue.poll();
                        for (int ind = 0; ind < dx.length; ind++)
                        {
                            IntPair nxt = new IntPair(cur.getKey() + dx[ind], cur.getValue() + dy[ind]);
                            Integer nextX = nxt.getKey(), nextY = nxt.getValue();
                            if (nextX < 0 || nextX >= height || nextY < 0 || nextY >= width)
                                continue;
                            if (copyImage[nextX][nextY] == 0)
                            {
                                copyImage[nextX][nextY] = 255;
                                queue.add(nxt);
                            }
                        }
                    }
                }
        return rings;
    }
}
