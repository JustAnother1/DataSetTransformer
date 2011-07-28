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
public class AddIndexDataElementFilter extends DataFilter
{
    private String IndexElementName = null;

    /**
     *
     */
    public AddIndexDataElementFilter()
    {
    }

    @Override
    public final String getConfig()
    {
        return "Index element name = " + IndexElementName;
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        IndexElementName = cfg.get("Index element name");
    }

    @Override
    public final String getName()
    {
        return "AddIndexDataElementFilter";
    }

    @Override
    public final DataSet[] applyFilterTo(final DataSet[] theData)
    {
        for(int i = 0; i < theData.length; i++)
        {
            theData[i].addDataAtom("" + i, IndexElementName);
        }
        return theData;
    }

}
