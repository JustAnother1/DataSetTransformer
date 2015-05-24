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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.Transformer.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class JdbcExporter extends Exporter
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private boolean jdbcWasSuccessfull = false;

    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public JdbcExporter()
    {
    }

    public final String getConfig()
    {
        return "dbDriver = " + dbDriver
           + "\ndbUrl = " + dbUrl
           + "\ndbUser = "  + dbUser
           + "\ndbPassword = " + dbPassword;
    }

    public final void setConfig(final Map<String, String> cfg)
    {
        dbDriver = cfg.get("dbDriver");
        dbUrl = cfg.get("dbUrl");
        dbUser = cfg.get("dbUser");
        dbPassword = cfg.get("dbPassword");
    }

    public final String getName()
    {
        return "JdbcExporter";
    }

    @Override
    public final void export(final DataSet[] theData, final ExportStyle expStyle)
    {
        if((dbDriver == null) || (dbDriver.length() == 0) ||
           (dbUrl == null)    || (dbUrl.length() == 0)       )
        {
            log.error("Configuration missing !");
            log.info("Driver : " + dbDriver);
            log.info("URL : " + dbUrl);
            return;
        }
        Connection cn = null;
        try
        {
            // Select fitting database driver and connect:
            Class.forName(dbDriver);
            cn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            expStyle.setDatabaseConnection(cn);
            if(true == expStyle.formatTheData(theData))
            {
                jdbcWasSuccessfull = true;
            }
            else
            {
                jdbcWasSuccessfull = false;
            }
        }
        catch(final ClassNotFoundException e)
        {
            log.error("Driver is invalid !\nDriver was " + dbDriver + " !\n");
            jdbcWasSuccessfull = false;
        }
        catch(final SQLException e)
        {
            e.printStackTrace();
            jdbcWasSuccessfull = false;
        }
        catch(final IOException e)
        {
            e.printStackTrace();
            jdbcWasSuccessfull = false;
        }
        finally
        {
            if(cn != null)
            {
                try
                {
                    cn.close();
                }
                catch(final SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public final boolean wasSuccessfull()
    {
        return jdbcWasSuccessfull;
    }

}
