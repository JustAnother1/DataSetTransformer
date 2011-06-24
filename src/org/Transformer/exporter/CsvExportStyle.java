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
package org.Transformer.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import org.Transformer.JobUtils;
import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class CsvExportStyle extends ExportStyle
{
    private String[] mapping;
    private String separator = ",";
    private OutputStream out = null;

    /**
     *
     */
    public CsvExportStyle()
    {
    }

    public final void setSeperator(final String theSeperator)
    {
        separator = theSeperator;
    }

    public final void setMapping(final String[] theMapping)
    {
        mapping = theMapping;
    }

    /**
     * @see org.Transformer.exporter.ExportStyle#formatTheData(org.Transformer.dataset.DataSet[], java.io.OutputStream)
     */
    @Override
    public final boolean formatTheData(final DataSet[] theData) throws IOException
    {
        if((null == mapping) || (null == theData) || (null == out))
        {
            return false;
        }
        for(int i = 0; i < theData.length; i++)
        {
            final StringBuffer sb = new StringBuffer();
            final DataSet curSet = theData[i];
            if(null != curSet)
            {
                int maxLength;
                if(mapping.length > curSet.getNumberOfAtoms())
                {
                    maxLength = curSet.getNumberOfAtoms();
                }
                else
                {
                    maxLength = mapping.length;
                }
                for(int j = 0; j < maxLength; j++)
                {
                    final Object val = curSet.getDataAtom(mapping[j]);
                    if(null != val)
                    {
                        sb.append(val.toString() + separator);
                    }
                }
            }
            String line = sb.toString();
            line = line + "\n";
            out.write(line.getBytes());
        }
        return true;
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
        return "CsvExportStyle";
    }

    @Override
    public final void setOutputStream(final OutputStream newOut)
    {
        out = newOut;
    }

    @Override
    public final void setDatabaseConnection(final Connection dbconnection)
    {
        // not possible
    }

}
