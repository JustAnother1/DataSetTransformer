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
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.Transformer.JobUtils;
import org.Transformer.Tool;
import org.Transformer.Translator;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class ConCatFilter extends DataFilter
{
    private String[] FieldThatMustBeEqual;
    private String[] FieldsThatWillBeConCatenated;
    private String ConCatSeperator = "";
    private JTextField sep;
    private JTextArea map;
    private JTextArea cmap;

    /**
     *
     */
    public ConCatFilter()
    {
    }

    @Override
    public void updateLanguage(Translator newMsg)
    {
    }

    public void setFieldThatMustBeEqual(String[] fieldThatMustBeEqual)
    {
        FieldThatMustBeEqual = fieldThatMustBeEqual;
    }

    public void setFieldsThatWillBeConCatenated(String[] fieldsThatWillBeConCatenated)
    {
        FieldsThatWillBeConCatenated = fieldsThatWillBeConCatenated;
    }

    public void setConCatSeperator(String conCatSeperator)
    {
        ConCatSeperator = conCatSeperator;
    }

    /**
     * @see org.Transformer.dataset.DataFilter#applyFilterTo(org.Transformer.dataset.DataSet[])
     */
    @Override
    public DataSet[] applyFilterTo(DataSet[] theData)
    {
        DataSet[] res = new DataSet[1];
        res[0] = new DataSet();
        if((null == FieldThatMustBeEqual) || (null == FieldsThatWillBeConCatenated) || (null == theData))
        {
            return res;
        }
        if(1 > theData.length)
        {
            return res;
        }
        for(int i = 0; i < FieldThatMustBeEqual.length; i++)
        {
            // copy the Fields
            res[0].addDataAtom(theData[0].getDataAtom(FieldThatMustBeEqual[i]), FieldThatMustBeEqual[i]);
        }
        for(int i = 0; i < FieldsThatWillBeConCatenated.length; i++)
        {
            // copy the Fields
            res[0].addDataAtom(theData[0].getDataAtom(FieldsThatWillBeConCatenated[i]), FieldsThatWillBeConCatenated[i]);
        }
        if(2 > theData.length)
        {
            // only one element
            return res;
        }

        // check Eqals
        for(int k = 0; k < FieldThatMustBeEqual.length; k++)
        {
            String should = res[0].getDataAtom(FieldThatMustBeEqual[k]);
            if(null != should)
            {
                for(int i = 1; i < theData.length; i++)
                {
                    String is = theData[i].getDataAtom(FieldThatMustBeEqual[k]);
                    if(false == should.equals(is))
                    {
                        System.out.println("Checked field is different !(" + should + "|" + is + ")");
                        return null;
                    }
                }
            }
            else
            {
                System.out.println("Checked field should Value is null !");
            }
        }

        // concat
        for(int k = 0; k < FieldsThatWillBeConCatenated.length; k++)
        {
            String line = "";
            for(int i = 0; i < theData.length; i++)
            {
                line = line + ConCatSeperator + theData[i].getDataAtom(FieldsThatWillBeConCatenated[k]);
            }
            res[0].addDataAtom(line, FieldsThatWillBeConCatenated[k]);
        }

        return res;
    }

    @Override
    public String getConfig()
    {
        return "ConCatSeperator = " + ConCatSeperator + "\n"
               + JobUtils.getConfigTextFor(FieldThatMustBeEqual, "FieldThatMustBeEqual")
               + JobUtils.getConfigTextFor(FieldsThatWillBeConCatenated, "FieldsThatWillBeConCatenated");
    }

    @Override
    public void setConfig(Map<String, String> cfg)
    {
        ConCatSeperator = cfg.get("ConCatSeperator");
        FieldThatMustBeEqual = JobUtils.getStringArrayFromSettingMap(cfg, "FieldThatMustBeEqual");
        FieldsThatWillBeConCatenated = JobUtils.getStringArrayFromSettingMap(cfg, "FieldsThatWillBeConCatenated");
    }

    @Override
    public String getName()
    {
        return "ConCatFilter";
    }

    @Override
    public void actionAfterShow()
    {
    }


    @Override
    public void actionOnClose()
    {
        if(null != sep)
        {
            ConCatSeperator = sep.getText();
        }
        if(null != cmap)
        {
            FieldsThatWillBeConCatenated = Tool.StringToArray(cmap.getText());
        }
        if(null != map)
        {
            FieldThatMustBeEqual = Tool.StringToArray(map.getText());
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
        JLabel desc2 = new JLabel("Equal Mapping : ");
        slide2.add(desc2);
        map = new JTextArea();
        slide2.add(map);

        JPanel slide3 = new JPanel();
        slide3.setLayout(new BoxLayout(slide3, BoxLayout.X_AXIS));
        JLabel desc3 = new JLabel("Concat Mapping : ");
        slide3.add(desc3);
        cmap = new JTextArea();
        slide3.add(cmap);

        parentSlide.add(slide1);
        parentSlide.add(slide2);
        parentSlide.add(slide3);
        parentSlide.add(Box.createVerticalGlue());
        return parentSlide;
    }

}
