/**
 *
 */
package org.Transformer.Slides;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.Transformer.Job;
import org.Transformer.Translator;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SaveJobSlide extends OneNextConfigurationSlide implements ActionListener
{
    private Translator msg;
    private JTextField FileNameField;

    /**
     *
     */
    public SaveJobSlide(Translator newMsg)
    {
        msg = newMsg;
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getName()
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return "Save Job";
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getComponent()
     */
    @Override
    public Component getComponent()
    {
        final JPanel slide = new JPanel();
        slide.setLayout(new BoxLayout(slide, BoxLayout.Y_AXIS));
        JLabel headline = new JLabel(msg.tr("If you want to save the Configuration to a Job File then fill in the File Name and Press the Button"));
        slide.add(headline);

        JPanel slide1 = new JPanel();
        slide1.setLayout(new BoxLayout(slide1, BoxLayout.X_AXIS));
        JLabel desc = new JLabel("File Name : ");
        slide1.add(desc);
        FileNameField = new JTextField();
        slide1.add(FileNameField);
        slide.add(slide1);

        JButton SaveButton = new JButton();
        SaveButton.setText("Save");
        SaveButton.addActionListener(this);
        slide.add(SaveButton);
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
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(java.util.ResourceBundle)
     */
    @Override
    public void updateLanguage(Translator newMsg)
    {
        msg = newMsg;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Button has been pressed -Save Job to File
        String FileName = FileNameField.getText();
        if(false == FileName.endsWith(".job"))
        {
            FileName = FileName + ".job";
        }
        final File JobFile = new File(FileName);
        Job.writeJobToFile(JobFile, job);
    }

}
