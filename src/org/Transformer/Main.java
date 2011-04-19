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
import org.Transformer.importer.Importer;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class Main
{

    /** not used
     *
     */
    public Main()
    {
    }

    public static void printDataSetArray(DataSet[] theData)
    {
        System.out.println("Data Set Entries:");
        for(int k = 0; k < theData.length; k++)
        {
            DataSet curSet = theData[k];
            if(null != curSet)
            {
                System.out.println("Entry " + k + " has " + theData[k].getNumberOfAtoms() + " Data Atoms !");
                String[] names = theData[k].getNamesOfAllDataAtoms();
                // System.out.println("Data Atoms : ");
                for (int h = 0; h < names.length; h++)
                {
                    System.out.println("(" + names[h] + " : " + theData[k].getDataAtom(names[h]) + ")");
                }
            }
            else
            {
                System.out.println("DataSet number " + k + " is null !");
            }
        }
    }

    public static void executeJob(Job curJob)
    {
        // Import Data using Import Filter
            Importer imp = curJob.getImporter();
            imp.importData(curJob.getImportSelector());
            if(false == imp.wasSuccessfull())
            {
                System.err.println("Import Failed");
                return;
            }
            // Importing worked !
        // Get Data Set from Importer
            DataSet theData[] = imp.getTheData();

            printDataSetArray(theData);

        // Filter the Data
            DataFilter filter = curJob.getDataFilter();
            if(null != filter)
            {
                theData = filter.applyFilterTo(theData);
            }

            printDataSetArray(theData);

        // export
            Exporter exp = curJob.getExporter();
            exp.export(theData, curJob.getExportStyle());
            if(false == exp.wasSuccessfull())
            {
                System.err.println("Export Failed");
                return;
            }
    }

    public static void execute(String Parameter)
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
            // complain
            System.err.println("Could not read Job File " + Parameter + " !");
            System.out.println("Start without Parameter to use Wizard, or provide a job File as Parameter !");
        }
        // Do the Jobs
        for(int i = 0; i < jobs.length; i++)
        {
            Job curJob = jobs[i];
            executeJob(curJob);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Logger rootlog = null;
        // Start Log4J Logger
        if(true == (new File("log4j.xml")).canRead())
        {
            DOMConfigurator.configureAndWatch("log4j.xml");
        }
        else
        {
            // Fall Back - No log4j.xml to configure Logging
            rootlog = Logger.getRootLogger();
            rootlog.setLevel(Level.ERROR);
            final Layout layout = new SimpleLayout();
            final Appender app = new ConsoleAppender(layout);
            rootlog.addAppender(app);
        }

        if(0 < args.length)
        {
            execute(args[0]);
        }
        else
        {
            final BaseWindow ConfigCreator = new BaseWindow();
            javax.swing.SwingUtilities.invokeLater(ConfigCreator);
        }
    }

}
