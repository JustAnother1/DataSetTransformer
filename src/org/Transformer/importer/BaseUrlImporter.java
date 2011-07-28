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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.Transformer.JobUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public abstract class BaseUrlImporter extends Importer
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String sourceUrl = "";

    public final void setSource(final String src)
    {
        if(null != src)
        {
            sourceUrl = src;
        }
    }

    public final String getSource()
    {
        return sourceUrl;
    }

    protected final void baseImportData(final ImportSelector infilt)
    {
        try
        {
            // already cached ?
            final File cacheFolder = new File("cache");
            if(false == cacheFolder.isDirectory())
            {
                if(false == cacheFolder.mkdir())
                {
                    log.error("Could not create cache folder !");
                }
            }
            final String cacheName = "cache/DataTransformer_cache" + JobUtils.onlyAllowedChars(sourceUrl) + ".dtc";
            final File cacheFile = new File(cacheName);

            if(false == cacheFile.exists())
            {
                log.debug("Getting Source Data from " + sourceUrl);
                // not cached load the file
                final URL src = new URL(sourceUrl);
                final InputStream is = src.openStream();
                final FileOutputStream cache = new FileOutputStream(cacheName);
                copy(is, cache);
                is.close();
                cache.flush();
                cache.close();
            }
            else
            {
                log.debug("Using cached Data for " + sourceUrl);
            }
            final FileInputStream fin = new FileInputStream(cacheFile);
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
        catch(final MalformedURLException e)
        {
            log.error("Could not parse the URL : " + sourceUrl);
            //e.printStackTrace();
            setSuccessfullyCompleted(false);
        }
        catch(final IOException e)
        {
            e.printStackTrace();
            setSuccessfullyCompleted(false);
        }
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
    private static void copy(final InputStream in, final OutputStream out) throws IOException
    {
        final byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1)
        {
            out.write(b, 0, read);
        }
    }

}
