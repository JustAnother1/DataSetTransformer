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

import java.util.Map;
import java.util.Vector;


/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class RemoveEmptyLinesFilter extends DataFilter
{

    /**
     *
     */
    public RemoveEmptyLinesFilter()
    {
    }

    public final DataSet[] applyFilterTo(final DataSet[] theData)
    {
        final Vector<DataSet> res = new Vector<DataSet>();
        for(int i = 0; i < theData.length; i++)
        {
            if(0 != theData[i].getNumberOfAtoms())
            {
                res.add(theData[i]);
            }
            // else skip empty Line
        }
        return res.toArray(new DataSet[0]);
    }

    public final String getConfig()
    {
        return "";
    }

    public void setConfig(final Map<String, String> cfg)
    {
    }

    public final String getName()
    {
        return "RemoveEmptyLinesFilter";
    }

}
