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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Vector;

import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SqlImportSelector extends ImportSelector
{
    private Connection dbConnection;
    private String SqlQuery;

    /**
     *
     */
    public SqlImportSelector()
    {
        super();
    }

    @Override
    public final String getConfig()
    {
        return "SqlQuery = " + SqlQuery;
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        SqlQuery = cfg.get("SqlQuery");
    }

    @Override
    public final String getName()
    {
        return "SqlImportSelector";
    }

    @Override
    public final void setInputStream(final InputStream src)
    {
        // not used
    }

    @Override
    public final void setDatabaseConnection(final Connection newDbConnection)
    {
        dbConnection = newDbConnection;
    }

    @Override
    public final boolean parseToDataSets()
    {
        boolean result = false;
        Statement  st = null;
        ResultSet  rs = null;
        if((null == dbConnection) || (null == SqlQuery))
        {
            return false;
        }
        final Vector<DataSet> res = new Vector<DataSet>();
        try {
          st = dbConnection.createStatement();
          rs = st.executeQuery(SqlQuery);
          // Get meta data:
          final ResultSetMetaData rsmd = rs.getMetaData();
          final int numcol = rsmd.getColumnCount();
          System.out.println("Found " + numcol + " columns in the database !");
          while(true == rs.next())
          {
              final DataSet curRow = new DataSet();
              for(int i = 1; i <= numcol; i++) // Attention: first column with 1 instead of 0
              {
                  System.out.println("Adding : " + rs.getString(i) + " : " + rsmd.getColumnName(i));
                  curRow.addDataAtom(rs.getString(i), rsmd.getColumnName(i));
              }
              res.add(curRow);
          }
          System.out.println("Found " + res.size() + " Data Sets !");
          final DataSet[] hlp = res.toArray(new DataSet[1]);
          System.out.println("Data Set array has " + hlp.length + " entries !");
          setDataSet(hlp);
          result = true;
        }
        catch(final SQLException e)
        {
            e.printStackTrace();
        }
        cleanup(st, rs);
        return result;
    }

    private void cleanup(final Statement st, final ResultSet rs)
    {
        try
        {
            if(rs != null)
            {
                rs.close();
            }
        }
        catch(final SQLException e)
        {
            /* nothing to do*/
        }
        try
        {
            if(st != null)
            {
                st.close();
            }
        }
        catch(final SQLException e)
        {
            /* nothing to do*/
        }
    }

}
