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
package org.Transformer.importer;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Vector;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class HtmlTreeStructure extends TreeStructure
{
    private Document doc;

    /**
     * @param src
     */
    public HtmlTreeStructure(InputStream src)
    {
        super(src);
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("utf8");
        tidy.setOnlyErrors(true);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        doc = tidy.parseDOM(src, null);
        valid = true;
    }

    private boolean hasHtmlOutputPrefix(String pos)
    {
        if(true == pos.startsWith("@html@"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private String removeHtmlOutputPrefix(String pos)
    {
        return pos.substring(6);
    }

    private String[] getStringsForPath(String[] path, NodeList nl, boolean htmlOutput, int index)
    {
        if(null == path)
        {
            return null;
        }
        if(index >= path.length)
        {
            return null;
        }
        if(null == nl)
        {
            return null;
        }
        String curPath = path[index];
        String attrName = null;
        String attrValue = null;
        // Check if attributes requested
        if(true == curPath.contains("("))
        {
            String help = curPath.substring(curPath.indexOf('(') + 1, curPath.lastIndexOf(')'));
            attrName = help.substring(0, help.indexOf(':'));
            attrValue = help.substring(help.indexOf(':') + 1);
            curPath = curPath.substring(0, curPath.indexOf('('))
                      + curPath.substring(curPath.lastIndexOf(')') + 1);
        }
        // else nothing to do as attrName already null !

        // Check for Output Modifier
        String selectedData = null;
        if(true == curPath.contains("@"))
        {
            String help = curPath.substring(curPath.indexOf('@') + 1);
            selectedData = help;
            curPath = curPath.substring(0, curPath.indexOf('@'));
        }
        // else nothing to do as selectedData already null

        Vector<String> res = new Vector<String>();
        // curPath has now only the tag name in it
        if(true == "*".equals(curPath))
        {
            // star can be more than one level of tags
            for(int k = 0; k < nl.getLength(); k++)
            {
                Node n = nl.item(k);
                String[] hlp = getStringsForPath(path, n.getChildNodes(), htmlOutput, index);
                if(null != hlp)
                {
                    for(int i = 0; i < hlp.length; i++)
                    {
                        if(null != hlp[i])
                        {
                            res.add(hlp[i]);
                        }
                    }
                }
            }
            // find in the tags of this node the next path element
            String[] hlp = getStringsForPath(path, nl, htmlOutput, index + 1);
            if(null != hlp)
            {
                for(int i = 0; i < hlp.length; i++)
                {
                    if(null != hlp[i])
                    {
                        res.add(hlp[i]);
                    }
                }
            }
        }

        for(int k = 0; k < nl.getLength(); k++)
        {
            Node n = nl.item(k);
            if(true == curPath.equals(n.getNodeName()))
            {
                if(null == attrName)
                {
                    if(index == (path.length - 1))
                    {
                        if(null == selectedData)
                        {
                            String hlp = getTextFromNode(n, htmlOutput);
                            if(null != hlp)
                            {
                                res.add(hlp);
                            }
                        }
                        else
                        {
                            String hlp = getAttributeFromNode(n, selectedData);
                            if(null != hlp)
                            {
                                res.add(hlp);
                                System.err.println("5-added: " + hlp);
                            }
                        }
                    }
                    else
                    {
                        String[] hlp = getStringsForPath(path, n.getChildNodes(), htmlOutput, index + 1);
                        if(null != hlp)
                        {
                            for(int i = 0; i < hlp.length; i++)
                            {
                                if(null != hlp[i])
                                {
                                    res.add(hlp[i]);
                                }
                            }
                        }
                    }
                }
                else
                {
                    if(true == AttributeValueEquals(attrValue, attrName, n))
                    {
                        if(index == path.length)
                        {
                            if(null == selectedData)
                            {
                                String hlp = getTextFromNode(n, htmlOutput);
                                if(null != hlp)
                                {
                                    res.add(hlp);
                                }
                            }
                            else
                            {
                                String hlp = getAttributeFromNode(n, selectedData);
                                if(null != hlp)
                                {
                                    res.add(hlp);
                                }
                            }
                        }
                        else
                        {
                            String[] hlp = getStringsForPath(path, n.getChildNodes(), htmlOutput, index + 1);
                            if(null != hlp)
                            {
                                for(int i = 0; i < hlp.length; i++)
                                {
                                    if(null != hlp[i])
                                    {
                                        res.add(hlp[i]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return res.toArray(new String[1]);
    }

    /**
     * @param pos if the String starts with \@html\@ then the HTML code will be returned,
     *            otherwise the displayed text will be returned.
     */
    @Override
    public String[] getLeafsFor(String pos)
    {
        if((false == valid) || (null == pos))
        {
            return null;
        }
        if(true == "".equals(pos))
        {
            return null;
        }

        // Output format = HTML ?
        boolean htmlOutput = hasHtmlOutputPrefix(pos);
        if(true == htmlOutput)
        {
            pos = removeHtmlOutputPrefix(pos);
        }
        String[] path = pos.split("/");
        NodeList nl = doc.getChildNodes();

        return getStringsForPath(path, nl, htmlOutput, 0);
    }

    private String getAttributeFromNode(Node n, String Name)
    {
        String res = "";
        NamedNodeMap attributes = n.getAttributes();
        Node att = attributes.getNamedItem(Name);
        if(null != att)
        {
            return att.getNodeValue();
        }
        return res;
    }

    private String getTextFromNode(Node n, boolean html)
    {
        if(true == html)
        {
            try
            {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(n);
                Writer w = new StringWriter();
                StreamResult outputTarget = new StreamResult(w);
                transformer.transform(xmlSource, outputTarget);
                String hlp = w.toString();
                return hlp.substring(hlp.indexOf('>') + 1);
            }
            catch(TransformerConfigurationException e)
            {
                e.printStackTrace();
            }
            catch(TransformerFactoryConfigurationError e)
            {
                e.printStackTrace();
            }
            catch(TransformerException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        else
        {
            String res = n.getNodeValue();
            NodeList nl = n.getChildNodes();
            for(int i = 0; i < nl.getLength(); i++)
            {
                n = nl.item(i);
                res = res + getTextFromNode(n, false);
            }
            return res;
        }
    }

    private boolean AttributeValueEquals(String attrValue, String attrName, Node n)
    {
        if((null == attrValue) || (null == n))
        {
            return false;
        }
        NamedNodeMap nnm = n.getAttributes();
        if(null == nnm)
        {
            return false;
        }
        Node attribute = nnm.getNamedItem(attrName);
        if(null == attribute)
        {
            return false;
        }
        return attrValue.equals(attribute.getNodeValue());
    }

}
