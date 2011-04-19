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
import java.io.InputStream;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.Transformer.Tool;
import org.Transformer.Translator;
import org.Transformer.XmlUtils;
import org.Transformer.dataset.DataSet;
import org.jdom.Element;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class TreeImportSelector extends ImportSelector
{
    private TreeStructure expectedStructure;
    private String[] Mapping_Position;
    private JTextArea pos;
    private String[] Mapping_Name;
    private JTextArea names;

    /**
     *
     */
    public TreeImportSelector()
    {
        super();
    }

    @Override
    public void updateLanguage(Translator newMsg)
    {
        // TODO Auto-generated method stub

    }

    public void setMapping(String[] positions, String[] Names)
    {
        Mapping_Name = Names;
        Mapping_Position = positions;
    }

    @Override
    public boolean parseToDataSets(InputStream src)
    {
        expectedStructure = new XmlTreeStructure(null);
        TreeStructure Tree = new HtmlTreeStructure(src);
        if(false == Tree.matchesTreeStructure(expectedStructure))
        {
            System.out.println("Wrong Tree Structure");
            return false;
        }
        if((null == Mapping_Position) || (null == Mapping_Name))
        {
            System.out.println("No Mapping !");
            return false;
        }
        Vector<DataSet> res = new Vector<DataSet>();

        // TODO more than one row
        DataSet row = new DataSet();
        for(int i = 0; i < Mapping_Position.length; i++)
        {
            String Val = Tree.getLeafFor(Mapping_Position[i]);
            row.addDataAtom(Val, Mapping_Name[i]);
        }
        res.add(row);

        data = (DataSet[])res.toArray(new DataSet[1]);
        return true;
    }

    @Override
    public Element getConfig()
    {
        org.jdom.Element res = new org.jdom.Element("cfg");
        org.jdom.Element pos = XmlUtils.getElementFor(Mapping_Position, "MappingPosition");
        res.addContent(pos);
        org.jdom.Element name = XmlUtils.getElementFor(Mapping_Name, "MappingName");
        res.addContent(name);
        return res;
    }

    @Override
    public void setConfig(Element cfg)
    {
        Mapping_Position = XmlUtils.getStringArrayFrom(cfg.getChild("MappingPosition"));
        Mapping_Name = XmlUtils.getStringArrayFrom(cfg.getChild("MappingName"));
    }

    @Override
    public String getName()
    {
        return "TreeImportSelector";
    }

    @Override
    public Component getComponent()
    {
        JPanel parentSlide = new JPanel();
        parentSlide.setLayout(new BoxLayout(parentSlide, BoxLayout.Y_AXIS));

        JPanel slide1 = new JPanel();
        slide1.setLayout(new BoxLayout(slide1, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("Positions : ");
        slide1.add(desc);
        pos = new JTextArea();
        pos.setLineWrap(false);
        slide1.add(pos);

        JPanel slide2 = new JPanel();
        slide2.setLayout(new BoxLayout(slide2, BoxLayout.X_AXIS));
        JLabel desc2 = new JLabel("Names : ");
        slide2.add(desc2);
        names = new JTextArea();
        names.setLineWrap(false);
        slide2.add(names);

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
    public void actionOnClose()
    {
        if(null != pos)
        {
            Mapping_Position = Tool.StringToArray(pos.getText());
        }
        if(null != names)
        {
            Mapping_Name = Tool.StringToArray(names.getText());
        }
    }

}
