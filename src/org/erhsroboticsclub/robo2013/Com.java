/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erhsroboticsclub.robo2013;


import com.sun.squawk.io.j2me.socket.Protocol;
import java.io.*;
import java.*;
import javax.microedition.io.Connection;


public class Com{
	Protocol socket;	
        Connection connection; 	
        
        Com(String host){            
            socket = new Protocol();
            try {
                connection =  socket.open("http", host, 0, true);                                
            } catch (IOException ex) {
                ex.printStackTrace();
            }        
        }
        
        public String getMessage() throws IOException {
            DataInputStream in = socket.openDataInputStream();
            String value = in.readUTF();
            in.close();
            return value;
        }
}
