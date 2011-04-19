/**
 *
 */
package org.Transformer.Slides;

import org.Transformer.Factory;
import org.Transformer.Translator;
import org.Transformer.exporter.Exporter;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SelectExporterSlide extends SelectOneFromManySlide
{
    private Translator msg;

    /**
     *
     */
    public SelectExporterSlide(Translator newMsg)
    {
        updateLanguage(newMsg);
        addChoices(Factory.getAllExporterSlides());
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(java.util.ResourceBundle)
     */
    @Override
    public void updateLanguage(Translator newMsg)
    {
        msg = newMsg;
        setTitel(msg.tr("Define Target"));
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getName()
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return "Select Exporter";
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
        closeCurrentSlide();
        if(null != job)
        {
            job.setExporter((Exporter)getSelectedChoice());
        }
    }

}
