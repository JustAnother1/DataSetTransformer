/**
 *
 */
package org.Transformer.Slides;

import org.Transformer.Factory;
import org.Transformer.Translator;
import org.Transformer.exporter.ExportStyle;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SelectExportStyleSlide extends SelectOneFromManySlide
{
    private Translator msg;

    /**
     *
     */
    public SelectExportStyleSlide(Translator newMsg)
    {
        updateLanguage(newMsg);
        addChoices(Factory.getAllExportStyleSlides());
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(java.util.ResourceBundle)
     */
    @Override
    public void updateLanguage(Translator newMsg)
    {
        msg = newMsg;
        setTitel(msg.tr("Define Target Format"));
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getName()
     */
    @Override
    public String getName()
    {
        return "Select Export Style";
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
            job.setExportStyle((ExportStyle)getSelectedChoice());
        }
    }

}
