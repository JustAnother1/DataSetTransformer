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
import java.util.Map;

import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class FileExporter extends Exporter
{
    private boolean ExportSuccessfullyExecuted = false;
    private String target = "";

    /**
     *
     */
    public FileExporter()
    {
    }

    public void export(DataSet[] theData, ExportStyle expStyle)
    {
        FileOutputStream out;
        try
        {
            out = new FileOutputStream(target);
            if(true == expStyle.formatTheData(theData, out))
            {
                out.flush();
                ExportSuccessfullyExecuted = true;
                System.out.println("Export successfully !");
            }
            else
            {
                System.out.println("Export failed !");
            }
            out.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Export failed !");
            ExportSuccessfullyExecuted = false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Export failed !");
            ExportSuccessfullyExecuted = false;
        }
    }

    @Override
    public boolean wasSuccessfull()
    {
        return ExportSuccessfullyExecuted;
    }

    public void setTarget(String trg)
    {
        if(null != trg)
        {
            target = trg;
        }
    }


    @Override
    public String getConfig()
    {
        return "target = " + target;
    }

    @Override
    public void setConfig(Map<String, String> cfg)
    {
        target = cfg.get("target");
    }

    @Override
    public String getName()
    {
        return "FileExporter";
    }

}
