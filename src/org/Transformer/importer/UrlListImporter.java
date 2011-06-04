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

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.Transformer.Translator;
import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class UrlListImporter extends Importer
{
    private String source = null;
    private UrlImporter singleImporter = new UrlImporter();
    private Vector<DataSet> resultCollect = new Vector<DataSet>();
    private JTextField url;

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
    public void setSource(String src)
    {
        source = src;
    }

    /**
     * @see org.Transformer.importer.Importer#importData(org.Transformer.importer.ImportSelector)
     */
    @Override
    public void importData(ImportSelector infilt)
    {
        File srcFile = new File(source);
        if((true == srcFile.exists()) && (true == srcFile.canRead()))
        {
            try
            {
                FileReader fr = new FileReader(srcFile);
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
                            String line = sb.toString();
                            sb = new StringBuffer(); // Delete all characters
                            if(false == partImport(line, infilt))
                            {
                                theImportedData = resultCollect.toArray(new DataSet[1]);
                                return;
                            }
                        }
                    }
                    else
                    {
                        // last line
                        if(0 < sb.length())
                        {
                            String line = sb.toString();
                            if(false == partImport(line, infilt))
                            {
                                theImportedData = resultCollect.toArray(new DataSet[1]);
                                return;
                            }
                        }
                    }
                }while(r != -1);
                theImportedData = resultCollect.toArray(new DataSet[1]);
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean partImport(String line, ImportSelector infilt)
    {
        System.out.println("Importing : " + line);
        singleImporter.setSource(line);
        singleImporter.importData(infilt);
        if(true == singleImporter.ImportSuccessfullyCompleted)
        {
            DataSet[] resPart = singleImporter.getTheData();
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
    public String getConfig()
    {
        return "source = " + source;
    }

    @Override
    public void setConfig(Map<String, String> cfg)
    {
        source = cfg.get("source");
    }

    @Override
    public String getName()
    {
        return "UrlListImporter";
    }

    @Override
    public Component getComponent()
    {
        JPanel slide = new JPanel();
        slide.setLayout(new BoxLayout(slide, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("URL of URL List :");
        slide.add(desc);
        url = new JTextField();
        slide.add(url);
        slide.add(Box.createVerticalGlue());
        return slide;
    }

    @Override
    public void actionAfterShow()
    {
    }

    @Override
    public void actionOnClose()
    {
        if(null != url)
        {
            source = url.getText();
        }
    }

    @Override
    public void updateLanguage(Translator newMsg)
    {
        // TODO Auto-generated method stub

    }

}
