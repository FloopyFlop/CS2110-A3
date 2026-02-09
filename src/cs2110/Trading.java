package cs2110;

import java.util.Random;

import static cs2110.LoopInvariants.*;

/**
 * Contains methods for computing the optimal achievable profit of a stock transaction based on its
 * price history in a given time window.
 */
public class Trading {

    /**
     * Returns an *index* of the maximum value in `prices(i..]`. Requires that `0 <= i <
     * prices.length-1`.
     */
    static int argmaxTail(int[] prices, int i) {

        int j = i + 1;
        int max_index = prices.length;
        int max = prices[j];
        int k = j;

        /* Loop invariant: {max} will always be the max value in array range (i,j]*/
        while (j < max_index) {
            // Check if the max is now invalid
            if (prices[j] >= max) {
                max = prices[j];
                k = j;
            }
            j++;
        }
        return k;
    }

    /**
     * Returns the maximum profit that can be achieved through a transaction (a purchase followed by
     * a sale in a later time period) for the given `prices` window, or returns 0 if no profitable
     * transaction can be made. Requires `prices.length > 1`, and each entry of `prices` is >= 0.
     */
    static int optimalProfit1(int[] prices) {

        int optProfit = 0;
        int i = 0;

        /*
         * Loop invariant: `optProfit` is the maximum profit that can be achieved when the share is
         * purchased at a time in `[..i)`.
         */
        while (i < prices.length - 1) {
            assert optimalProfit1Invariant(prices, optProfit,
                    i); // DO NOT COUNT IN RUNTIME ANALYSIS

            int futureMaxValueIndex = argmaxTail(prices, i);

            // Evaluate profit of current term
            int currentProfit = prices[futureMaxValueIndex] - prices[i];
            if (currentProfit > optProfit) {
                optProfit = currentProfit;
            }

            i++;
        }
        return optProfit;
    }

    /**
     * Returns the maximum profit that can be achieved through a transaction (a purchase followed by
     * a sale in a later time period) for the given `prices` window, or returns 0 of no profitable
     * transaction can be made. Requires `prices.length > 1`, and each entry of `prices` is >= 0.
     */
    static int optimalProfit2(int[] prices) {
        int optProfit = 0;

        int j = prices.length - 2;
        int maxSell = prices[prices.length - 1];

        /*
         * Loop invariant: `optProfit` is the maximum profit that can be achieved when the share
         * is purchased at a time in `(j..]`.
         * Augmented invariant: `maxSell` is the maximum value in `prices(j..]`.
         */
        while (j >= 0) {
            assert optimalProfit2Invariant(prices, optProfit, j);

            int profitIfBuyNow = maxSell - prices[j];
            if (profitIfBuyNow > optProfit) {
                optProfit = profitIfBuyNow;
            }

            if (prices[j] > maxSell) {
                maxSell = prices[j];
            }

            j--;
        }
        return optProfit;
    }

    public static void main(String[] args) {

        // Generating Empty Array
        int[][] prices = new int[10][];
        for (int i = 0; i < 10; i++) {
            int[] prices_array = new int[(i + 1) * 100000];
            prices[i] = prices_array;
        }

        // Filling Array with Data
        for (int i = 0; i < prices.length; i++) {
            int prev_price = (int) (Math.random() * 900 + 101);
            for (int j = 0; j < prices[i].length; j++) {
                prices[i][j] = prev_price + (int) (Math.random() * 10 - 5 * Math.random() * 5);
            }
        }

        // Performing Runtime Analysis
        for (int i = 0; i < prices.length; i++) {
            float t_i = System.nanoTime();
            int cba = optimalProfit1(prices[i]);
            float t_f = System.nanoTime();
            System.out.println("The time for the method optimalProfit1 to complete for an array "
                    + "with size " + prices[i].length + " is " + (t_f - t_i) * 1000 + " ms.");

            t_i = System.nanoTime();
            int abc = optimalProfit2(prices[i]);
            t_f = System.nanoTime();
            System.out.println("The time for the method optimalProfit2 to complete for an array "
                    + "with size " + prices[i].length + " is " + (t_f - t_i) * 1000 + " ms.");
        }

    }

}
