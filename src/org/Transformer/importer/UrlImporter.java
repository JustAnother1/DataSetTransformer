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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.Transformer.Translator;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class UrlImporter extends Importer
{
    protected String SourceUrl = "";
    private JTextField url;

    /**
     *
     */
    public UrlImporter()
    {
    }

    public void setSource(String src)
    {
        if(null != src)
        {
            SourceUrl = src;
        }
    }

    private boolean isValidChar(final char c)
    {
        final char[] validChars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                                   'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                                   'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                                   'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                                   '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for(int i = 0; i < validChars.length; i++)
        {
            if(c == validChars[i])
            {
                return true;
            }
        }
        return false;
    }

    private String onlyAllowedChars(final String src)
    {
        final StringBuffer dst = new StringBuffer();
        for(int i = 0; i < src.length(); i++)
        {
            final char cur = src.charAt(i);
            if(true == isValidChar(cur))
            {
                dst.append(cur);
            }
        }
        return dst.toString();
    }

    /**
     * Copy the content of the input stream into the output stream, using a temporary
     * byte array buffer whose size is 4 * 1024.
     *
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     *
     * @throws IOException If any error occurs during the copy.
     */
    private static void copy(InputStream in, OutputStream out) throws IOException
    {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1)
        {
            out.write(b, 0, read);
        }
    }


    public void importData(ImportSelector infilt)
    {
        try
        {
            // already cached ?
            String cacheName = "DataTransformer_cache" + onlyAllowedChars(SourceUrl) + ".dtc";
            File cacheFile = new File(cacheName);
            if(false == cacheFile.exists())
            {
                System.out.println("Getting Source Data from " + SourceUrl);
                // not cached load the file
                URL src = new URL(SourceUrl);
                InputStream is = src.openStream();
                FileOutputStream cache = new FileOutputStream(cacheName);
                copy(is, cache);
                is.close();
                cache.flush();
                cache.close();
            }
            else
            {
                System.out.println("Using cached Data for " + SourceUrl);
            }
            FileInputStream fin = new FileInputStream(cacheFile);
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
            System.err.println("Could not parse the URL : " + SourceUrl);
            //e.printStackTrace();
            ImportSuccessfullyCompleted = false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            ImportSuccessfullyCompleted = false;
        }
    }

    @Override
    public String getConfig()
    {
        return "SourceUrl = " + SourceUrl;
    }

    @Override
    public void setConfig(Map<String, String> cfg)
    {
        SourceUrl = cfg.get("SourceUrl");
    }

    @Override
    public String getName()
    {
        return "UrlImporter";
    }

    @Override
    public Component getComponent()
    {
        JPanel slide = new JPanel();
        slide.setLayout(new BoxLayout(slide, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("URL :");
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
            SourceUrl = url.getText();
        }
    }

    @Override
    public void updateLanguage(Translator newMsg)
    {
        // TODO Auto-generated method stub

    }

}
