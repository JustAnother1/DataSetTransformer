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
import java.util.Vector;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class JobUtils
{

    public static String getConfigTextFor(String[] arr, String Name)
    {
        String res = "";
        for(int i = 0; i < arr.length; i++)
        {
            res = res + Name + "_" + i + " = " + arr[i] + "\n";
        }
        return res;
    }

    public static String[] getStringArrayFromSettingMap(Map<String, String> cfg, String Name)
    {
        Vector<String> vec = new Vector<String>();
        String val = cfg.get(Name + "_0");
        int i = 0;
        while(val != null)
        {
            vec.add(val);
            i++;
            val = cfg.get(Name + "_" + i);
        }
        return vec.toArray(new String[1]);
    }
}
