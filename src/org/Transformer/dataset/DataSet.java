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

/**
 *
 */
package org.Transformer.dataset;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class DataSet
{
    private HashMap<String, String> Data = new HashMap<String, String>();

    /**
     *
     */
    public DataSet()
    {
    }

    public void addDataAtom(String da, String Name)
    {
        if((null != da) && (null != Name))
        {
            Data.put(Name, da);
        }
    }

    public String getDataAtom(String Name)
    {
        return Data.get(Name);
    }

    public String[] getNamesOfAllDataAtoms()
    {
        Set<String> hlp = Data.keySet();
        return hlp.toArray(new String[1]);
    }

    public int getNumberOfAtoms()
    {
        return Data.size();
    }
}
