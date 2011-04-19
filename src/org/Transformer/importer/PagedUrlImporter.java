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

import java.util.Vector;

import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class PagedUrlImporter extends UrlImporter
{
    /**
     *
     */
    public PagedUrlImporter()
    {
    }

    public void importData(ImportSelector infilt)
    {
        Vector<DataSet> res = new Vector<DataSet>();
        String nextPageUrl = SourceUrl;
        do
        {
            // Parse the Page
            System.out.println("Importing Page : " + nextPageUrl);
            super.setSource(nextPageUrl);
            super.importData(infilt);
            if(false == super.wasSuccessfull())
            {
                ImportSuccessfullyCompleted = false;
                return;
            }
            // has more pages ?
            nextPageUrl = "";
            DataSet[] curRes = super.getTheData();
            for(int i = 0; i < curRes.length; i++)
            {
                res.add(curRes[i]);
            }
            for(int i = 0; i < curRes.length; i++)
            {
                String curNext = curRes[i].getDataAtom("Next");
                if(null != curNext)
                {
                    if(0 < curNext.length())
                    {
                        nextPageUrl = curNext;
                        break;
                    }
                }
            }
        } while(0 < nextPageUrl.length());
        theImportedData = res.toArray(new DataSet[1]);
    }

    @Override
    public String getName()
    {
        return "PagedUrlImporter";
    }

}
