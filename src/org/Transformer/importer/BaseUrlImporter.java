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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

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

    private URLConnection checkConnection(URL src) throws IOException
    {
        URLConnection con = src.openConnection();

        if(con instanceof HttpURLConnection)
        {
            HttpURLConnection conn = (HttpURLConnection)con;
            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
            {
                if(   status == HttpURLConnection.HTTP_MOVED_TEMP
                   || status == HttpURLConnection.HTTP_MOVED_PERM
                   || status == HttpURLConnection.HTTP_SEE_OTHER   )
                {
                    // get redirect url from "location" header field
                    String newUrl = conn.getHeaderField("Location");
                    // open the new connection again
                    log.debug("Redirect to URL : " + newUrl);
                    src =  new URL(newUrl);
                    con = checkConnection(src);
                }
            }
        }

        // to debug https use the VM argument : " -Djavax.net.debug=all "
        if(con instanceof HttpsURLConnection)
        {
            HttpsURLConnection scon = (HttpsURLConnection)con;
            print_https_cert(scon);
        }
        con.setConnectTimeout(20000); // in ms
        con.setReadTimeout(30000); // in ms
        return con;
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
                URL src = new URL(sourceUrl);
                URLConnection con = checkConnection(src);
                con.connect();
                final InputStream is = con.getInputStream();
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

    private void print_https_cert(HttpsURLConnection con)
    {
        if(con!=null)
        {
            try
            {
                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for(Certificate cert : certs)
                {
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                                                 + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                                                 + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }
            }
            catch (SSLPeerUnverifiedException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
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
    private void copy(final InputStream in, final OutputStream out) throws IOException
    {
        long length = 0;
        final byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1)
        {
            length = length + read;
            out.write(b, 0, read);
        }
        log.debug("Copied " + length + " bytes !");
    }

}
