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
package org.Transformer.dataset;

import java.awt.Component;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.Transformer.Translator;
import org.jdom.Element;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class RegExpFilter extends DataFilter
{
    private String RegularExpression;
    private String NameOfAtom;
    private JTextField exp;
    private JTextField map;

    /**
     *
     */
    public RegExpFilter()
    {
    }

    public void setRegularExpression(String regularExpression)
    {
        RegularExpression = regularExpression;
    }

    public void setNameOfAtom(String nameOfAtom)
    {
        NameOfAtom = nameOfAtom;
    }

    /**
     * @see org.Transformer.dataset.DataFilter#applyFilterTo(org.Transformer.dataset.DataSet[])
     */
    @Override
    public DataSet[] applyFilterTo(DataSet[] theData)
    {
        Vector<DataSet> res = new Vector<DataSet>();
        for(int i = 0; i < theData.length; i++)
        {
            String fieldValue = theData[i].getDataAtom(NameOfAtom);
            if(null != fieldValue)
            {
                if(true == fieldValue.matches(RegularExpression))
                {
                    res.add(theData[i]);
                }
                // else skip not matching Data Set
            }
            // else skip not matching Data Set
        }
        return res.toArray(new DataSet[1]);
    }


    @Override
    public Element getConfig()
    {
        org.jdom.Element res = new org.jdom.Element("cfg");
        org.jdom.Element regexp = new org.jdom.Element("RegularExpression");
        regexp.setText(RegularExpression);
        res.addContent(regexp);
        org.jdom.Element name = new org.jdom.Element("NameOfAtom");
        name.setText(NameOfAtom);
        res.addContent(name);
        return res;
    }

    @Override
    public void setConfig(Element cfg)
    {
        RegularExpression = cfg.getChildText("RegularExpression");
        NameOfAtom = cfg.getChildText("NameOfAtom");
    }

    @Override
    public String getName()
    {
        return "RegExpFilter";
    }

    @Override
    public void actionOnClose()
    {
        if(null != exp)
        {
            RegularExpression = exp.getText();
        }
        if(null != map)
        {
            NameOfAtom = map.getText();
        }
    }

    @Override
    public Component getComponent()
    {
        JPanel parentSlide = new JPanel();
        parentSlide.setLayout(new BoxLayout(parentSlide, BoxLayout.Y_AXIS));

        JPanel slide1 = new JPanel();
        slide1.setLayout(new BoxLayout(slide1, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("Expression : ");
        slide1.add(desc);
        exp = new JTextField();
        slide1.add(exp);

        JPanel slide2 = new JPanel();
        slide2.setLayout(new BoxLayout(slide2, BoxLayout.X_AXIS));
        JLabel desc2 = new JLabel("Name : ");
        slide2.add(desc2);
        map = new JTextField();
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
