package pal;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public interface Task {

    public String eval(InputStream is) throws IOException;

}
