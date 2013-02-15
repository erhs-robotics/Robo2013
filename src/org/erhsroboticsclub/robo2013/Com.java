/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erhsroboticsclub.robo2013;


import com.sun.squawk.io.j2me.socket.Protocol;
import java.io.*;
import java.*;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;


public class Com {
	String host;
        
        Com(String host){ 
            this.host = host;                    
        }
        
        public String getJSON(String url) throws IOException {
            if(!url.startsWith("/")) url = "/" + url;
            
            DataInputStream in = Connector.openDataInputStream("http://10.0.53.23:80" + url);
            String value = in.readUTF();                
            in.close();
            
            return value;           
        }       
       
}
