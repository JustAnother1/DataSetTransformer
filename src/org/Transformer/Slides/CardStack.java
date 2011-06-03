/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/>
 *
 */
/**
 *
 */
package org.Transformer.Slides;

import java.awt.Container;
import java.util.Vector;

import org.Transformer.BaseWindow;
import org.Transformer.Translator;

/** The Card Stack holds all the Windows that the Wizard consists of.
 *
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class CardStack
{
    private WelcomeSlide hello;
    private Vector<ConfigurationSlide> slides;
    private Container displayArea;
    private OneNextConfigurationSlide lastSlide;

    /** Construct the Card Stack.
     * @param displayArea The Component Owning the window Area that the Slides will be drawn into.
     * @param msg  Translator for I18n Message output
     * @param baseWindow Window that contains the displayarea.
     * @param curLocale currently active Locale.
     */
    public CardStack(final Container displayArea,
                     final Translator msg,
                     final BaseWindow baseWindow,
                     final String curLocale)
    {
        this.displayArea = displayArea;
        slides = new Vector<ConfigurationSlide>();
        // create all used Slides ...
// Welcome
        hello = new WelcomeSlide(msg, baseWindow, curLocale);
        slides.add(hello);
        // ... and register them with the Base Window
        displayArea.add(hello.getComponent(), hello.getName());
        // Link the Slides
        lastSlide = hello;
// Select Importer
        addSlide(new SelectImporterSlide(msg));
// Select Import Selector
        addSlide(new SelectImportSelectorSlide(msg));
// Select Data Filter
        addSlide(new SelectDataFilterSlide(msg));
// Select Exporter
        addSlide(new SelectExporterSlide(msg));
// Select Export Style
        addSlide(new SelectExportStyleSlide(msg));
// Save Job
        addSlide(new SaveJobSlide(msg));
// Execute Job
        addSlide(new ExecuteJobSlide(msg, baseWindow));
// Finished
        addSlide(new FinishedOrNewJobSlide(msg));
    }

    private void addSlide(OneNextConfigurationSlide slide)
    {
        slides.add(slide);
        displayArea.add(slide.getComponent(), slide.getName());
        lastSlide.addNextSlide(slide);
        lastSlide = slide;
    }

    public final ConfigurationSlide getFirstSlide()
    {
        return hello;
    }

    public final void updateLanguage(final Translator msg)
    {
        for(int i = 0; i < slides.size(); i++)
        {
            final ConfigurationSlide curSlide = slides.get(i);
            curSlide.updateLanguage(msg);
        }
    }

}
