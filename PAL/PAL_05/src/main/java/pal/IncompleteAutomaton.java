package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class IncompleteAutomaton implements Task {

    private int S, A, F, P, N, L;
    private State[] states;
    private String[] positiveSamples;

    @Override
    public String eval(InputStream is) throws IOException {
        Main.printTimeDuration("Start");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String[] lineArray;

        line = br.readLine();
        lineArray = line.split(" ");
        S = Integer.parseInt(lineArray[0]);
        A = Integer.parseInt(lineArray[1]);
        F = Integer.parseInt(lineArray[2]);
        P = Integer.parseInt(lineArray[3]);
        N = Integer.parseInt(lineArray[4]);
        L = Integer.parseInt(lineArray[5]);

        states = new State[S];
        positiveSamples = new String[P];

        int ID;
        for (int i = 0; i < S; i++) {
            line = br.readLine();
            lineArray = line.split(" ");
            ID = Integer.parseInt(lineArray[0]);
            if (states[ID] == null) {
                states[ID] = new State(ID, A);
            }
            states[ID].addFollowers(lineArray, states);
        }
        Main.printTimeDuration("Init states");

        for (int i = 0; i < P; i++) {
            positiveSamples[i] = br.readLine();
        }
        Main.printTimeDuration("Positive samples");
        line = null;
        lineArray = null;

        Result[] results = new Result[S];
        for (int i = 0; i < S; i++) {
            results[i] = states[i].getResult(positiveSamples, F);
        }

        StringBuilder sb = new StringBuilder();
        for (Result r : results) {
            if (r.isOK(F)) {
                sb.append(r.toString()).append("\n");
            }
        }

        return sb.toString().trim();
    }

}
