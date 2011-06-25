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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import org.Transformer.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class UrlListImporter extends Importer
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String source = null;
    private UrlImporter singleImporter = new UrlImporter();
    private Vector<DataSet> resultCollect = new Vector<DataSet>();

    /**
     *
     */
    public UrlListImporter()
    {
    }

    /**
     * @see org.Transformer.importer.Importer#setSource(java.lang.String)
     */
    @Override
    public final void setSource(final String src)
    {
        source = src;
    }

    /**
     * @see org.Transformer.importer.Importer#importData(org.Transformer.importer.ImportSelector)
     */
    @Override
    public final void importData(final ImportSelector infilt)
    {
        final File srcFile = new File(source);
        if((true == srcFile.exists()) && (true == srcFile.canRead()))
        {
            FileReader fr = null;
            try
            {
                fr = new FileReader(srcFile);
                int r;
                char c;
                StringBuffer sb = new StringBuffer();
                do
                {
                    r = fr.read();
                    if(r != -1)
                    {
                        c = (char)r;
                        if('\n' != c)
                        {
                            sb.append(c);
                        }
                        else
                        {
                            final String line = sb.toString();
                            sb = new StringBuffer(); // Delete all characters
                            if(false == partImport(line, infilt))
                            {
                                setTheData(resultCollect.toArray(new DataSet[1]));
                                return;
                            }
                        }
                    }
                    else
                    {
                        // last line
                        if(0 < sb.length())
                        {
                            final String line = sb.toString();
                            if(false == partImport(line, infilt))
                            {
                                setTheData(resultCollect.toArray(new DataSet[1]));
                                return;
                            }
                        }
                    }
                }while(r != -1);
                setTheData(resultCollect.toArray(new DataSet[1]));
            }
            catch(final FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(final IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(null != fr)
                {
                    try
                    {
                        fr.close();
                    }
                    catch(final IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean partImport(final String line, final ImportSelector infilt)
    {
        log.info("Importing : " + line);
        singleImporter.setSource(line);
        singleImporter.importData(infilt);
        if(true == singleImporter.wasSuccessfull())
        {
            final DataSet[] resPart = singleImporter.getTheData();
            for(int i = 0; i < resPart.length; i++)
            {
                resultCollect.add(resPart[i]);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public final String getConfig()
    {
        return "source = " + source;
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        source = cfg.get("source");
    }

    @Override
    public final String getName()
    {
        return "UrlListImporter";
    }

}
