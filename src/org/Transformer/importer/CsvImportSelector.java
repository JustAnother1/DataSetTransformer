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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Map;
import java.util.Vector;

import org.Transformer.JobUtils;
import org.Transformer.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class CsvImportSelector extends ImportSelector
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String separator = ",";
    private String[] mapping = new String[0];
    private InputStream src;

    /**
     *
     */
    public CsvImportSelector()
    {
        super();
    }

    public final void setMapping(final String[] theMapping)
    {
        mapping = theMapping;
    }

    public final void setSeperator(final String theSeperator)
    {
        separator = theSeperator;
    }


    /**
     * @see org.Transformer.importer.ImportFilter#parseToDataSets(java.io.InputStream)
     */
    @Override
    public final boolean parseToDataSets()
    {
        final Vector<DataSet> res = new Vector<DataSet>();
        final InputStreamReader in = new InputStreamReader(src);
        StringBuffer sb = new StringBuffer();
        int r;
        try
        {
            do{
                r = in.read();
                if(-1 != r)
                {
                    final char c = (char)r;
                    if(c == '\n')
                    {
                        // end of Line reached
                        final String line = sb.toString();
                        final DataSet LineSet = parseLineToDataSet(line);
                        if(null != LineSet)
                        {
                            res.add(LineSet);
                        }
                        sb = new StringBuffer();
                    }
                    else
                    {
                        sb.append(c);
                    }
                }
            }while(-1 != r);
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
        // end of Line reached
        final String line = sb.toString();
        final DataSet LineSet = parseLineToDataSet(line);
        if(null != LineSet)
        {
            res.add(LineSet);
        }

        setDataSet(res.toArray(new DataSet[1]));
        return true;
    }


    private DataSet parseLineToDataSet(final String line)
    {
        log.debug("Line : " + line);
        final String[] Values = line.split(separator);
        log.debug("Split with Separator : " + separator + " !");
        for(int i = 0; i < Values.length; i++)
        {
            log.debug("Atom " + i + " : " + Values[i]);
        }
        final DataSet row = new DataSet();
        log.debug("mapping.length : " + mapping.length);
        for(int i = 0; i < mapping.length; i++)
        {
            log.debug("Mapping " + i + " : " + mapping[i]);
        }
        int maxLength;
        if(mapping.length > Values.length)
        {
            maxLength = Values.length;
        }
        else
        {
            maxLength = mapping.length;
        }
        for(int j = 0; j < maxLength; j++)
        {
            final String Name = mapping[j];
            if(null != Name)
            {
                row.addDataAtom(Values[j], Name);
            }
            // else this field is not mapped
        }
        return row;
    }

    @Override
    public final String getConfig()
    {
        return "separator = " + separator + "\n"
               + JobUtils.getConfigTextFor(mapping, "mapping");
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        mapping = JobUtils.getStringArrayFromSettingMap(cfg, "mapping");
        separator = cfg.get("separator");
    }

    @Override
    public final String getName()
    {
        return "CsvImportSelector";
    }

    @Override
    public final void setInputStream(final InputStream newSrc)
    {
        src = newSrc;
    }

    @Override
    public final void setDatabaseConnection(final Connection dbconnection)
    {
        // not possible
    }

}
