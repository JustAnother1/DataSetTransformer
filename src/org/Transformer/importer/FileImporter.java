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
    protected String FileName = "";

    /**
     *
     */
    public FileImporter()
    {
    }

    /* (non-Javadoc)
     * @see org.Transformer.XmlSerialize#getConfig()
     */
    @Override
    public String getConfig()
    {
        return "FileName = " + FileName;
    }

    /* (non-Javadoc)
     * @see org.Transformer.XmlSerialize#getName()
     */
    @Override
    public String getName()
    {
        return "FileImporter";
    }

    /* (non-Javadoc)
     * @see org.Transformer.importer.Importer#setSource(java.lang.String)
     */
    @Override
    public void setSource(String src)
    {
        if(null != src)
        {
            FileName = src;
        }
    }

    /* (non-Javadoc)
     * @see org.Transformer.importer.Importer#importData(org.Transformer.importer.ImportSelector)
     */
    @Override
    public void importData(ImportSelector infilt)
    {
        try
        {
            File sourceFile = new File(FileName);
            if((false == sourceFile.exists()) || (false == sourceFile.canRead()))
            {
                System.err.println("Could not read Data from " + FileName);
            }
            FileInputStream fin = new FileInputStream(sourceFile);
            System.out.println("Reading Data from " + sourceFile.getAbsolutePath());
            if(true == infilt.parseToDataSets(fin))
            {
                theImportedData = infilt.getTheData();
                ImportSuccessfullyCompleted = true;
            }
            else
            {
                ImportSuccessfullyCompleted = false;
            }
            fin.close();
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
            ImportSuccessfullyCompleted = false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            ImportSuccessfullyCompleted = false;
        }
    }

    @Override
    public void setConfig(Map<String, String> cfg)
    {
        FileName = cfg.get("FileName");
    }

}
