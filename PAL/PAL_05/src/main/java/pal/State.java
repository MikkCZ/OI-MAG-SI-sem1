package pal;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class State<T extends State> implements Comparable<T> {

    protected final int ID, A;
    private final String stringID;
    private final State[] followers;

    public State(int ID, int A) {
        this.ID = ID;
        this.A = A;
        this.stringID = Integer.toString(ID);
        this.followers = new State[A];
    }

    public void addFollowers(String[] lineArray, State[] states) {
        int charID, followerID;
        for (int i = 1; i < lineArray.length; i++) {
            charID = i - 1;
            followerID = Integer.parseInt(lineArray[i]);
            if (states[followerID] == null) {
                states[followerID] = new State(followerID, A);
            }
            this.followers[charID] = states[followerID];
        }
    }

    public Result getResult(String[] positiveSamples, int F) {
        Result r = new Result(this);
        String ps;
        int samplesLeft = positiveSamples.length;
        int rSize = 0;
        for (int i = 0; i < positiveSamples.length; i++) {
            ps = positiveSamples[i];
            samplesLeft--;
            r.addOutput(this.getOutputStateFor(ps, 0));
            rSize = r.size();
            if (samplesLeft + rSize < F || rSize > F) {
                r.ko();
                break;
            }
        }
        return r;
    }

    private int charToAlphabetIndex(char c) {
        c = Character.toLowerCase(c);
        int tmp = (int) c;
        return (tmp - 96 - 1);
    }

    private State getOutputStateFor(String ps, int index) {
        if (index == ps.length()) {
            return this;
        }
        int followerID = charToAlphabetIndex(ps.charAt(index));
        State follower = followers[followerID];
        return follower.getOutputStateFor(ps, index + 1);
    }

    @Override
    public int compareTo(T o) {
        return this.ID - o.ID;
    }

    @Override
    public String toString() {
        return stringID;
    }

}
