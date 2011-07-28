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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class OneFilePerRowExporter extends Exporter
{
    private boolean wasSuccessfully = false;

    private String FileNamePrefix = "";
    private String FileNamePostfix = "";
    private String FileNameDataElementName = null;
    /**
     *
     */
    public OneFilePerRowExporter()
    {
    }

    @Override
    public final String getConfig()
    {
        return "filename prefix = " + FileNamePrefix +
               "filename postfix = " + FileNamePostfix +
               "filename data element = " + FileNameDataElementName;
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        FileNamePrefix = cfg.get("filename prefix");
        FileNamePostfix = cfg.get("filename postfix");
        FileNameDataElementName = cfg.get("filename data element");
    }

    @Override
    public final String getName()
    {
        return "OneFilePerRowExporter";
    }

    private boolean exportOneRow(final DataSet[] theData, final ExportStyle expStyle, final String FileName)
    {
        boolean result = false;
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(FileName);
            expStyle.setOutputStream(out);
            if(true == expStyle.formatTheData(theData))
            {
                out.flush();
                result = true;
            }
            else
            {
                result = false;
            }
        }
        catch(final FileNotFoundException e)
        {
            e.printStackTrace();
            result = false;
        }
        catch(final IOException e)
        {
            e.printStackTrace();
            result = false;
        }
        catch(final SQLException e)
        {
            e.printStackTrace();
            result = false;
        }
        if(null != out)
        {
            try
            {
                out.close();
            }
            catch(final IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public final void export(final DataSet[] theData, final ExportStyle expStyle)
    {
        if((null == FileNameDataElementName) || (null == theData))
        {
            wasSuccessfully = false;
            return;
        }
        // else
        for(int i = 0; i < theData.length; i++)
        {
            final DataSet[] rowData = new DataSet[1];
            rowData[0] = theData[i];
            final String curName = FileNamePrefix + theData[i].getDataAtom(FileNameDataElementName) + FileNamePostfix;
            if(false == exportOneRow(rowData, expStyle, curName))
            {
                wasSuccessfully = false;
                return;
            }
        }
        wasSuccessfully = true;
    }

    @Override
    public final boolean wasSuccessfull()
    {
        return wasSuccessfully;
    }

}
