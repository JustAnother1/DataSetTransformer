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
    private HashMap<String, String> data = new HashMap<String, String>();

    /**
     *
     */
    public DataSet()
    {
    }

    /** adds or replaces(updates) the data element.
     *
     * @param da Value of data element.
     * @param Name Name of the Data element.
     */
    public final void addDataAtom(final String da, final String Name)
    {
        if((null != da) && (null != Name))
        {
            data.put(Name, da);
        }
    }

    /** retrieves a data element.
     *
     * @param Name Name of data element
     * @return the requested data element or null
     */
    public final String getDataAtom(final String Name)
    {
        return data.get(Name);
    }

    public final String[] getNamesOfAllDataAtoms()
    {
        final Set<String> hlp = data.keySet();
        return hlp.toArray(new String[0]);
    }

    public final int getNumberOfAtoms()
    {
        return data.size();
    }
}
