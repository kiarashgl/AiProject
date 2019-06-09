public class Factors {
    public static final int NUMBER_OF_FACTORS = 10;
    private double[] factors = new double[NUMBER_OF_FACTORS];
    private int[][] image;

    public Factors(int[][] image) {
        this.image = image;
    }

    public void calcFactors()
    {
        double result;
        factors[0] = 1;
        int i = 1;
        result = topDownRatio();
        factors[i++] = result;
    }

    private double topDownRatio() {
        // TODO: 09/06/2019 Implement
        return 1;
    }
    // TODO: 09/06/2019 Implement all factors

    public double[] getFactors()
    {
        calcFactors();
        return factors;
    }
}
