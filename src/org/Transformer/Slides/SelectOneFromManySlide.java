/**
 *
 */
package org.Transformer.Slides;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public abstract class SelectOneFromManySlide extends OneNextConfigurationSlide implements ActionListener
{
    private HashMap<String, ConfigurationSlide> choices;
    private String Title;
    private String selectedSlide;
    private String[] choiceArray;
    private JPanel config;
    private JPanel slide;

    /**
     *
     */
    public SelectOneFromManySlide()
    {
        choices = new HashMap<String, ConfigurationSlide>();
        Title = "";
        selectedSlide = null;
    }

    protected void addChoices(ConfigurationSlide[] opts)
    {
        for(int i = 0; i < opts.length; i++)
        {
            addChoice(opts[i].getName(), opts[i]);
        }
    }

    protected void addChoice(String Name, ConfigurationSlide opt)
    {
        choices.put(Name, opt);
        if(null == selectedSlide)
        {
            selectedSlide = Name;
        }
    }

    protected void setTitel(String text)
    {
        Title = text;
    }

    protected ConfigurationSlide getSelectedChoice()
    {
        return choices.get(selectedSlide);
    }

    protected void closeCurrentSlide()
    {
        ConfigurationSlide curSlide = getSelectedChoice();
        curSlide.actionOnClose();
    }

    /* (non-Javadoc)
     * @see org.Transformer.Slides.ConfigurationSlide#getComponent()
     */
    @Override
    public Component getComponent()
    {
        slide = new JPanel();
        slide.setLayout(new BoxLayout(slide, BoxLayout.Y_AXIS));

        JLabel headline = new JLabel(Title);
        slide.add(headline);

        if(1 < choices.size())
        {
            // Drop Down Menu on Top with all Choices
            // Panel beneath with configuration for the selected Choice
            Set<String> g = choices.keySet();
            choiceArray = g.toArray(new String[1]);
            JComboBox dropDown = new JComboBox(choiceArray);
            dropDown.setSelectedIndex(0);
            selectedSlide = choiceArray[0];
            dropDown.addActionListener(this);
            slide.add(dropDown);
        }
        // else No Drop Down Menu

        config = getPanelFor(selectedSlide);
        if(null != config)
        {
            slide.add(config);
        }
        return slide;
    }

    private JPanel getPanelFor(String SlideName)
    {
        ConfigurationSlide curSlide = choices.get(SlideName);
        final JPanel config;
        if(null == curSlide)
        {
            config = new JPanel();
            JLabel error = new JLabel("ERROR - no choices available !");
            config.add(error);
        }
        else
        {
            config = (JPanel)curSlide.getComponent();
        }
        if(null == SlideName)
        {
            SlideName = "null";
        }
        if(null != config)
        {
            config.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(SlideName),
                    BorderFactory.createEmptyBorder(5,5,5,5)));
        }
        return config;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JComboBox cb = (JComboBox)e.getSource();
        String newSlide = (String)cb.getSelectedItem();
        selectedSlide = newSlide;
        if(null != config)
        {
            config.removeAll();
            slide.remove(config);
        }
        config = getPanelFor(newSlide);
        if(null != config)
        {
            slide.add(config);
        }
        slide.validate();
    }

}
