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
package org.Transformer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.Transformer.Slides.CardStack;
import org.Transformer.Slides.ConfigurationSlide;
import org.apache.log4j.Logger;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class BaseWindow implements ActionListener, Runnable
{
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private Translator msg;
    private JFrame dialog;
    private JButton backButton;
    private JButton nextButton;
    private JButton cancelButton;
    private Vector<ConfigurationSlide> slideHistory;
    private ConfigurationSlide curSlide;
    private ImageIcon nextIcon;
    private ImageIcon finishIcon;
    private JPanel slidePanel;
    private CardLayout slideLayout;
    private final CardStack stack;
    private String curLocale;
    private Job theJob;
    /**
     * The String-based action command for the 'Next' button.
     */
    public static final String NEXT_BUTTON_ACTION_COMMAND = "NextButtonActionCommand";
    /**
     * The String-based action command for the 'Back' button.
     */
    public static final String BACK_BUTTON_ACTION_COMMAND = "BackButtonActionCommand";
    /**
     * The String-based action command for the 'Cancel' button.
     */
    public static final String CANCEL_BUTTON_ACTION_COMMAND = "CancelButtonActionCommand";

    /**
     * @param fs
     *
     */
    public BaseWindow()
    {
        theJob = new Job();
        curLocale = getLocale();
        slideHistory = new Vector<ConfigurationSlide>();
        dialog =  new JFrame();
        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        msg = new Translator();
        dialog.setTitle(msg.tr("CreateConfiguration"));

        // Window Size and Position
        dialog.setMinimumSize(new Dimension(200, 100)); // to avoid invisible Window due to no content

        // Move Window to center of Screen
        // Get the size of the screen
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        final int w = dialog.getSize().width;
        final int h = dialog.getSize().height;

        // Move the window
        dialog.setLocation((dim.width-w)/2, (dim.height-h)/2);

        final Image logo = new ImageIcon("logo.gif").getImage();
        if(null == logo)
        {
            System.err.println("Could not read Logo !");
        }
        dialog.setIconImage(logo);


        slidePanel = new JPanel();
        slideLayout = new CardLayout();

        slidePanel.setLayout(slideLayout);
        slidePanel.setOpaque(true);
        slidePanel.setBackground(Color.RED);
        slidePanel.setMinimumSize(new Dimension(300, 300));


        stack = new CardStack(slidePanel, msg, this, curLocale);
        curSlide = stack.getFirstSlide();

        final JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("clouds.jpg"));
        iconLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        iconLabel.setOpaque(true);

        final Container contPane = dialog.getContentPane();
        if(null == contPane)
        {
            log.error("Content Pane is Null !");
            return;
        }
        contPane.setLayout(new BorderLayout());

        contPane.add(iconLabel, BorderLayout.WEST);
        contPane.add(createButtonPane(), BorderLayout.SOUTH);
        contPane.add(slidePanel, BorderLayout.EAST);
    }

    private String getLocale()
    {
        final Locale loc = Locale.getDefault();
        final String locstr;
        if(null == loc)
        {
            locstr = "en_US";
        }
        else
        {
            final String var = loc.getVariant();
            if(var.length() < 1)
            {
                locstr = loc.getLanguage() + "_" + loc.getCountry();
            }
            else
            {
                locstr = loc.getLanguage() + "_" + loc.getCountry() + "_" + loc.getVariant();
            }
        }
        return locstr;
    }



    public final void updateLanguage(final Translator newMsg)
    {
        this.msg = newMsg;
        dialog.setTitle(msg.tr("CreateConfiguration"));
        backButton.setText(msg.tr("ccBackButton"));
        nextButton.setText(msg.tr("ccNextButton"));
        cancelButton.setText(msg.tr("ccCancelButton"));
        if(false == curSlide.hasNextSlide())
        {
            nextButton.setText(msg.tr("ccFinishButton"));
        }
        else
        {
            nextButton.setText(msg.tr("ccNextButton"));
        }
        stack.updateLanguage(msg);
    }

    private JPanel createButtonPane()
    {
        final JPanel buttonPanel = new JPanel();
        final Box buttonBox = new Box(BoxLayout.X_AXIS);
        final JSeparator separator = new JSeparator();
        nextIcon = new ImageIcon("nextIcon.gif");
        finishIcon = new ImageIcon("finishIcon.gif");
        backButton = new JButton(new ImageIcon("backIcon.gif"));
        nextButton = new JButton(nextIcon);
        cancelButton = new JButton(new ImageIcon("cancelIcon.gif"));

        backButton.setActionCommand(BACK_BUTTON_ACTION_COMMAND);
        nextButton.setActionCommand(NEXT_BUTTON_ACTION_COMMAND);
        cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);
        backButton.setText(msg.tr("ccBackButton"));
        nextButton.setText(msg.tr("ccNextButton"));
        cancelButton.setText(msg.tr("ccCancelButton"));

        backButton.addActionListener(this);
        nextButton.addActionListener(this);
        cancelButton.addActionListener(this);
        backButton.setOpaque(true);
        nextButton.setOpaque(true);
        cancelButton.setOpaque(true);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(separator, BorderLayout.NORTH);
        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        buttonBox.add(backButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(cancelButton);
        buttonBox.setOpaque(true);
        buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);
        buttonPanel.setOpaque(true);
        return buttonPanel;
    }


    public final void run()
    {
        setCurrentPanel(curSlide);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void setCurrentPanel(final ConfigurationSlide cs)
    {
        log.debug("setting current Panel to " + cs.getName());
        curSlide = cs;
        // Next or Finish on last Slide
        if(false == curSlide.hasNextSlide())
        {
            nextButton.setIcon(finishIcon);
            nextButton.setText(msg.tr("ccFinishButton"));
        }
        else
        {
            nextButton.setIcon(nextIcon);
            nextButton.setText(msg.tr("ccNextButton"));
        }
        // Back Enable / Disabled on first Slide
        if(1 > slideHistory.size())
        {
            backButton.setEnabled(false);
        }
        else
        {
            backButton.setEnabled(true);
        }
        slideLayout.show(slidePanel, cs.getName());
        curSlide.setJob(theJob);
        curSlide.actionAfterShow();
        slidePanel.revalidate();
        slidePanel.repaint();
    }

    @Override
    public final void actionPerformed(final ActionEvent e)
    {
        if (e.getActionCommand().equals(CANCEL_BUTTON_ACTION_COMMAND))
        {
            cancelButtonPressed();
        }
        else if (e.getActionCommand().equals(BACK_BUTTON_ACTION_COMMAND))
        {
            backButtonPressed();
        }
        else if (e.getActionCommand().equals(NEXT_BUTTON_ACTION_COMMAND))
        {
            nextButtonPressed();
        }
        else
        {
            System.err.println(e.getActionCommand());
        }
    }

    /**
     *
     */
    public void cancelButtonPressed()
    {
        dialog.dispose();
    }

    /**
     *
     */
    public void nextButtonPressed()
    {
        curSlide.actionOnClose();
        theJob = curSlide.getJob();
        final ConfigurationSlide next = curSlide.getNextSlide();
        if(null != next)
        {
            slideHistory.add(curSlide);
            setCurrentPanel(next);
        }
        else
        {
            // finished Dialog
            dialog.dispose();
        }
    }

    /**
     *
     */
    public void backButtonPressed()
    {
        curSlide.actionOnClose();
        theJob = curSlide.getJob();
        ConfigurationSlide last = null;
        try
        {
            last = slideHistory.lastElement();
            slideHistory.remove(slideHistory.size() -1);
        }
        catch(final NoSuchElementException e)
        {
            // No lastSlide in Vector
        }
        catch(final ArrayIndexOutOfBoundsException e)
        {
            // could not remove size -1
        }
        if(null != last)
        {
            setCurrentPanel(last);
        }
    }

}
