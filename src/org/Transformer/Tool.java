/**
 *
 */
package org.Transformer;

import java.util.Scanner;
import java.util.Vector;

/**
 * @author lars
 *
 */
public class Tool
{

    /**
     *
     */
    public Tool()
    {
    }

    public static String[] StringToArray(String text)
    {
        Scanner sc = new Scanner(text);
        Vector<String> vec = new Vector<String>();
        while(sc.hasNextLine())
        {
            vec.add(sc.nextLine());
        }
        return vec.toArray(new String[1]);
    }

}
