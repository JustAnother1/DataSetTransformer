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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.Transformer.Translator;
import org.Transformer.dataset.DataSet;
import org.jdom.CDATA;
import org.jdom.Element;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class HtmlExportStyle extends ExportStyle
{
    private String GlobalStyleDefinition = "";
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
        String[] parts = usedRowStyle.split("%", -1);
        for(int i = 0; i < parts.length; i++)
        {
            if(0 == i%2)
            {
                // Text
                out.write(parts[i].getBytes()); // Todo charset ?
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
                out.write(variable.toString().getBytes());// Todo charset ?
            }
        }
        return true;
    }

    @Override
    public boolean formatTheData(DataSet[] theData, OutputStream out) throws IOException
    {
        String usedStyle = GlobalStyleDefinition;
        String[] globalParts = usedStyle.split("%", 3);
        // Global Prefix
        out.write(globalParts[0].getBytes()); // Todo charset ?
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
            out.write(globalParts[2].getBytes()); // Todo charset ?
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
    public Element getConfig()
    {
        org.jdom.Element res = new org.jdom.Element("cfg");
        org.jdom.Element row = new org.jdom.Element("RowStyleDefinition");
        CDATA rowdata = new CDATA(RowStyleDefinition);
        row.addContent(rowdata);
        res.addContent(row);
        org.jdom.Element glo = new org.jdom.Element("GlobalStyleDefinition");
        CDATA glodata = new CDATA(GlobalStyleDefinition);
        glo.addContent(glodata);
        res.addContent(glo);
        return res;
    }

    @Override
    public void setConfig(Element cfg)
    {
        GlobalStyleDefinition = cfg.getChildText("GlobalStyleDefinition");
        RowStyleDefinition = cfg.getChildText("RowStyleDefinition");
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
