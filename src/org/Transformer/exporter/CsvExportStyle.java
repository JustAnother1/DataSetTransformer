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

import org.Transformer.Tool;
import org.Transformer.Translator;
import org.Transformer.XmlUtils;
import org.Transformer.dataset.DataSet;
import org.jdom.Element;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class CsvExportStyle extends ExportStyle
{
    private String[] mapping;
    private String seperator = ",";
    private JTextField sep;
    private JTextArea map;

    /**
     *
     */
    public CsvExportStyle()
    {
    }

    public void setSeperator(String theSeperator)
    {
        seperator = theSeperator;
    }

    public void setMapping(String[] theMapping)
    {
        mapping = theMapping;
    }

    /**
     * @see org.Transformer.exporter.ExportStyle#formatTheData(org.Transformer.dataset.DataSet[], java.io.OutputStream)
     */
    @Override
    public boolean formatTheData(DataSet[] theData, OutputStream out) throws IOException
    {
        if((null == mapping) || (null == theData) || (null == out))
        {
            return false;
        }
        for(int i = 0; i < theData.length; i++)
        {
            String line = "";
            DataSet curSet = theData[i];
            if(null != curSet)
            {
                int maxLength;
                if(mapping.length > curSet.getNumberOfAtoms())
                {
                    maxLength = curSet.getNumberOfAtoms();
                }
                else
                {
                    maxLength = mapping.length;
                }
                for(int j = 0; j < maxLength; j++)
                {
                    Object val = curSet.getDataAtom(mapping[j]);
                    if(null != val)
                    {
                        line = line + val.toString() + seperator;
                    }
                }
            }
            line = line + "\n";
            out.write(line.getBytes());
        }
        return true;
    }


    @Override
    public Element getConfig()
    {
        org.jdom.Element res = new org.jdom.Element("cfg");
        org.jdom.Element pos = XmlUtils.getElementFor(mapping, "Mapping");
        res.addContent(pos);
        org.jdom.Element src = new org.jdom.Element("seperator");
        src.setText(seperator);
        res.addContent(src);
        return res;
    }

    @Override
    public void setConfig(Element cfg)
    {
        mapping = XmlUtils.getStringArrayFrom(cfg.getChild("Mapping"));
        seperator = cfg.getChildText("seperator");
    }

    @Override
    public String getName()
    {
        return "CsvExportStyle";
    }


    @Override
    public void actionOnClose()
    {
        if(null != sep)
        {
            seperator = sep.getText();
        }
        if(null != map)
        {
            mapping = Tool.StringToArray(map.getText());
        }
    }

    @Override
    public Component getComponent()
    {
        JPanel parentSlide = new JPanel();
        parentSlide.setLayout(new BoxLayout(parentSlide, BoxLayout.Y_AXIS));

        JPanel slide1 = new JPanel();
        slide1.setLayout(new BoxLayout(slide1, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("Seperator : ");
        slide1.add(desc);
        sep = new JTextField();
        sep.setText(",");
        slide1.add(sep);

        JPanel slide2 = new JPanel();
        slide2.setLayout(new BoxLayout(slide2, BoxLayout.X_AXIS));
        JLabel desc2 = new JLabel("Mapping : ");
        slide2.add(desc2);
        map = new JTextArea();
        slide2.add(map);

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
