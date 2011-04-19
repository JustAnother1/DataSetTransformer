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

import org.Transformer.dataset.DataFilter;
import org.Transformer.dataset.DataSet;
import org.Transformer.exporter.Exporter;
import org.Transformer.exporter.FileExporter;
import org.Transformer.exporter.HtmlExportStyle;
import org.Transformer.importer.Importer;
import org.Transformer.importer.PagedUrlImporter;
import org.Transformer.importer.TreeImportSelector;
import org.Transformer.importer.UrlImporter;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class Wizard
{

    /**
     *    private char comment = (char)-1;
     */
    public Wizard()
    {
        // TODO Auto-generated constructor stub
    }

    private void execute(String Parameter)
    {
        Job[] jobs = null;
        File configFile = new File(Parameter);
        System.out.println("Parameter is : " + Parameter);

        if(true == configFile.canRead())
        {
            // Read Job Description from File
            jobs = Job.readFromFile(configFile);
        }
        else
        {
            // Ask User for Import Format
            // Ask User for Source
            // Ask user for Import Filter
            // Ask User for Export Format
            // Ask user for Export Filter
            // Ask the User for Export File Name
            // for Now:
            jobs = new Job[1];
            Job theJob = new Job();

            // Import

            // forNow: assume : HTML
            UrlImporter imp = new PagedUrlImporter();
            imp.setSource("http://www.heise.de/newsticker");
            theJob.setImporter(imp);
            // forNow: use default

            String[] Mapping_Position = new String[] {"@html@html/body/div(id:container)/div(id:container_content)/div(id:mitte)/div(id:mitte_links)/div(id:mitte_news)/div(class:tage)"};
            String[] Mapping_Name = new String[] {"link"};

            TreeImportSelector infilt = new TreeImportSelector();
            infilt.setMapping(Mapping_Position, Mapping_Name);
            theJob.setImportSelector(infilt);

            // Filter
/*
            ConCatFilter df = new ConCatFilter();
            // df.setConCatSeperator("");
            String[] concat = new String[1];
            concat[0] = "Text";
            df.setFieldsThatWillBeConCatenated(concat);
            String[] musteq = new String[2];
            musteq[1] = "Author";
            musteq[0] = "Titel";
            df.setFieldThatMustBeEqual(musteq);
            theJob.setDataFilter(df);
*/
            // Export

            // forNow: assume HTML
            FileExporter exp = new FileExporter();
            exp.setTarget("links.html");
            theJob.setExporter(exp);
            // forNow: assume default
            HtmlExportStyle expStyle = new HtmlExportStyle();

            String GlobalStyleDef = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                                    "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"de\" lang=\"de\">" +
                    // "<?xml version='1.0' encoding='utf-8'?>\n<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
            // "<html>" +
            "<head><title>links</title>" +
            "<meta content=\"http://www.w3.org/1999/xhtml; charset=utf-8\" http-equiv=\"Content-Type\"/>" +
            "</head><body>" +
            "%ROW%" +
            "</body></html>";

            String RowStyleDef ="%link%";

            expStyle.setStyleDefinition(GlobalStyleDef, RowStyleDef);

            theJob.setExportStyle(expStyle);

            jobs[0] = theJob;
            Job.writeJobsToFile(new File("meiner.txt"), jobs);
        }
        // Do the Jobs
        for(int i = 0; i < jobs.length; i++)
        {
            Job curJob = jobs[i];
            Importer imp = curJob.getImporter();
            // Import Data using Import Filter
            imp.importData(curJob.getImportSelector());
            if(false == imp.wasSuccessfull())
            {
                System.err.println("Import Failed");
                return;
            }
            // Importing worked !
            // Get Data Set from Importer
            DataSet theData[] = imp.getTheData();

            System.out.println("Data Set Entries:");
            for(int k = 0; k < theData.length; k++)
            {
                System.out.println("Entry " + k + " has " + theData[k].getNumberOfAtoms() + " Data Atoms !");
                String[] names = theData[k].getNamesOfAllDataAtoms();
                // System.out.println("Data Atoms : ");
                for (int h = 0; h < names.length; h++)
                {
                    System.out.println("(" + names[h] + " : " + theData[k].getDataAtom(names[h]) + ")");
                }
            }

            DataFilter filter = curJob.getDataFilter();
            if(null != filter)
            {
                theData = filter.applyFilterTo(theData);
            }

            System.out.println("Data Set Entries:");
            for(int k = 0; k < theData.length; k++)
            {
                System.out.println("Entry " + k + " has " + theData[k].getNumberOfAtoms() + " Data Atoms !");
                String[] names = theData[k].getNamesOfAllDataAtoms();
                // System.out.println("Data Atoms : ");
                for (int h = 0; h < names.length; h++)
                {
                    System.out.println("(" + names[h] + " : " + theData[k].getDataAtom(names[h]) + ")");
                }
            }

            // export
            Exporter exp = curJob.getExporter();
            exp.export(theData, curJob.getExportStyle());
            if(false == exp.wasSuccessfull())
            {
                System.err.println("Export Failed");
                return;
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Wizard w = new Wizard();
        String parameter = "";
        if(0 < args.length)
        {
            parameter = args[0];
        }
        w.execute(parameter);
    }

}
