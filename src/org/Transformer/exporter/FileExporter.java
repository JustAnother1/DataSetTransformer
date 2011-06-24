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
public class FileExporter extends Exporter
{
    private boolean exportSuccessfullyExecuted = false;
    private String target = "";

    /**
     *
     */
    public FileExporter()
    {
    }

    public final void export(final DataSet[] theData, final ExportStyle expStyle)
    {
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(target);
            expStyle.setOutputStream(out);
            if(true == expStyle.formatTheData(theData))
            {
                out.flush();
                exportSuccessfullyExecuted = true;
                System.out.println("Export successfully !");
            }
            else
            {
                System.out.println("Export failed !");
            }
        }
        catch(final FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Export failed !");
            exportSuccessfullyExecuted = false;
        }
        catch(final IOException e)
        {
            e.printStackTrace();
            System.out.println("Export failed !");
            exportSuccessfullyExecuted = false;
        }
        catch(final SQLException e)
        {
            e.printStackTrace();
            System.out.println("Export failed !");
            exportSuccessfullyExecuted = false;
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
    }

    @Override
    public final boolean wasSuccessfull()
    {
        return exportSuccessfullyExecuted;
    }

    public final void setTarget(final String trg)
    {
        if(null != trg)
        {
            target = trg;
        }
    }


    @Override
    public final String getConfig()
    {
        return "target = " + target;
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        target = cfg.get("target");
    }

    @Override
    public final String getName()
    {
        return "FileExporter";
    }

}
