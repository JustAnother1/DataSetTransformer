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

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class ReplaceFilter extends DataFilter
{
    private String elementToChange;
    private String searchFor;
    private String replaceWith;

    /**
     *
     */
    public ReplaceFilter()
    {
    }

    public final String getConfig()
    {
        return "Element to change = " + elementToChange
          + "\nSearch for = " + searchFor
          + "\nReplace with = " + replaceWith;
    }

    public final void setConfig(final Map<String, String> cfg)
    {
        elementToChange = cfg.get("Element to change");
        searchFor = cfg.get("Search for");
        replaceWith = cfg.get("Replace with");
    }

    public final String getName()
    {
        return "ReplaceFilter";
    }

    @Override
    public final DataSet[] applyFilterTo(final DataSet[] theData)
    {
        if(null != elementToChange)
        {
            for(int i = 0; i < theData.length; i++)
            {
                final String curValue = theData[i].getDataAtom(elementToChange);
                if(null == searchFor)
                {
                    if(null ==  curValue)
                    {
                        theData[i].addDataAtom(replaceWith, elementToChange);
                    }
                    // else not matched
                }
                else
                {
                    if(null != curValue)
                    {
                        final String hlp = curValue.replace(searchFor, replaceWith);
                        theData[i].addDataAtom(hlp, elementToChange);
                    }
                    // else does not match
                }
            }
        }
        return theData;
    }

}
