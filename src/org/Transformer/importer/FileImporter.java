/**
 *
 */
package org.Transformer.importer;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.Transformer.Translator;
import org.jdom.Element;

/**
 * @author lars
 *
 */
public class FileImporter extends Importer
{
    protected String FileName = "";
    private JTextField path;

    /**
     *
     */
    public FileImporter()
    {
    }

    /* (non-Javadoc)
     * @see org.Transformer.XmlSerialize#getConfig()
     */
    @Override
    public Element getConfig()
    {
        org.jdom.Element res = new org.jdom.Element("cfg");
        org.jdom.Element src = new org.jdom.Element("FileName");
        src.setText(FileName);
        res.addContent(src);
        return res;
    }

    /* (non-Javadoc)
     * @see org.Transformer.XmlSerialize#setConfig(org.jdom.Element)
     */
    @Override
    public void setConfig(Element cfg)
    {
        FileName = cfg.getChildText("FileName");
    }

    /* (non-Javadoc)
     * @see org.Transformer.XmlSerialize#getName()
     */
    @Override
    public String getName()
    {
        return "FileImporter";
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getComponent()
     */
    @Override
    public Component getComponent()
    {
        JPanel slide = new JPanel();
        slide.setLayout(new BoxLayout(slide, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("FileName :");
        slide.add(desc);
        path = new JTextField();
        slide.add(path);
        slide.add(Box.createVerticalGlue());
        return slide;
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#actionAfterShow()
     */
    @Override
    public void actionAfterShow()
    {
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#actionOnClose()
     */
    @Override
    public void actionOnClose()
    {
        if(null != path)
        {
            FileName = path.getText();
        }
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(org.Transformer.Translator)
     */
    @Override
    public void updateLanguage(Translator msg)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.Transformer.importer.Importer#setSource(java.lang.String)
     */
    @Override
    public void setSource(String src)
    {
        if(null != src)
        {
            FileName = src;
        }
    }

    /* (non-Javadoc)
     * @see org.Transformer.importer.Importer#importData(org.Transformer.importer.ImportSelector)
     */
    @Override
    public void importData(ImportSelector infilt)
    {
        try
        {
            File sourceFile = new File(FileName);
            if((false == sourceFile.exists()) || (false == sourceFile.canRead()))
            {
                System.err.println("Could not read Data from " + FileName);
            }
            FileInputStream fin = new FileInputStream(sourceFile);
            System.out.println("Reading Data from " + sourceFile.getAbsolutePath());
            if(true == infilt.parseToDataSets(fin))
            {
                theImportedData = infilt.getTheData();
                ImportSuccessfullyCompleted = true;
            }
            else
            {
                ImportSuccessfullyCompleted = false;
            }
            fin.close();
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
            ImportSuccessfullyCompleted = false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            ImportSuccessfullyCompleted = false;
        }
    }

}
