/**
 *
 */
package org.Transformer.Slides;

import org.Transformer.Factory;
import org.Transformer.Translator;
import org.Transformer.dataset.DataFilter;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class SelectDataFilterSlide extends SelectOneFromManySlide
{
    private Translator msg;

    /**
     *
     */
    public SelectDataFilterSlide(Translator newMsg)
    {
        updateLanguage(newMsg);
        addChoices(Factory.getAllDataFilterSlides());
        addChoice("None", null);
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#updateLanguage(java.util.ResourceBundle)
     */
    @Override
    public void updateLanguage(Translator newMsg)
    {
        msg = newMsg;
        setTitel(msg.tr("Define Filter"));
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getName()
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return "Select Data Filter";
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
            job.setDataFilter((DataFilter)getSelectedChoice());
        }
    }

}
