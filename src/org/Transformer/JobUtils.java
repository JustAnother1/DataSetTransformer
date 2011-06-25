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
}
