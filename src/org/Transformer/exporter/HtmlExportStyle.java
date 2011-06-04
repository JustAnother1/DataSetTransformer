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

import java.awt.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.Transformer.Translator;
import org.Transformer.dataset.DataSet;
import org.apache.log4j.Logger;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class HtmlExportStyle extends ExportStyle
{
    private final Logger log = Logger.getLogger(this.getClass().getName());
    /**
     * Structure of the HTML File before and After the Row data. Row Data is inserted at the Point labeled with %ROW%.
     * Simple Example: "<html><head></head><body>%ROW%</body></html>"
     */
    private String GlobalStyleDefinition = "";
    /**
     * Formating of the Row Data. consist of pairs of Descriptionand Variable name.
     * If the Variables in the DataSet have the Names vari1 vari2 and var3 then the following styles are valid:
     * "%vari1", "Value of Variable 1 : %vari1%Value of Variable 2 : %vari2%Value of Variable 3 : %vari3"
     */
    private String RowStyleDefinition = "";
    private JTextField glob;
    private JTextArea row;

    /**
     *
     */
    public HtmlExportStyle()
    {
    }

    private boolean formatARow(DataSet theData, OutputStream out) throws IOException
    {
        String usedRowStyle = RowStyleDefinition;
        if(null == usedRowStyle)
        {
            log.error("Row Style Definition is Null !");
            return false;
        }
        String[] parts = usedRowStyle.split("%", -1);
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
                Object variable = theData.getDataAtom(parts[i]);
                if(null == variable)
                {
                    System.err.println("Could not resolve Variable ! (" + parts[i] + ") !");
                    return false;
                }
                out.write(variable.toString().getBytes());// TODO charset ?
            }
        }
        return true;
    }

    @Override
    public boolean formatTheData(DataSet[] theData, OutputStream out) throws IOException
    {
        String usedStyle = GlobalStyleDefinition;
        if(null == usedStyle)
        {
            log.error("Global Style Definition is Null !");
            return false;
        }
        log.debug("Using Global Style : " + usedStyle);
        String[] globalParts = usedStyle.split("%", 3);
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
                System.err.println("Global Style Definition invalid ! (" + globalParts[1] + ") !");
                return false;
            }
        }
        for(int g = 0; g < theData.length; g++)
        {
            if(false == formatARow(theData[g], out))
            {
                System.err.println("Row output failed at row " + g + " !");
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


    public void setStyleDefinition(String globaleDef, String RowDef)
    {
        if(null != globaleDef)
        {
            GlobalStyleDefinition = globaleDef;
        }
        if(null != RowDef)
        {
            RowStyleDefinition = RowDef;
        }
    }
    @Override
    public String getConfig()
    {
        return "GlobalStyleDefinition = " + GlobalStyleDefinition + "\n"
               + "RowStyleDefinition = " + RowStyleDefinition;
    }

    @Override
    public void setConfig(Map<String, String> cfg)
    {
        GlobalStyleDefinition = cfg.get("GlobalStyleDefinition");
        RowStyleDefinition = cfg.get("RowStyleDefinition");
    }

    @Override
    public String getName()
    {
        return "HtmlExportStyle";
    }

    @Override
    public void actionOnClose()
    {
        if(null != glob)
        {
            GlobalStyleDefinition = glob.getText();
        }
        if(null != row)
        {
            RowStyleDefinition = row.getText();
        }
    }

    @Override
    public Component getComponent()
    {
        JPanel parentSlide = new JPanel();
        parentSlide.setLayout(new BoxLayout(parentSlide, BoxLayout.Y_AXIS));

        JPanel slide1 = new JPanel();
        slide1.setLayout(new BoxLayout(slide1, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("Global Style : ");
        slide1.add(desc);
        JTextField glob = new JTextField();
        slide1.add(glob);

        JPanel slide2 = new JPanel();
        slide2.setLayout(new BoxLayout(slide2, BoxLayout.X_AXIS));
        JLabel desc2 = new JLabel("Row Style : ");
        slide2.add(desc2);
        JTextArea row = new JTextArea();
        slide2.add(row);

        parentSlide.add(slide1);
        parentSlide.add(slide2);
        parentSlide.add(Box.createVerticalGlue());
        return parentSlide;
    }

    @Override
    public void actionAfterShow()
    {
    }

    @Override
    public void updateLanguage(Translator newMsg)
    {
        // TODO Auto-generated method stub

    }

}
