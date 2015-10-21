/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public class BackupConnectionTest {

    private Task t;

    @Before
    public void setUp() {
        t = new BackupConnection();
    }

    @After
    public void tearDown() {
        t = null;
    }

    private void testFile(String fName, String input, String output) {
        assertEquals("Wrong solution for " + fName + ".", output, t.eval(input));
    }

    /**
     * Test of eval method, of class BackupConnection.
     */
    @org.junit.Test
    public void testEval() {
        File folder = new File("./src/main/resources/datapub");
        File[] listOfFiles = folder.listFiles();
        Queue<File> inputFiles = new LinkedList();
        Queue<File> outputFiles = new LinkedList();

        for (File f : listOfFiles) {
            if (f.isDirectory()) {
                continue;
            }
            String fName = f.getName();
            if (fName.toLowerCase().contains(".in")) {
                inputFiles.add(f);
            } else if (fName.toLowerCase().contains(".out")) {
                outputFiles.add(f);
            }
        }

        for (File inputFile : inputFiles) {
            try {
                testFile(inputFile.getName(), readFile(inputFile), readFile(outputFiles.poll()));
            } catch (IOException ex) {
                Logger.getLogger(BackupConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String readFile(File f) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }
            return sb.toString();
        }
    }

}
