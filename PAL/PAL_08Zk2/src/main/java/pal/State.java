package pal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class State {

    public static final int MIN_INDEX = 0,
            MAX_INDEX = 1;

    protected final int ID, S;
    private boolean isFinal = false;
    private final Collection<State>[] followers;
    private final Set<State> allFollowers;
    private int[] minAndMax = null;

    public State(int ID, boolean isFinal, int S) {
        this.ID = ID;
        this.S = S;
        this.isFinal = isFinal;
        this.followers = new Collection[S];
        for (int i = 0; i < S; i++) {
            this.followers[i] = new ArrayList<>(S);
        }
        this.allFollowers = new HashSet<>(S);
    }

    public State(int ID, int S) {
        this(ID, false, S);
    }

    public void setFinal() {
        this.isFinal = true;
    }

    public void addFollower(int charID, int followerID, State[] states) {
        if (states[followerID] == null) {
            states[followerID] = new State(followerID, S);
        }
        this.followers[charID].add(states[followerID]);
        this.allFollowers.add(states[followerID]);
    }

    public void addFollowers(String[] lineArray, State[] states) {
        int charID;
        int followerID;

        for (int i = 2; i < lineArray.length; i++) {
            if (lineArray[i].isEmpty()) {
                continue;
            }
            charID = charToAlphabetIndex(lineArray[i].toLowerCase().toCharArray()[0]);
            while (true) {
                if (lineArray[i].isEmpty()) {
                    i++;
                    continue;
                }
                try {
                    followerID = Integer.decode(lineArray[i + 1]);
                    this.addFollower(charID, followerID, states);
                    i++;
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    private int charToAlphabetIndex(char c) {
        c = Character.toLowerCase(c);
        int tmp = (int) c;
        return (tmp - 96 - 1);
    }

    public Set<State> getStatesFor(String P, int index) {
        if (index == P.length()) {
            Set<State> tmp = new HashSet<>(1);
            tmp.add(this);
            return tmp;
        }
//        System.out.println("" + ID + " " + P.substring(index, P.length()));
        Set<State> toReturn = new HashSet<>(S);
        char c = P.charAt(index);
        Collection<State> cFollowers = followers[charToAlphabetIndex(c)];
        for (State cFollower : cFollowers) {
            toReturn.addAll(cFollower.getStatesFor(P, index + 1));
        }
        return toReturn;
    }

    public int[] getMinAndMax() {
        if (this.minAndMax != null) {
            return this.minAndMax;
        }
        int min = Integer.MAX_VALUE;
        if (this.isFinal) {
            min = 0;
        }
        int max = Integer.MIN_VALUE;
        if (this.isFinal) {
            max = 0;
        }
        for (State follower : allFollowers) {
            int[] followerMinAndMax = follower.getMinAndMax();
            if (!this.isFinal && followerMinAndMax[MIN_INDEX] < min) {
                min = followerMinAndMax[MIN_INDEX] + 1;
            }
            if (followerMinAndMax[MAX_INDEX] >= max) {
                max = followerMinAndMax[MAX_INDEX] + 1;
            }
        }
        this.minAndMax = new int[2];
        this.minAndMax[MIN_INDEX] = min;
        this.minAndMax[MAX_INDEX] = max;
//        System.out.println("" + ID + " " + min + " " + max);
        return this.minAndMax;
    }

//    private State getOutputStateFor(String ps, int index) {
//        if (index == ps.length()) {
//            return this;
//        }
//        int followerID = charToAlphabetIndex(ps.charAt(index));
//        State follower = followers[followerID];
//        return follower.getOutputStateFor(ps, index + 1);
//    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ID).append(" ");
        sb.append(isFinal).append(" ");
        for (int i = 0; i < S; i++) {
            Collection<State> coll = followers[i];
            sb.append(i).append(" ");
            for (State s : coll) {
                sb.append(s.ID).append(" ");
            }
            sb.append(",");
        }
        return sb.toString();
    }

}
