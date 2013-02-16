/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erhsroboticsclub.robo2013;

import com.sun.squawk.io.j2me.socket.Protocol;
import edu.wpi.first.wpilibj.networktables2.util.List;
import java.io.*;
import java.*;
import java.util.Hashtable;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import org.erhsroboticsclub.robo2013.utilities.SSJSONParser;
import org.erhsroboticsclub.robo2013.utilities.Target;

public class Com {

    String host;

    Com(String host) {
        this.host = host;
    }

    public String getJSON(String url) throws IOException {
        if (url.startsWith("/") && host.endsWith("/")) {
            url = url.substring(1);
        }
        if (!url.startsWith("/") && !host.endsWith("/")) {
            url = "/" + url;
        }


        DataInputStream in = Connector.openDataInputStream(host + url);
        String value = in.readUTF();
        in.close();

        return value;
    }

    public Hashtable getValues(String url) {
        try {
            String str = getJSON(url);
            Hashtable h = SSJSONParser.parseJSON(str);
            return h;
        } catch (Exception e) {
        }
        return null;
    }
    
    
    
    public List parseTargets(String targets) {
        List parsedTargets = new List();
        //split at '|'
        
        List targetInfo = SSJSONParser.splitString(targets, "|");
        for(int i=0;i<targetInfo.size();i++) {
            String s = (String) targetInfo.get(i);
            List items = SSJSONParser.splitString(s, ",");
            if(items.size() == 3) {
                Target t = new Target();
                t.x = Double.parseDouble((String)items.get(0));
                t.distance = Double.parseDouble((String)items.get(1));
                t.height = Double.parseDouble((String)items.get(2));
                parsedTargets.add(t);
            }
        }
        return parsedTargets;        
    }
}
