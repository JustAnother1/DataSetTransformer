/**
 *
 */
package org.Transformer.Slides;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.Transformer.BaseWindow;
import org.Transformer.Translator;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class ExecuteJobSlide extends OneNextConfigurationSlide
{
    @SuppressWarnings("unused")
    private Translator msg;
    private JProgressBar pgb;
    private JobExecutorTask jet;
    private BaseWindow baseWindow;

    /**
     * @param baseWindow
     *
     */
    public ExecuteJobSlide(Translator newMsg, BaseWindow baseWindow)
    {
        updateLanguage(newMsg);
        this.baseWindow = baseWindow;
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(java.util.ResourceBundle)
     */
    @Override
    public void updateLanguage(Translator newMsg)
    {
        msg = newMsg;
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getName()
     */
    @Override
    public String getName()
    {
        return "Execute Job";
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getComponent()
     */
    @Override
    public Component getComponent()
    {
        final JPanel slide = new JPanel();
        pgb = new JProgressBar(0, 100);
        pgb.setValue(0);
        // TODO http://download.oracle.com/javase/tutorial/uiswing/components/progress.html
        slide.add(pgb);
        return slide;
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#actionAfterShow()
     */
    @Override
    public void actionAfterShow()
    {
        jet = new JobExecutorTask(pgb, job, baseWindow);
        jet.execute();
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#actionOnClose()
     */
    @Override
    public void actionOnClose()
    {
        jet.cancel(true);
    }

}
