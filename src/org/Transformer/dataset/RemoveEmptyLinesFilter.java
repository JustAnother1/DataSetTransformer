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

import org.Transformer.Translator;
import org.jdom.Element;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class RemoveEmptyLinesFilter extends DataFilter
{

    /**
     *
     */
    public RemoveEmptyLinesFilter()
    {
    }

    public DataSet[] applyFilterTo(DataSet theData[])
    {
        Vector<DataSet> res = new Vector<DataSet>();
        for(int i = 0; i < theData.length; i++)
        {
            if(0 != theData[i].getNumberOfAtoms())
            {
                res.add(theData[i]);
            }
            // else skip empty Line
        }
        return res.toArray(new DataSet[1]);
    }

    @Override
    public Element getConfig()
    {
        org.jdom.Element res = new org.jdom.Element("cfg");
        return res;
    }

    @Override
    public void setConfig(Element cfg)
    {
    }

    @Override
    public String getName()
    {
        return "RemoveEmptyLinesFilter";
    }

    @Override
    public Component getComponent()
    {
        JPanel slide = new JPanel();
        slide.setLayout(new BoxLayout(slide, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("no configuration needed !");
        slide.add(desc);
        slide.add(Box.createVerticalGlue());
        return slide;
    }

    @Override
    public void actionAfterShow()
    {
    }

    @Override
    public void actionOnClose()
    {
    }

    @Override
    public void updateLanguage(Translator newMsg)
    {
        // TODO Auto-generated method stub

    }

}
