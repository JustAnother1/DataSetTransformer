/**
 *
 */
package org.Transformer.Slides;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.Transformer.Translator;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class FinishedOrNewJobSlide extends OneNextConfigurationSlide
{
    private Translator msg;

    /**
     *
     */
    public FinishedOrNewJobSlide(Translator newMsg)
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
        return "Finished";
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getComponent()
     */
    @Override
    public Component getComponent()
    {
        final JPanel slide = new JPanel();
        // TODO
        JLabel finished = new JLabel(msg.tr("Finished the Job !"));
        slide.add(finished);
        return slide;
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#actionAfterShow()
     */
    @Override
    public void actionAfterShow()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#actionOnClose()
     */
    @Override
    public void actionOnClose()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(java.util.ResourceBundle)
     */
    @Override
    public void updateLanguage(Translator newMsg)
    {
        msg = newMsg;
    }

}
