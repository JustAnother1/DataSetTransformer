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
import java.util.Map;

import org.Transformer.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class HtmlExportStyle extends ExportStyle
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private OutputStream out;
    /**
     * Structure of the HTML File before and After the Row data. Row Data is inserted at the Point labeled with %ROW%.
     * Simple Example: "<html><head></head><body>%ROW%</body></html>"
     */
    private String globalStyleDefinition = "";
    /**
     * Formating of the Row Data. consist of pairs of Description and Variable name.
     * If the Variables in the DataSet have the Names vari1 vari2 and var3 then the following styles are valid:
     * "%vari1", "Value of Variable 1 : %vari1%Value of Variable 2 : %vari2%Value of Variable 3 : %vari3"
     */
    private String rowStyleDefinition = "";

    /**
     *
     */
    public HtmlExportStyle()
    {
    }

    private boolean formatARow(final DataSet theData) throws IOException
    {
        final String usedRowStyle = rowStyleDefinition;
        if(null == usedRowStyle)
        {
            log.error("Row Style Definition is Null !");
            return false;
        }
        final String[] parts = usedRowStyle.split("%", -1);
        for(int i = 0; i < parts.length; i++)
        {
            if(0 == i%2)
            {
                // Text
                out.write(parts[i].getBytes()); // TODO charset ?
            }
            else
            {
                // Variable
                final Object variable = theData.getDataAtom(parts[i]);
                if(null == variable)
                {
                    log.error("Could not resolve Variable ! (" + parts[i] + ") !");
                    return false;
                }
                out.write(variable.toString().getBytes());// TODO charset ?
            }
        }
        return true;
    }

    @Override
    public final boolean formatTheData(final DataSet[] theData) throws IOException
    {
        final String usedStyle = globalStyleDefinition;
        if(null == usedStyle)
        {
            log.error("Global Style Definition is Null !");
            return false;
        }
        log.debug("Using Global Style : " + usedStyle);
        final String[] globalParts = usedStyle.split("%", 3);
        for(int i = 0; i < globalParts.length; i++)
        {
            log.debug("globalParts[" + i + "] = " + globalParts[i]);
        }
        // Global Prefix
        out.write(globalParts[0].getBytes()); // TODO charset ?
        if(globalParts.length > 1)
        {
            if(false == "ROW".equalsIgnoreCase(globalParts[1]))
            {
                log.error("Global Style Definition invalid ! (" + globalParts[1] + ") !");
                return false;
            }
        }
        for(int g = 0; g < theData.length; g++)
        {
            if(false == formatARow(theData[g]))
            {
                log.error("Row output failed at row " + g + " !");
                return false;
            }
        }

        if(globalParts.length > 2)
        {
            // Global Postfix
            out.write(globalParts[2].getBytes()); // TODO charset ?
        }
        return true;
    }


    public final void setStyleDefinition(final String globaleDef, final String RowDef)
    {
        if(null != globaleDef)
        {
            globalStyleDefinition = globaleDef;
        }
        if(null != RowDef)
        {
            rowStyleDefinition = RowDef;
        }
    }
    @Override
    public final String getConfig()
    {
        return "GlobalStyleDefinition = " + globalStyleDefinition + "\n"
               + "RowStyleDefinition = " + rowStyleDefinition;
    }

    @Override
    public final void setConfig(final Map<String, String> cfg)
    {
        globalStyleDefinition = cfg.get("GlobalStyleDefinition");
        rowStyleDefinition = cfg.get("RowStyleDefinition");
    }

    @Override
    public final String getName()
    {
        return "HtmlExportStyle";
    }

    @Override
    public final void setOutputStream(final OutputStream newOut)
    {
        out = newOut;
    }

    @Override
    public final void setDatabaseConnection(final Connection dbconnection)
    {
        // not possible
    }

}
