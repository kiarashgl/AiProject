package UnitTest;

import Source.Factors;
import org.junit.Assert;
import org.junit.Test;

public class FactorsTest {
    @Test
    public void testRings() {
        int[][] image ={
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}};
        Factors factors = new Factors(image);
        Assert.assertEquals(factors.numberOfRings(), 5);
    }
}