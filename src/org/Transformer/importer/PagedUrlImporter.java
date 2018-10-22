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

import java.util.Map;
import java.util.Vector;

import org.Transformer.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class PagedUrlImporter extends BaseUrlImporter
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public PagedUrlImporter()
    {
    }

    public final void setConfig(final Map<String, String> cfg)
    {
        setSource(cfg.get("SourceUrl"));
    }

    public final String getConfig()
    {
        return "SourceUrl = " + getSource();
    }

    public final void importData(final ImportSelector infilt)
    {
        final Vector<DataSet> res = new Vector<DataSet>();
        String nextPageUrl = getSource();
        do
        {
            // Parse the Page
            log.debug("Importing Page : " + nextPageUrl);
            setSource(nextPageUrl);
            baseImportData(infilt);
            if(false == super.wasSuccessfull())
            {
                setSuccessfullyCompleted(false);
                return;
            }
            // copy the data
            final DataSet[] curRes = super.getTheData();
            for(int i = 0; i < curRes.length; i++)
            {
                res.add(curRes[i]);
            }
            // has more pages ?
            log.debug("Searching for next Page...");
            nextPageUrl = "";
            for(int i = 0; i < curRes.length; i++)
            {
                String curNext = curRes[i].getDataAtom("Next");
                if(null != curNext)
                {
                    if(0 < curNext.length())
                    {
                        if(curNext.startsWith("//"))
                        {
                            // https is missing -> Browsers fix that
                            // -> so let us do the same
                            curNext = "https:" + curNext;
                        }
                        log.debug("Next Atom is : {}", curNext);
                        nextPageUrl = curNext;
                        break;
                    }
                    else
                    {
                        log.debug("Next Atom is empty !");
                    }
                }
                else
                {
                    log.debug("Entry has no Next Atom !");
                }
            }
        } while(0 < nextPageUrl.length());
        log.debug("No more pages found !");
        setTheData(res.toArray(new DataSet[0]));
    }

    public final String getName()
    {
        return "PagedUrlImporter";
    }

}
