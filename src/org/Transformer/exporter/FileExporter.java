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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class FileExporter extends Exporter
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
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
                log.info("Export successfully !");
            }
            else
            {
                log.error("Export failed !");
            }
        }
        catch(final FileNotFoundException e)
        {
            e.printStackTrace();
            log.error("Export failed !");
            exportSuccessfullyExecuted = false;
        }
        catch(final IOException e)
        {
            e.printStackTrace();
            log.error("Export failed !");
            exportSuccessfullyExecuted = false;
        }
        catch(final SQLException e)
        {
            e.printStackTrace();
            log.error("Export failed !");
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

    public final String getConfig()
    {
        return "target = " + target;
    }

    public final void setConfig(final Map<String, String> cfg)
    {
        target = cfg.get("target");
    }

    public final String getName()
    {
        return "FileExporter";
    }

}
