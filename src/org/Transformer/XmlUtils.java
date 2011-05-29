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

package org.Transformer;

import java.util.Vector;

import org.jdom.Element;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class XmlUtils
{

    public static Element getElementFor(String[] arr, String Name)
    {
        org.jdom.Element res = new org.jdom.Element(Name);
        if(null != arr)
        {
            for(int i = 0; i < arr.length; i++)
            {
                org.jdom.Element curElement = new org.jdom.Element("id" + i);
                curElement.setText(arr[i]);
                res.addContent(curElement);
            }
        }
        return res;
    }

    public static String[] getStringArrayFrom(Element child)
    {
        Vector<String> res = new Vector<String>();
        String curContent = "";
        boolean reachedEnd = false;
        int i = 0;
        // System.out.println("Scanning Element : " + child.getName());
        do
        {
            Element curChild = child.getChild("id" + i);
            if(null != curChild)
            {
                // System.out.println("Found id" + i + " !");
                curContent = curChild.getText();
                // System.out.println("Found Content : " + curContent);
                if(curContent.length() > 0)
                {
                    res.add(curContent);
                }
            }
            else
            {
                // System.out.println("Could not find id" + i + " !");
                curContent = "";
                reachedEnd = true;
            }
            i++;
        }while(false == reachedEnd);
        return res.toArray(new String[i-1]);
    }
}
