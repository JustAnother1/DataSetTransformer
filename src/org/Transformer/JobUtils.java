/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/>
 *
 */

package org.Transformer;

import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public final class JobUtils
{
    private JobUtils()
    {
    }

    public static String getConfigTextFor(final String[] arr, final String Name)
    {
        final StringBuffer sb = new StringBuffer();
        for(int i = 0; i < arr.length; i++)
        {
            sb.append(Name + "_" + i + " = " + arr[i] + "\n");
        }
        return sb.toString();
    }

    public static String[] getStringArrayFromSettingMap(final Map<String, String> cfg, final String Name)
    {
        final Vector<String> vec = new Vector<String>();
        String val = cfg.get(Name + "_0");
        int i = 0;
        while(val != null)
        {
            vec.add(val);
            i++;
            val = cfg.get(Name + "_" + i);
        }
        return vec.toArray(new String[0]);
    }

    public static String[] stringToArray(final String text)
    {
        final Scanner sc = new Scanner(text);
        final Vector<String> vec = new Vector<String>();
        while(sc.hasNextLine())
        {
            vec.add(sc.nextLine());
        }
        sc.close();
        return vec.toArray(new String[0]);
    }

    private static boolean isValidChar(final char c)
    {
        final char[] validChars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                                   'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                                   'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                                   'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                                   '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for(int i = 0; i < validChars.length; i++)
        {
            if(c == validChars[i])
            {
                return true;
            }
        }
        return false;
    }

    public static String onlyAllowedChars(final String src)
    {
        final StringBuffer dst = new StringBuffer();
        for(int i = 0; i < src.length(); i++)
        {
            final char cur = src.charAt(i);
            if(true == isValidChar(cur))
            {
                dst.append(cur);
            }
        }
        return dst.toString();
    }
}
