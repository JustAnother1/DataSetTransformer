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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.Transformer.Translator;
import org.Transformer.dataset.DataSet;
import org.jdom.Element;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class FileExporter extends Exporter
{
    private boolean ExportSuccessfullyExecuted = false;
    private String target = "";
    private JTextField url;

    /**
     *
     */
    public FileExporter()
    {
    }

    public void export(DataSet[] theData, ExportStyle expStyle)
    {
        FileOutputStream out;
        try
        {
            out = new FileOutputStream(target);
            if(true == expStyle.formatTheData(theData, out))
            {
                out.flush();
                ExportSuccessfullyExecuted = true;
                System.out.println("Export successfully !");
            }
            else
            {
                System.out.println("Export failed !");
            }
            out.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Export failed !");
            ExportSuccessfullyExecuted = false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Export failed !");
            ExportSuccessfullyExecuted = false;
        }
    }

    @Override
    public boolean wasSuccessfull()
    {
        return ExportSuccessfullyExecuted;
    }

    public void setTarget(String trg)
    {
        if(null != trg)
        {
            target = trg;
        }
    }


    @Override
    public Element getConfig()
    {
        org.jdom.Element res = new org.jdom.Element("cfg");
        org.jdom.Element src = new org.jdom.Element("target");
        src.setText(target);
        res.addContent(src);
        return res;
    }

    @Override
    public void setConfig(Element cfg)
    {
        target = cfg.getChildText("target");
    }

    @Override
    public String getName()
    {
        return "FileExporter";
    }


    @Override
    public void actionOnClose()
    {
        if(null != url)
        {
            target = url.getText();
        }
    }

    @Override
    public Component getComponent()
    {
        JPanel slide = new JPanel();
        slide.setLayout(new BoxLayout(slide, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("File Name :");
        slide.add(desc);
        url = new JTextField();
        slide.add(url);
        slide.add(Box.createVerticalGlue());
        return slide;
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
