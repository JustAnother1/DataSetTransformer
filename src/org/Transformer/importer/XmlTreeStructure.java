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
package org.Transformer.importer;

import java.io.InputStream;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class XmlTreeStructure extends TreeStructure
{

    /**
     * @param src
     */
    public XmlTreeStructure(InputStream src)
    {
        super(src);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String[] getLeafsFor(String string)
    {
        if(false == valid)
        {
            return null;
        }
        else
        {
            // TODO
            return null;
        }
    }

}
