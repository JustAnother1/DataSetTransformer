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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class FileImporter extends Importer
{
    private String fileName = "";

    /**
     *
     */
    public FileImporter()
    {
    }

    @Override
    public final String getConfig()
    {
        return "FileName = " + fileName;
    }

    @Override
    public final String getName()
    {
        return "FileImporter";
    }

    @Override
    public final void setSource(final String src)
    {
        if(null != src)
        {
            fileName = src;
        }
    }

    @Override
    public final void importData(final ImportSelector infilt)
    {
        try
        {
            final File sourceFile = new File(fileName);
            if((false == sourceFile.exists()) || (false == sourceFile.canRead()))
            {
                System.err.println("Could not read Data from " + fileName);
                setSuccessfullyCompleted(false);
            }
            else
            {
                final FileInputStream fin = new FileInputStream(sourceFile);
                System.out.println("Reading Data from " + sourceFile.getAbsolutePath());
                infilt.setInputStream(fin);
                if(true == infilt.parseToDataSets())
                {
                    setTheData(infilt.getTheData());
                    setSuccessfullyCompleted(true);
                }
                else
                {
                    setSuccessfullyCompleted(false);
                }
                fin.close();
            }
        }
        catch(final MalformedURLException e)
        {
            e.printStackTrace();
            setSuccessfullyCompleted(false);
        }
        catch(final IOException e)
        {
            e.printStackTrace();
            setSuccessfullyCompleted(false);
        }
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        fileName = cfg.get("FileName");
    }

}
