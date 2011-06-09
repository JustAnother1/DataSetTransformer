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

    /*
    private String AttributesToString(NamedNodeMap attributes)
    {
        String res = "";
        for(int i = 0; i < attributes.getLength(); i++)
        {
            Node n = attributes.item(i);
            res = res + n.getNodeName() + " : " + n.getNodeValue() + ", ";
        }
        return res;
    }
*/

    @Override
    public boolean matchesTreeStructure(TreeStructure expectedStructure)
    {
        if(false == valid)
        {
            return false;
        }
        else
        {
            // TODO
            return true;
        }
    }

    /**
     * @param pos if the String starts with \@html\@ then the HTML code will be returned,
     *            otherwise the displayed text will be returned.
     */
    @Override
    public String getLeafFor(String pos)
    {
        boolean htmlOutput = false;
        String selectedData = "";
        if((false == valid) || (null == pos))
        {
            return null;
        }
        else
        {
            if(true == "".equals(pos))
            {
                return null;
            }
            // Output format = HTML ?
            if(true == pos.startsWith("@html@"))
            {
                htmlOutput = true;
                pos = pos.substring(6);
            }
            String[] path = pos.split("/");
            NodeList nl = doc.getChildNodes();
            Node n = doc.getFirstChild(); // just to initialize variable hopefully never read
            boolean found = false;
            boolean NeddsAttr = false;
            for(int i = 0; i < path.length; i++)
            {
                String curPath = path[i];
                String attrName = "neverEver";
                String attrValue = "neverEver";
                // Check if attributes requested
                if(true == curPath.contains("("))
                {
                    String help = curPath.substring(curPath.indexOf('(') + 1, curPath.lastIndexOf(')'));
                    // System.out.println("help : " + help);
                    attrName = help.substring(0, help.indexOf(':'));
                    attrValue = help.substring(help.indexOf(':') + 1);
                    // System.out.println("Name : " + attrName);
                    // System.out.println("Value : " + attrValue);
                    curPath = curPath.substring(0, curPath.indexOf('('))
                              + curPath.substring(curPath.lastIndexOf(')') + 1);
                    // System.out.println("new Path : " + curPath);
                    NeddsAttr = true;
                }
                else
                {
                    NeddsAttr = false;
                }
                // Check for Output Modifier
                if(true == curPath.contains("@"))
                {
                    // System.out.println("Found AT !!!!!");
                    String help = curPath.substring(curPath.indexOf('@') + 1);
                    selectedData = help;
                    curPath = curPath.substring(0, curPath.indexOf('@'));
                }
                else
                {
                    selectedData = "Text";
                }
                for(int k = 0; k < nl.getLength(); k++)
                {
                    found = false;
                    n = nl.item(k);
                    if(true == curPath.equals(n.getNodeName()))
                    {
                        if(false == NeddsAttr)
                        {
                            // System.out.println("Found " + curPath + " at index " + k);
                            found = true;
                            break;
                        }
                        else
                        {
                            // if (true == attrValue.equals(n.getAttributes().getNamedItem(attrName).getNodeValue()))
                            if(true == AttributeValueEquals(attrValue, attrName, n))
                            {
                                // System.out.println("Found " + curPath + " with attribute " + attrName + ":" + attrValue + " at index " + k);
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if(false == found)
                {
                    if(true == NeddsAttr)
                    {
                        System.out.println("Could not find " + curPath + " with Attribute " + attrName + ":" + attrValue + "  !");
                    }
                    else
                    {
                        System.out.println("Could not find " + curPath);
                    }
                    return null;
                }
                else
                {
                    nl = n.getChildNodes();
                }
            }
            if(true == "Text".equals(selectedData))
            {
                // System.out.println("Text of " + pos + " is " + getTextFromNode(n, htmlOutput));
                return getTextFromNode(n, htmlOutput);
            }
            else
            {
                // System.out.println("Attribute " + selectedData + " of " + pos + " is " + getAttributeFromNode(n, selectedData));
                return getAttributeFromNode(n, selectedData);
            }
        }
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(TransformerFactoryConfigurationError e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(TransformerException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        else
        {

            /*
            final XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
            final org.jdom.Document jdoc = new org.jdom.Document();
            jdoc.setRootElement(n.);
            String res = xout.outputString(jdoc);
            */


            /*
            System.out.println("Found The Node : " + n);
            System.out.println("Name : " + n.getNodeName());
            System.out.println("Type : " + n.getNodeType());
            System.out.println("Value : " + n.getNodeValue());
            System.out.println("Attributes : " + AttributesToString(n.getAttributes()));
            System.out.println("Text : " + n.getTextContent());
            */


            String res = n.getNodeValue();
            NodeList nl = n.getChildNodes();
            for(int i = 0; i < nl.getLength(); i++)
            {
                n = nl.item(i);
                res = res + getTextFromNode(n, false);

                /*
                System.out.println("Found The Child : " + i);
                System.out.println("Name : " + n.getNodeName());
                System.out.println("Type : " + n.getNodeType());
                System.out.println("Value : " + n.getNodeValue());
                System.out.println("Attributes : " + AttributesToString(n.getAttributes()));
                System.out.println("Text : " + n.getTextContent());
                */
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
