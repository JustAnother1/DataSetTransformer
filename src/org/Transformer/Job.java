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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.Transformer.dataset.DataFilter;
import org.Transformer.exporter.ExportStyle;
import org.Transformer.exporter.Exporter;
import org.Transformer.importer.ImportSelector;
import org.Transformer.importer.Importer;


/** Representation of the Job to do.
 * Brings together all information needed to execute the task.
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class Job
{
    private final static String IMPORTER_LINE = "Importer";
    private final static String IMPORT_SELECTOR_LINE = "Import Selector";
    private final static String DATA_FILTER_LINE = "Data Filter";
    private final static String EXPORTER_LINE = "Exporter";
    private final static String EXPORT_STYLE_LINE = "Export Style";
    private final static String CLASS_TYPE_NAME = "type";

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

    public static void writeJobToFile(File cfgFile, Job job)
    {
        try
        {
            FileWriter fw = new FileWriter(cfgFile);

            Importer imp = job.getImporter();
            if(null != imp)
            {
                fw.write("[" + IMPORTER_LINE + "]\n");
                fw.write(CLASS_TYPE_NAME + " = " + imp.getName() + "\n");
                fw.write(imp.getConfig() + "\n");
            }
            ImportSelector impsel = job.getImportSelector();
            if(null != impsel)
            {
                fw.write("[" + IMPORT_SELECTOR_LINE + "]\n");
                fw.write(CLASS_TYPE_NAME + " = " + impsel.getName() + "\n");
                fw.write(impsel.getConfig() + "\n");
            }
            DataFilter df = job.getDataFilter();
            if(null != df)
            {
                fw.write("[" + DATA_FILTER_LINE + "]\n");
                fw.write(CLASS_TYPE_NAME + " = " + df.getName() + "\n");
                fw.write(df.getConfig() + "\n");
            }
            Exporter exp = job.getExporter();
            if(null != exp)
            {
                fw.write("[" + EXPORTER_LINE + "]\n");
                fw.write(CLASS_TYPE_NAME + " = " + exp.getName() + "\n");
                fw.write(exp.getConfig() + "\n");
            }
            ExportStyle expsty = job.getExportStyle();
            if(null != expsty)
            {
                fw.write("[" + EXPORT_STYLE_LINE + "]\n");
                fw.write(CLASS_TYPE_NAME + " = " + expsty.getName() + "\n");
                fw.write(expsty.getConfig() + "\n");
            }
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /** creates a Job from the provided File.
     * If the File does not exist a new Job is returned.
     * @param cfgFile File that contains Job Description
     * @return Job created from cfgFile or new Job
     */
    public static Job readFromFile(File cfgFile)
    {
        Job result = new Job();
        try
        {
            FileReader fr = new FileReader(cfgFile);
            ConfigParser cfgp = new ConfigParser(fr);
            String classType;
            Map<String, String> settings = cfgp.getSettingsOfSection(IMPORTER_LINE);
            if(null != settings)
            {
                classType = settings.get(CLASS_TYPE_NAME);
                Importer imp = Factory.createImporterFor(classType);
                if(null != imp)
                {
                    imp.setConfig(settings);
                    result.setImporter(imp);
                }
                else
                {
                    System.err.println("Could not create the Importer : " + classType);
                }
            }
            else
            {
                System.err.println("File did not contain an Importer !");
            }

            settings = cfgp.getSettingsOfSection(IMPORT_SELECTOR_LINE);
            if(null != settings)
            {
                classType = settings.get(CLASS_TYPE_NAME);
                ImportSelector impsel = Factory.createImportSelectorFor(classType);
                if(null != impsel)
                {
                    impsel.setConfig(settings);
                    result.setImportSelector(impsel);
                }
                else
                {
                    System.err.println("Could not create the Import Selector : " + classType);
                }
            }
            else
            {
                System.err.println("File did not contain an Import Selector !");
            }

            settings = cfgp.getSettingsOfSection(DATA_FILTER_LINE);
            if(null != settings)
            {
                classType = settings.get(CLASS_TYPE_NAME);
                DataFilter df = Factory.createDataFilterFor(classType);
                if(null != df)
                {
                    df.setConfig(settings);
                    result.setDataFilter(df);
                }
                else
                {
                    System.err.println("Could not create the Data Filter : " + classType);
                }
            }
            else
            {
                System.err.println("File did not contain a Data Filter !");
            }

            settings = cfgp.getSettingsOfSection(EXPORTER_LINE);
            if(null != settings)
            {
                classType = settings.get(CLASS_TYPE_NAME);
                Exporter exp = Factory.createExporterFor(classType);
                if(null != exp)
                {
                    exp.setConfig(settings);
                    result.setExporter(exp);
                }
                else
                {
                    System.err.println("Could not create the Exporter : " + classType);
                }
            }
            else
            {
                System.err.println("File did not contain an Exporter !");
            }

            settings = cfgp.getSettingsOfSection(EXPORT_STYLE_LINE);
            if(null != settings)
            {
                classType = settings.get(CLASS_TYPE_NAME);
                ExportStyle expSty = Factory.createExportStyleFor(classType);
                if(null != expSty)
                {
                    expSty.setConfig(settings);
                    result.setExportStyle(expSty);
                }
                else
                {
                    System.err.println("Could not create the Export Style : " + classType);
                }
            }
            else
            {
                System.err.println("File did not contain an Export Style !");
            }

            fr.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return result;
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
