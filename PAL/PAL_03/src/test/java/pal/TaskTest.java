package pal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
public class TaskTest {

    private Task t;

    @Before
    public void setUp() {
        t = new TreeIsomorphism();
    }

    @After
    public void tearDown() {
        t = null;
    }

    private void testFile(String fName, InputStream is, String output) throws IOException {
        assertEquals("Wrong solution for " + fName + ".", output, t.eval(is));
    }

    /**
     * Test of eval method, of class BackupConnection.
     */
    @org.junit.Test
    public void testEval() {
        File folder = new File("./src/main/resources/datapub");
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);
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
        Collections.sort((List<File>) inputFiles);
        Collections.sort((List<File>) outputFiles);

        for (File inputFile : inputFiles) {
            try {
                this.setUp();
                testFile(inputFile.getName(), new FileInputStream(inputFile), readFile(outputFiles.poll()));
            } catch (IOException ex) {
                Logger.getLogger(TaskTest.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                this.tearDown();
            }
        }
    }

    private String readFile(File f) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            StringBuilder sb = new StringBuilder();
            String prefix = "";
            String line = br.readLine();
            while (line != null) {
                sb.append(prefix).append(line);
                line = br.readLine();
                prefix = "\n";
            }
            return sb.toString();
        }
    }

}
