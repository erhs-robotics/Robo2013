/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erhsroboticsclub.robo2013.utilities;

import edu.wpi.first.wpilibj.networktables2.util.List;
import java.util.Hashtable;

/**
 *
 * @author michael
 */
public class SSJSONParser {
     public static List tokenize(String json) throws Exception {
        List tokens = new List();
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{' || c == '}' || c == ',' || c == '[' || c == ']' || c == ':') {
                tokens.add(String.valueOf(c));
            } else if (c == ' ') {
                //do nothing
            } else if (c == '"' || c == '\'') {//we need to parse a string
                String msg = "";
                if (++i > json.length() - 1) {
                    throw new Exception("Cannot end JSON with a quote!");
                }
                c = json.charAt(i);
                tokens.add("\"");
                while (c != '"' && c != '\'') {
                    msg += c;
                    if (++i > json.length() - 1) {
                        throw new Exception("Could not find terminator quote!");
                    }
                    c = json.charAt(i);
                }

                tokens.add(msg);
            }

        }
        return tokens;
    }   

    public static Hashtable parseJSON(String json) throws Exception {
        int index = 0;
        Hashtable table = new Hashtable();
        List tokens = tokenize(json);
        
        if (!"{".equals(tokens.get(index))) {
            throw new Exception("JSON must start with a '{', not " + tokens.get(index));
        }
        index++;

        for (; index < tokens.size(); index++) {
            Object token = tokens.get(index);
            if (token.equals("\"")) {

                Object key = tokens.get(++index);
                if (!tokens.get(++index).equals(":")) {
                    throw new Exception("Expected Colon, got " + tokens.get(index));
                }
                if (!tokens.get(++index).equals("\"")) {
                    throw new Exception("Expected Quote, got " + tokens.get(index));
                }

                Object value = tokens.get(++index);

                table.put(key, value);

            } else if (token.equals(",") || token.equals("}")) {
                //do nothing
            } else {
                throw new Exception("Unregecognized symbol: " + token);
            }

        }


        return table;
    }
    
}
