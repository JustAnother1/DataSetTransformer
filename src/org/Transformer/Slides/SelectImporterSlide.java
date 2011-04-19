/**
 *
 */
package org.Transformer.Slides;

import org.Transformer.Factory;
import org.Transformer.Translator;
import org.Transformer.importer.Importer;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SelectImporterSlide extends SelectOneFromManySlide
{
    private Translator msg;

    /**
     *
     */
    public SelectImporterSlide(Translator newMsg)
    {
        updateLanguage(newMsg);
        addChoices(Factory.getAllImporterSlides());
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(java.util.ResourceBundle)
     */
    @Override
    public void updateLanguage(Translator newMsg)
    {
        msg = newMsg;
        setTitel(msg.tr("Define Data Source"));
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getName()
     */
    @Override
    public String getName()
    {
        return "Select Importer";
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
            job.setImporter((Importer)getSelectedChoice());
        }
    }

}
