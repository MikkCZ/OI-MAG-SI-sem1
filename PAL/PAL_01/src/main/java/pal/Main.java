/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.io.IOException;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class Main {

    private static long lastKnownTime = System.currentTimeMillis();
    private static final boolean DEBUG = false;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        final Task t = new BackupConnection();
        System.out.println(t.eval(System.in));
    }

    public static void printTimeDuration(String description) {
        if (DEBUG) {
            final long now = System.currentTimeMillis();
            System.err.println(description + ":" + (now - lastKnownTime));
            lastKnownTime = now;
        }
    }

}
