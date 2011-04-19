/**
 *
 */
package org.Transformer;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class Translator
{
    private ResourceBundle rb;

    /**
     *
     */
    public Translator()
    {
        final Locale loc = Locale.getDefault();
        setLocale(loc);
    }

    public void setLocale(final Locale curLocale)
    {
        try
        {
            final String prefix = "Lang";
            rb = ResourceBundle.getBundle(prefix, curLocale);
        }
        catch(final MissingResourceException e)
        {
        }
    }

    public String tr(String key)
    {
        if(null != rb)
        {
            String res;
            try
            {
                res = rb.getString(key);
            }
            catch(MissingResourceException e)
            {
                res = key;
            }
            return res;
        }
        else
        {
            return key;
        }
    }

    public String tr(String key, Object... args)
    {
        if(null != rb)
        {
            return MessageFormat.format(rb.getString(key), args);
        }
        else
        {
            return MessageFormat.format(key, args);
        }
    }

}
