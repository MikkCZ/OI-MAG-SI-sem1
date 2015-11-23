package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class LinearCongruentialGenerator implements Task {

    private int A, C, M, K, N;
    private final int x1 = 0;

    @Override
    public String eval(InputStream is) throws IOException {
        Main.printTimeDuration("Start");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = br.readLine();
        String[] input = line.split(" ");
        A = Integer.parseInt(input[0]);
        C = Integer.parseInt(input[1]);
        M = Integer.parseInt(input[2]);
        K = Integer.parseInt(input[3]);
        N = Integer.parseInt(input[4]);

        br = null;
        line = null;
        Main.printTimeDuration("Reading input");

        int upperPrimeBound = (int) Math.ceil(M / getKMinusOnePrimesProduct());
        int[] primes = getPrimesUpTo(upperPrimeBound);
        BitSet kPrimesProducts = countAllKPrimesProducts(primes);
        // TODO: generuj poslouponost, prochazej a hledej maximum v oknech

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int generateNextVal(int xi) {
        return ((A * xi) + C) % M;
    }

    private BitSet countAllKPrimesProducts(int[] primes) {
        BitSet toReturn = new BitSet(M);
        toReturn.clear();
        AtomicBoolean terminate = new AtomicBoolean(false);
        countKPrimesProduct(primes, 0, 0, 0, toReturn, terminate);
        return toReturn;
    }

    private void countKPrimesProduct(int[] primes, int level, int k, int product, BitSet result, AtomicBoolean terminate) {
        if (terminate.get() || k >= K || level >= primes.length) {
            return;
        }
        int primeInThisLevel = primes[level];
        level += 1;
        // use prime in this level for product for the next one
        int newProduct = product * primeInThisLevel;
        if (newProduct > M) {
            terminate.set(true);
            return;
        }
        result.set(newProduct, true);
        countKPrimesProduct(primes, level, k + 1, newProduct, result, terminate);
        // or do not use it
        countKPrimesProduct(primes, level, k, product, result, terminate);
    }

    private int[] getPrimesUpTo(int max) {
        // TODO
    }

    private int getKMinusOnePrimesProduct() {
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
        int result = 1;
        for (int i = 0; i < K - 1; i++) {
            result *= primes[i];
        }
        return result;
    }

}
