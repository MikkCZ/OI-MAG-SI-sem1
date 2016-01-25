/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class WordWithGivenPrefix implements Task {

    private int N, S;
    private State[] states;
    private String P;

    @Override
    public String eval(InputStream is) throws IOException {
        Main.printTimeDuration("Start");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String[] lineArray;

        line = br.readLine();
        lineArray = line.split(" ");
        N = Integer.parseInt(lineArray[0]);
        S = Integer.parseInt(lineArray[1]);
        
        states = new State[N];

        int ID;
        for (int i = 0; i < N; i++) {
            line = br.readLine();
            lineArray = line.split(" ");
            ID = Integer.parseInt(lineArray[0]);
            if (states[ID] == null) {
                states[ID] = new State(ID, S);
            }
            if ("F".equals(lineArray[1].toUpperCase())) {
                states[ID].setFinal();
            }
            states[ID].addFollowers(lineArray, states);
        }
        
        for(State s : states) {
            s.printLine();
        }

        Main.printTimeDuration("Init states");

        P = br.readLine();

        Main.printTimeDuration("Word P");
        line = null;
        lineArray = null;

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        State start = states[0];
        Set<State> startStatesForPrefix = start.getStatesFor(P, 0);
        for (State s : startStatesForPrefix) {
            int[] minAndMax = s.getMinAndMax();
            if (minAndMax[State.MIN_INDEX] < min) {
                min = minAndMax[State.MIN_INDEX];
            }
            if (minAndMax[State.MAX_INDEX] > max) {
                max = minAndMax[State.MAX_INDEX];
            }
        }
        min += P.length();
        max += P.length();

        return "" + min + " " + max;
    }

}
