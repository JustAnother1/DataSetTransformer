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

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

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
public class CsvImportSelector extends ImportSelector
{
    private String seperator = ",";
    private String[] mapping;
    private JTextField sep;
    private JTextArea map;

    /**
     *
     */
    public CsvImportSelector()
    {
        super();
    }

    public void setMapping(String[] theMapping)
    {
        mapping = theMapping;
    }

    public void setSeperator(String theSeperator)
    {
        seperator = theSeperator;
    }


    /**
     * @see org.Transformer.importer.ImportFilter#parseToDataSets(java.io.InputStream)
     */
    @Override
    public boolean parseToDataSets(InputStream src)
    {
        Vector<DataSet> res = new Vector<DataSet>();
        InputStreamReader in = new InputStreamReader(src);
        StringBuffer sb = new StringBuffer();
        int r;
        try
        {
            do{
                r = in.read();
                if(-1 != r)
                {
                    char c = (char)r;
                    if(c == '\n')
                    {
                        // end of Line reached
                        String line = sb.toString();
                        DataSet LineSet = parseLineToDataSet(line);
                        if(null != LineSet)
                        {
                            res.add(LineSet);
                        }
                        sb = new StringBuffer();
                    }
                    else
                    {
                        sb.append(c);
                    }
                }
            }while(-1 != r);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        // end of Line reached
        String line = sb.toString();
        DataSet LineSet = parseLineToDataSet(line);
        if(null != LineSet)
        {
            res.add(LineSet);
        }

        data = res.toArray(new DataSet[1]);
        return true;
    }


    private DataSet parseLineToDataSet(String line)
    {
        System.out.println("Line : " + line);
        String[] Values = line.split(seperator);
        System.out.println("Split with Seperator : " + seperator + " !");
        for(int i = 0; i < Values.length; i++)
        {
            System.out.println("Atom " + i + " : " + Values[i]);
        }
        DataSet row = new DataSet();
        System.out.println("mapping.length : " + mapping.length);
        for(int i = 0; i < mapping.length; i++)
        {
            System.out.println("Mapping " + i + " : " + mapping[i]);
        }
        int maxLength;
        if(mapping.length > Values.length)
        {
            maxLength = Values.length;
        }
        else
        {
            maxLength = mapping.length;
        }
        for(int j = 0; j < maxLength; j++)
        {
            String Name = mapping[j];
            if(null != Name)
            {
                row.addDataAtom(Values[j], Name);
            }
            // else this field is not mapped
        }
        return row;
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
        return "CsvImportSelector";
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
