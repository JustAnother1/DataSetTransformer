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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.Transformer.JobUtils;
import org.Transformer.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SqlExportStyle extends ExportStyle
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
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

    public final String getConfig()
    {
        return "tableName = " + dbTable +
             "\nfieldNames = " + FieldNames +
             JobUtils.getConfigTextFor(mapping, "mapping");
    }

    public final void setConfig(final Map<String, String> cfg)
    {
        mapping = JobUtils.getStringArrayFromSettingMap(cfg, "mapping");
        dbTable = cfg.get("tableName");
        FieldNames = cfg.get("fieldNames");
    }

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
          String sql = "insert into " + dbTable + "(" + FieldNames + ") VALUES(";
          for(int i = 0; i < mapping.length -1; i++)
          {
              sql = sql + "? , ";
          }
          // last value
          sql = sql + "? );";
          log.debug("Prepared Statement : {}", sql);
          final PreparedStatement ps = dbConnection.prepareStatement (sql);
          for(int k = 0; k < theData.length; k++)
          {
              for(int i = 0; i < mapping.length; i++)
              {
                  String hlp = theData[k].getDataAtom(mapping[i]);
                  if(null == hlp)
                  {
                      hlp = "";
                  }
                  ps.setString(i + 1, hlp); // SQL starts with 1 instead of 0 !
              }
              ps.executeUpdate();
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
