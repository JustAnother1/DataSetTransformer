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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class JdbcImporter extends Importer
{
    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    /**
     *
     */
    public JdbcImporter()
    {
    }

    @Override
    public final String getConfig()
    {
        return "dbDriver = " + dbDriver
           + "\ndbUrl = " + dbUrl
           + "\ndbUser = "  + dbUser
           + "\ndbPassword = " + dbPassword;
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        dbDriver = cfg.get("dbDriver");
        dbUrl = cfg.get("dbUrl");
        dbUser = cfg.get("dbUser");
        dbPassword = cfg.get("dbPassword");
    }

    @Override
    public final String getName()
    {
        return "JdbcImporter";
    }

    @Override
    public final void setSource(final String src)
    {
        dbUrl = src;
    }

    @Override
    public final void importData(final ImportSelector infilt)
    {
        if((dbDriver == null) || (dbDriver.length() == 0) ||
           (dbUrl == null)    || (dbUrl.length() == 0)       )
        {
            System.err.println("Configuration missing !");
            return;
        }
        Connection cn = null;
        try
        {
            // Select fitting database driver and connect:
            Class.forName(dbDriver);
            cn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            infilt.setDatabaseConnection(cn);
            if(true == infilt.parseToDataSets())
            {
                setTheData(infilt.getTheData());
                setSuccessfullyCompleted(true);
            }
            else
            {
                setSuccessfullyCompleted(false);
            }
        }
        catch(final ClassNotFoundException e)
        {
            e.printStackTrace();
            setSuccessfullyCompleted(false);
        }
        catch(final SQLException e)
        {
            e.printStackTrace();
            setSuccessfullyCompleted(false);
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
}
