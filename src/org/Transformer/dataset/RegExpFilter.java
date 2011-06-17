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
public class RegExpFilter extends DataFilter
{
    private String regularExpression;
    private String nameOfAtom;

    /**
     *
     */
    public RegExpFilter()
    {
    }

    public final void setRegularExpression(final String newRegularExpression)
    {
        regularExpression = newRegularExpression;
    }

    public final void setNameOfAtom(final String newNameOfAtom)
    {
        nameOfAtom = newNameOfAtom;
    }

    /**
     * @see org.Transformer.dataset.DataFilter#applyFilterTo(org.Transformer.dataset.DataSet[])
     */
    @Override
    public final DataSet[] applyFilterTo(final DataSet[] theData)
    {
        final Vector<DataSet> res = new Vector<DataSet>();
        for(int i = 0; i < theData.length; i++)
        {
            final String fieldValue = theData[i].getDataAtom(nameOfAtom);
            if(null != fieldValue)
            {
                if(true == fieldValue.matches(regularExpression))
                {
                    res.add(theData[i]);
                }
                // else skip not matching Data Set
            }
            // else skip not matching Data Set
        }
        return res.toArray(new DataSet[1]);
    }

    @Override
    public final String getConfig()
    {
        return "NameOfAtom = " + nameOfAtom + "\n"
               + "RegularExpression = " + regularExpression;
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        nameOfAtom = cfg.get("NameOfAtom");
        regularExpression = cfg.get("RegularExpression");
    }

    @Override
    public final String getName()
    {
        return "RegExpFilter";
    }

}
