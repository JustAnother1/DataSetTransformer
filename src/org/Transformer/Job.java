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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.Transformer.dataset.DataFilter;
import org.Transformer.exporter.ExportStyle;
import org.Transformer.exporter.Exporter;
import org.Transformer.importer.ImportSelector;
import org.Transformer.importer.Importer;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class Job
{
    private Importer theImporter;
    private ImportSelector theImportSelector;
    private DataFilter theDataFilter;
    private Exporter theExporter;
    private ExportStyle theExportStyle;

    /**
     *
     */
    public Job()
    {
    }

    private static Job createJobFromElement(org.jdom.Element cfg)
    {
        Job res = new Job();
        Importer cfgImporter = Factory.createImporterFor(cfg.getChild("Importer"));
        res.setImporter(cfgImporter);
        ImportSelector cfgImportSelector = Factory.createImportSelectorFor(cfg.getChild("ImportSelector"));
        res.setImportSelector(cfgImportSelector);
        DataFilter cfgDataFilter = Factory.createDataFilterFor(cfg.getChild("DataFilter"));
        res.setDataFilter(cfgDataFilter);
        Exporter cfgExporter = Factory.createExporterFor(cfg.getChild("Exporter"));
        res.setExporter(cfgExporter);
        ExportStyle cfgExportStyle = Factory.createExportStyleFor(cfg.getChild("ExportStyle"));
        res.setExportStyle(cfgExportStyle);
        return res;
    }

    private org.jdom.Element GetNodeFor(XmlSerialize obj, String Name)
    {
        org.jdom.Element imp = new org.jdom.Element(Name);
        org.jdom.Element type = new org.jdom.Element("type");
        if(null != obj)
        {
            type.addContent(obj.getName());
            imp.addContent(obj.getConfig());
        }
        imp.addContent(type);
        return imp;
    }

    private org.jdom.Element getXmlSerialization()
    {
        org.jdom.Element res = new org.jdom.Element("Job");

        org.jdom.Element imp = GetNodeFor(theImporter, "Importer");
        res.addContent(imp);

        org.jdom.Element impsel = GetNodeFor(theImportSelector, "ImportSelector");
        res.addContent(impsel);

        org.jdom.Element data = GetNodeFor(theDataFilter, "DataFilter");
        res.addContent(data);

        org.jdom.Element exp = GetNodeFor(theExporter, "Exporter");
        res.addContent(exp);

        org.jdom.Element expsty = GetNodeFor(theExportStyle, "ExportStyle");
        res.addContent(expsty);
        return res;
    }

    public static void writeJobToFile(File cfgFile, Job job)
    {
        Job[] jobs = new Job[1];
        jobs[0] = job;
        writeJobsToFile(cfgFile, jobs);
    }

    public static void writeJobsToFile(File cfgFile, Job[] jobs)
    {
        try
        {
            final FileOutputStream fout = new FileOutputStream(cfgFile);
            final BufferedOutputStream bout = new BufferedOutputStream(fout);
            org.jdom.Element root = new org.jdom.Element("Jobs");
            for(int i = 0; i < jobs.length; i++)
            {
                org.jdom.Element curElement = jobs[i].getXmlSerialization();
                root.addContent(curElement);
            }
            final XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
            final Document doc = new Document();
            doc.setRootElement(root);
            xout.output(doc, bout);
            bout.flush();
            bout.close();
            fout.close();
        }
        catch ( final IOException e )
        {
            e.printStackTrace();
        }
    }

    public static Job[] readFromFile(File cfgFile)
    {
        // Parse configuration
        final SAXBuilder builder = new SAXBuilder();
        org.jdom.Element root = null;
        try
        {
            final Document doc = builder.build(cfgFile);
            root = doc.getRootElement();
        }
        catch(final IllegalStateException e)
        {
            // Root Tag not set
            System.out.println("XML Document does not contain XML Tags !");
        }
        catch (final JDOMException e)
        {
            System.out.println("XML Document "
                      + cfgFile.getAbsolutePath()
                      + " is not a well formed XML File !"
                      + " Parsing failed !");
            e.printStackTrace();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        Vector<Job> foundJobs = new Vector<Job>();
        if(null != root)
        {
            if(true == "Jobs".equals(root.getName()))
            {
                @SuppressWarnings("unchecked")
                final List<org.jdom.Element> le = root.getChildren();
                for(int i = 0; i < le.size(); i++)
                {
                    final org.jdom.Element curElement = le.get(i);
                    foundJobs.add(createJobFromElement(curElement));
                }
            }
            else
            {
                System.out.println("Wrong root Tag of : " + root.getName());
            }
        }
        else
        {
            // File has no root element !
            System.out.println("Could not read tags from " + cfgFile.getAbsolutePath() + " !");
        }

        Job[] res = foundJobs.toArray(new Job[1]);
        return res;
    }

    public void setImporter(Importer aImporter)
    {
        theImporter = aImporter;
    }

    public Importer getImporter()
    {
        return theImporter;
    }

    public void setImportSelector(ImportSelector ainselect)
    {
        theImportSelector = ainselect;
    }

    public ImportSelector getImportSelector()
    {
        return theImportSelector;
    }

    public void setExporter(Exporter exp)
    {
        theExporter = exp;
    }

    public Exporter getExporter()
    {
        return theExporter;
    }

    public void setExportStyle(ExportStyle expStyle)
    {
        theExportStyle = expStyle;
    }

    public ExportStyle getExportStyle()
    {
        return theExportStyle;
    }

    public void setDataFilter(DataFilter filter)
    {
        theDataFilter = filter;
    }

    public DataFilter getDataFilter()
    {
        return theDataFilter;
    }

}
