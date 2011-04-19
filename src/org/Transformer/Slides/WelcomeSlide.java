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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.Transformer.BaseWindow;
import org.Transformer.Translator;
import org.apache.log4j.Logger;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class WelcomeSlide extends OneNextConfigurationSlide implements ActionListener
{
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final JComboBox LangChooser;
    private String curLocale;
    private final BaseWindow bw;
    private Translator msg;
    private final JLabel jLabel;
    private final JLabel welcomeTitle;
    private final JLabel langLabel;

    /**
     * @param msg2
     * @param cfg
     *
     */
    public WelcomeSlide(final Translator msg2,
                        final BaseWindow bw,
                        final String curLocale)
    {
        super();
        this.msg = msg2;
        welcomeTitle = new JLabel();
        jLabel = new JLabel();
        langLabel = new JLabel();
        LangChooser = new JComboBox(detectLanguages());
        LangChooser.setSelectedItem(curLocale);
        LangChooser.addActionListener(this);
        if(null == curLocale)
        {
            this.curLocale = "";
        }
        else
        {
            this.curLocale = curLocale;
        }
        this.bw = bw;
    }


    /** scans for Message Bundle Files.
     * @return List of Languages supported
     */
    public static String[] detectLanguages()
    {
        // TODO Read files from within Jar - remove res Folder from Release
        final Vector<String> vec = new Vector<String>();
        final File root = new File(".");
        final String[] files = root.list();
        if(null != files)
        {
            for(int i = 0; i < files.length; i++)
            {
                if(null == files[i])
                {
                    // String is Null
                    continue;
                }
                if("Lang".length() > files[i].length())
                {
                    // String is to short
                    continue;
                }
                if(false == files[i].startsWith("Lang" + "_"))
                {
                    // files[i] is not a Message Bundle
                    continue;
                }
                final int end = files[i].indexOf(".properties");
                if(end != -1)
                    // Expert Options
                {
                    final String CountryCode = files[i].substring("Lang".length() + 1, end);
                    vec.add(CountryCode);
                }
                // else file[i] is not a Message Bundle
            }
        }
        // else files is null -> no languages
        return vec.toArray(new String[vec.size()]);
    }

    public final void actionPerformed(final ActionEvent e)
    {
        final JComboBox cb = (JComboBox)e.getSource();
        final String newLocale = (String)cb.getSelectedItem();
        if(false == curLocale.equals(newLocale))
        {
            final Locale loc = stringToLocale(newLocale);
            msg.setLocale(loc);
            log.info("Switching Language to " + newLocale);
            curLocale = newLocale;
            bw.updateLanguage(msg);
        }
        // else no change
    }


    private Locale stringToLocale(final String newLocale)
    {
        final Scanner sc = new Scanner(newLocale);
        sc.useDelimiter("_");
        String Language;
        String Country = "";
        String Variant = "";
        if(false == sc.hasNext())
        {
            Language = newLocale;
        }
        else
        {
            Language = sc.next();
            if(true == sc.hasNext())
            {
                Country = sc.next();
            }
            if(true == sc.hasNext())
            {
                Variant = sc.next();
            }
        }
        return new Locale(Language, Country, Variant);
    }

    /**
     * @see org.FriendsUnited.ConfigurationCreator.ConfigurationSlide#getComponent()
     */
    public final Component getComponent()
    {
        log.debug("getting Component for Welcome Slide");
        final JPanel slide = new JPanel();
        final JPanel contentPanel = new JPanel();
        final JPanel jPanel1 = new JPanel();
        updateLanguage(msg);
        contentPanel.setLayout(new BorderLayout());
        //slide.add(LangChooser);
        JLabel blankSpace;
        blankSpace = new JLabel();
        welcomeTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        contentPanel.add(welcomeTitle, BorderLayout.NORTH);
        jPanel1.setLayout(new GridLayout(0, 1));
        jPanel1.add(blankSpace);
        jPanel1.add(jLabel);
        final JPanel langPanel = new JPanel();
        langPanel.setLayout(new GridLayout(1, 2));
        langPanel.add(langLabel);
        langPanel.add(LangChooser);
        jPanel1.add(langPanel);
        contentPanel.add(jPanel1, BorderLayout.CENTER);
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        slide.add(contentPanel, BorderLayout.NORTH);
        return slide;
    }

    public final void updateLanguage(final Translator newMsg)
    {
        msg = newMsg;
        welcomeTitle.setText(msg.tr("ccWsTitel"));
        jLabel.setText(msg.tr("ccWsLine"));
        langLabel.setText(msg.tr("ccWsChooseLang"));
    }

    /**
     * @see org.FriendsUnited.ConfigurationCreator.ConfigurationSlide#getName()
     */
    public final String getName()
    {
        return "Welcome";
    }

    @Override
    public final void actionAfterShow()
    {
        bw.updateLanguage(msg);
    }

    @Override
    public final void actionOnClose()
    {
    }

}
