/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pal;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Michal Stanke <michal.stanke@mikk.cz>
 */
public interface Task {

    public String eval(InputStream stdin) throws IOException;

}
