package Source;

//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Queue;

public class Factors {
    public void normalize(double[] mean, double[] minValue, double[] maxValue) {
        for (int i = 0; i < NUMBER_OF_FACTORS; i++)
            factors[i] = (factors[i] - mean[i]) /255;// ((maxValue[i] - minValue[i]) < 1e-6 ? 1 : (maxValue[i] - minValue[i]));
    }

    class IntPair extends AbstractMap.SimpleEntry<Integer, Integer> {
        public IntPair(Integer key, Integer value) {
            super(key, value);
        }
    }

    // FIXME: Change this parameter when changing factors; otherwise it doesn't work properly.
    public static final int NUMBER_OF_FACTORS = 28*28 + 8 + 1;
    private double[] factors = new double[NUMBER_OF_FACTORS];
    private int[][] image;
    private int height, width;
    private boolean factorsCalculated = false;
    public Factors( int[][] image) {
        this.image = image;
        height = image.length;
        width = image[0].length;
    }

    private void calcFactors() {
        //This divisions are there to simplify the process of dividing the picture to parts
        factorsCalculated = true;
        double result;
        factors[0] = 1;
        int index = 1;
        final int division = 1;
        for (int i = 0; i < height / division; i++)
            for (int j = 0; j < width / division; j++)
            {
                int sum = 0;
                for (int ii = i * division; ii < (i+1) * division; ii++)
                    for (int jj = j * division; jj < (j+1) * division; jj++)
                        sum += image[ii][jj];
                factors[index++] = sum / division;
            }

        result = leftRightRatio();
        factors[index++] = result;
        result = colorChange();
        factors[index++] = result;

        result = heightwidthRatio();
        factors[index++] = result;
        result = hasMoreBackgroundPixels();
        factors[index++] = result;
        result = backgroundColorNumber();
        factors[index++] = result;

        result = topBottomRatio();
        factors[index++] = result;
        result = ratioOfPixelsUnderSecondaryDiagonal();
        factors[index++] = result;
        result = numberOfRings();
        factors[index++] = result;
    }

    public double[] getFactors() {
        if (!factorsCalculated)
            calcFactors();
        return factors;
    }
    // TODO: 09/06/2019 Implement all factors

    private double topBottomRatio() {
        int topPixels = 0, bottomPixels = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if(image[i][j] > 0)
                {
                    if (i < height / 2)
                        topPixels++;
                    else bottomPixels++;
                }
        if (bottomPixels == 0) bottomPixels = 1;
        return topPixels / (double)(1.0 * bottomPixels);
    }

    private double heightwidthRatio() {
        int h = 0, w = 0;
        int minHeight = 0, maxHeight= 0, minWidth= 0, maxWidth= 0;

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (image[i][j] > 0)
                   minHeight = i;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (image[j][i] > 0)
                    minWidth = i;

        for (int i = height - 1; i >= 0; i--)
            for (int j = 0; j < width; j++)
                if (image[i][j] > 0)
                    maxHeight = i;

        for (int i = width - 1; i >= 0; i--)
            for (int j = 0; j < height; j++)
                if (image[j][i] > 0)
                    maxWidth = i;

        h = maxHeight - minHeight;
        w = maxWidth - minWidth;

        if (w == 0) w = 1;
        return h / (double)(1.0 * w);
    }

    private double backgroundColorNumber() {
        int start = height / 3, end = 2 * height / 3 , sum = 0;
        for (int i = start; i <= end; i++)
            for (int j = 0; j < width; ){
                while (j < 28 && image[i][j] > 0)
                    j++;

                if (j < 28 && image[i][j] == 0){
                    sum++;
                    while (j < 28 && image[i][j] == 0)
                        j++;
                }

            }

        return (double)(1.0 * sum) / (double)(end - start +1);
    }

    private double hasMoreBackgroundPixels() {
        int pixels = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if(image[i][j] > 0)
                {
                    pixels++;
                }
        if (pixels > (height* width / 2.0) )
            return -1;
        else return 1;
    }

    private double leftRightRatio() {
        int leftPixels = 0, rightPixels = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if(image[i][j] > 0)
                {
                    if (j < width / 2)
                        leftPixels++;
                    else rightPixels++;
                }
        if (rightPixels == 0) rightPixels = 1;
        return leftPixels / (double)(1.0 * rightPixels);
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
        ret = differenceSum / (double)(1.0 * (width - 1) * (height) + (width - 1) * height);
        return ret;
    }

    private double ratioOfPixelsUnderSecondaryDiagonal() {
        int pixelCount = 0, underDiagonalPixelCount = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
            {
                if (image[i][j] > 0)
                {
                    pixelCount++;
                    if (j >= width - i - 1)
                        underDiagonalPixelCount++;
                }
            }
        if (pixelCount == 0) pixelCount = 1;
        return underDiagonalPixelCount / (double)(1.0 * pixelCount);
    }


    private IntPair findCenterOfMass() {
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
        return new IntPair((int) weightedSumX, (int) weightedSumY);
    }

    public int numberOfRings() {
        double[][] copyImage = new double[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                copyImage[i][j] = image[i][j];
        int[] dx = {1, 0, -1, 0}, dy = {0, 1, 0, -1};
        int rings = 0;

        Queue<IntPair> queue = new LinkedList<>();
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (copyImage[i][j] == 0)
                {
                    rings++;
                    queue.clear();
                    queue.add(new IntPair(i, j));
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
