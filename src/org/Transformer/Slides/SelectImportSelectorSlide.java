/**
 *
 */
package org.Transformer.Slides;

import org.Transformer.Factory;
import org.Transformer.Translator;
import org.Transformer.importer.ImportSelector;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SelectImportSelectorSlide extends SelectOneFromManySlide
{
    private Translator msg;

    /**
     *
     */
    public SelectImportSelectorSlide(Translator newMsg)
    {
        updateLanguage(newMsg);
        addChoices(Factory.getAllImportSelectorSlides());
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(java.util.ResourceBundle)
     */
    @Override
    public void updateLanguage(Translator newMsg)
    {
        msg = newMsg;
        setTitel(msg.tr("Define Source Format"));
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getName()
     */
    @Override
    public String getName()
    {
        return "Select Import Selector";
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
            job.setImportSelector((ImportSelector)getSelectedChoice());
        }
    }

}
