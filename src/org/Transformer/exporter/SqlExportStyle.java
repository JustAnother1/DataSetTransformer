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
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.Transformer.JobUtils;
import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SqlExportStyle extends ExportStyle
{
    private Connection dbConnection;
    private String dbTable;
    private String FieldNames;
    private String[] mapping;

    /**
     *
     */
    public SqlExportStyle()
    {
    }

    @Override
    public final String getConfig()
    {
        return "tableName = " + dbTable +
             "\nfieldNames = " + FieldNames +
             JobUtils.getConfigTextFor(mapping, "mapping");
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        mapping = JobUtils.getStringArrayFromSettingMap(cfg, "mapping");
        dbTable = cfg.get("tableName");
        FieldNames = cfg.get("fieldNames");
    }

    @Override
    public final String getName()
    {
        return "SqlExportStyle";
    }

    @Override
    public final void setOutputStream(final OutputStream out)
    {
        // not needed
    }

    @Override
    public final void setDatabaseConnection(final Connection newDbConnection)
    {
        dbConnection = newDbConnection;
    }

    @Override
    public final boolean formatTheData(final DataSet[] theData) throws IOException, SQLException
    {
        boolean result = false;
        Statement  st = null;
        if(null == dbConnection)
        {
            return false;
        }
        try {
          st = dbConnection.createStatement();
          for(int k = 0; k < theData.length; k++)
          {
              String sql = "insert into " + dbTable + "(" + FieldNames + ") VALUES(";
              for(int i = 0; i < mapping.length -1; i++)
              {
                  sql = sql + "'" + theData[k].getDataAtom(mapping[i]) + "' , ";
              }
              // last value
              sql = sql + "'" + theData[k].getDataAtom(mapping[mapping.length -1]) + "');";
              System.out.println(sql);
              st.executeUpdate(sql);
          }

          result = true;
        }
        catch(final SQLException e)
        {
            e.printStackTrace();
            throw e;
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
        return result;
    }

}
