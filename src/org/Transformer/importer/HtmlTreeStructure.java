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
    private Vector<String> res;

    /**
     * @param src
     */
    public HtmlTreeStructure(final InputStream src)
    {
        super(src);
        final Tidy tidy = new Tidy();
        tidy.setInputEncoding("utf8");
        tidy.setOnlyErrors(true);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        doc = tidy.parseDOM(src, null);
        setValid(true);
    }

    /**
     * @param pos if the String starts with \@html\@ then the HTML code will be returned,
     *            otherwise the displayed text will be returned.
     */
    @Override
    public final String[] getLeafsFor(final String pos)
    {
        if((false == isValid()) || (null == pos) || (true == "".equals(pos)))
        {
            return new String[0];
        }

        // Output format = HTML ?
        final boolean htmlOutput = hasHtmlOutputPrefix(pos);
        final String hlp;
        if(true == htmlOutput)
        {
            hlp = removeHtmlOutputPrefix(pos);
        }
        else
        {
            hlp = pos;
        }

        final String[] path = hlp.split("/");

        final NodeList nl = doc.getChildNodes();
        res = new Vector<String>();
        getStringsForPath(path, nl, htmlOutput, 0);
        return res.toArray(new String[0]);
    }

    private boolean hasHtmlOutputPrefix(final String pos)
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

    private String removeHtmlOutputPrefix(final String pos)
    {
        return pos.substring(6);
    }

    private void getStringsForPath(final String[] path,
                                   final NodeList nl,
                                   final boolean htmlOutput,
                                   final int index)
    {
        if( (null == path) || (null == nl))
        {
            return;
        }
        if(index >= path.length)
        {
            return;
        }

        // curPath has now only the tag name in it
        if(true == "*".equals(path[index]))
        {
            handleWildCardPathElement(nl, path, htmlOutput, index);
        }
        else
        {
            handlePathElement(nl, path, index, htmlOutput);
        }
    }

    private void handlePathElement(final NodeList nl, final String[] path, final int index, final boolean htmlOutput)
    {
        String curPath = path[index];
        String attrName = null;
        String attrValue = null;
        // Check if attributes requested
        if(true == curPath.contains("("))
        {
            final String help = curPath.substring(curPath.indexOf('(') + 1, curPath.lastIndexOf(')'));
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
            final String help = curPath.substring(curPath.indexOf('@') + 1);
            selectedData = help;
            curPath = curPath.substring(0, curPath.indexOf('@'));
        }
        // else nothing to do as selectedData already null

        for(int k = 0; k < nl.getLength(); k++)
        {
            final Node n = nl.item(k);
            if(true == pathValueMatches(curPath, n.getNodeName()))
            {
                if(null == attrName)
                {
                    if(index == (path.length - 1))
                    {
                        if(null == selectedData)
                        {
                            final String hlp = getTextFromNode(n, htmlOutput);
                            if(null != hlp)
                            {
                                res.add(hlp);
                            }
                        }
                        else
                        {
                            final String hlp = getAttributeFromNode(n, selectedData);
                            if(null != hlp)
                            {
                                res.add(hlp);
                            }
                        }
                    }
                    else
                    {
                        getStringsForPath(path, n.getChildNodes(), htmlOutput, index + 1);
                    }
                }
                else
                {
                    if(true == attributeValueEquals(attrValue, attrName, n))
                    {
                        if(index == path.length - 1)
                        {
                            if(null == selectedData)
                            {
                                final String hlp = getTextFromNode(n, htmlOutput);
                                if(null != hlp)
                                {
                                    res.add(hlp);
                                }
                            }
                            else
                            {
                                final String hlp = getAttributeFromNode(n, selectedData);
                                if(null != hlp)
                                {
                                    res.add(hlp);
                                }
                            }
                        }
                        else
                        {
                            getStringsForPath(path, n.getChildNodes(), htmlOutput, index + 1);
                        }
                    }
                }
            }
        }
    }

    private void handleWildCardPathElement(final NodeList nl,
                                           final String[] path,
                                           final boolean htmlOutput,
                                           final int index)
    {
        // star can be more than one level of tags
        for(int k = 0; k < nl.getLength(); k++)
        {
            final Node n = nl.item(k);
            getStringsForPath(path, n.getChildNodes(), htmlOutput, index);
        }
        // find in the tags of this node the next path element
        getStringsForPath(path, nl, htmlOutput, index + 1);
    }

    private String getAttributeFromNode(final Node n, final String Name)
    {
        final String strRes = "";
        final NamedNodeMap attributes = n.getAttributes();
        final Node att = attributes.getNamedItem(Name);
        if(null != att)
        {
            return att.getNodeValue();
        }
        return strRes;
    }

    private String getTextFromNode(final Node n, final boolean html)
    {
        if(true == html)
        {
            try
            {
                final Transformer transformer = TransformerFactory.newInstance().newTransformer();
                final DOMSource xmlSource = new DOMSource(n);
                final Writer w = new StringWriter();
                final StreamResult outputTarget = new StreamResult(w);
                transformer.transform(xmlSource, outputTarget);
                final String hlp = w.toString();
                return hlp.substring(hlp.indexOf('>') + 1);
            }
            catch(final TransformerConfigurationException e)
            {
                e.printStackTrace();
            }
            catch(final TransformerFactoryConfigurationError e)
            {
                e.printStackTrace();
            }
            catch(final TransformerException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        else
        {
            final StringBuffer sb = new StringBuffer();
            sb.append(n.getNodeValue());
            final NodeList nl = n.getChildNodes();
            for(int i = 0; i < nl.getLength(); i++)
            {
                final Node curNode = nl.item(i);
                sb.append(getTextFromNode(curNode, false));
            }
            return sb.toString();
        }
    }

    private boolean pathValueMatches(final String path, final String pattern)
    {
        if(true == path.contains("*"))
        {
            String help = path.replaceAll("\\*", "");
            return help.contains(pattern);
        }
        else
        {
            return path.equals(pattern);
        }
    }

    private boolean attributeValueEquals(final String attrValue, final String attrName, final Node n)
    {
        if((null == attrValue) || (null == n))
        {
            return false;
        }
        final NamedNodeMap nnm = n.getAttributes();
        if(null == nnm)
        {
            return false;
        }
        final Node attribute = nnm.getNamedItem(attrName);
        if(null == attribute)
        {
            return false;
        }
        if(true == attrValue.contains("*"))
        {
            String searched = attrValue.replaceAll("\\*", "");
            String searcharea = attribute.getNodeValue();
            return searcharea.contains(searched);
        }
        else
        {
            return attrValue.equals(attribute.getNodeValue());
        }
    }

}
