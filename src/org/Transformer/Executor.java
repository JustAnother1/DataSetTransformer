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

import org.Transformer.dataset.DataFilter;
import org.Transformer.dataset.DataSet;
import org.Transformer.exporter.ExportStyle;
import org.Transformer.exporter.Exporter;
import org.Transformer.importer.ImportSelector;
import org.Transformer.importer.Importer;
import org.apache.log4j.Logger;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class Executor
{
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private IntReporter progRep = null;;

    /**
     *
     */
    public Executor()
    {
    }

    public void addProgressReporter(IntReporter rep)
    {
        progRep = rep;
    }

    private void reportProgress(int progress)
    {
        if(null != progRep)
        {
            progRep.reportProgress(progress);
        }
        // else no reporting
    }

    private DataSet[] importData(Job job)
    {
        Importer imp = job.getImporter();
        log.debug("Importer is : " + imp.getName());
        reportProgress(1);

        // Import Data using Import Filter
        ImportSelector impSelector = job.getImportSelector();
        log.debug("Import Selector is : " + impSelector.getName());
        imp.importData(impSelector);
        reportProgress(20);

        if(false == imp.wasSuccessfull())
        {
            log.error("Import Failed");
            reportProgress(21);
            return null;
        }
        reportProgress(33);

        // Importing worked !
        // Get Data Set from Importer
        return imp.getTheData();
    }

    private DataSet[] filterData(Job job, DataSet[] theData)
    {
        DataFilter filter = job.getDataFilter();
        log.debug("Data Filter is : " + filter.getName());
        if(null != filter)
        {
            theData = filter.applyFilterTo(theData);
        }
        return theData;
    }

    private Boolean exportData(Job job, DataSet[] theData)
    {
        Exporter exp = job.getExporter();
        log.debug("Exporter is : " + exp.getName());
        reportProgress(68);

        ExportStyle expStyle = job.getExportStyle();
        log.debug("Export Style is : " + expStyle.getName());
        exp.export(theData, expStyle);
        reportProgress(98);

        return exp.wasSuccessfull();
    }

    public void executeJob(Job job)
    {
        reportProgress(0);
        log.info("=== Starting ===");
        DataSet theData[] = importData(job);
        if(null == theData)
        {
            return;
        }
        reportProgress(35);
        log.info("=== Imported the Data ===");
        printDataSetArray(theData);

        filterData(job, theData);
        reportProgress(66);
        log.info("=== Filtered the Data ===");
        printDataSetArray(theData);

        if(false == exportData(job, theData))
        {
            log.error("Export Failed");
            reportProgress(99);
            return;
        }
        reportProgress(100);
        log.info("=== Success ===");
    }

    private void printDataSetArray(DataSet[] theData)
    {
        log.info("=== Current Data Set ===");
        log.debug("Data Set Entries:");
        for(int k = 0; k < theData.length; k++)
        {
            DataSet curSet = theData[k];
            if(null != curSet)
            {
                log.debug("Entry " + k + " has " + theData[k].getNumberOfAtoms() + " Data Atoms !");
                String[] names = theData[k].getNamesOfAllDataAtoms();
                // System.out.println("Data Atoms : ");
                for (int h = 0; h < names.length; h++)
                {
                    log.debug("(" + names[h] + " : " + theData[k].getDataAtom(names[h]) + ")");
                }
            }
            else
            {
                log.debug("DataSet number " + k + " is null !");
            }
        }
        log.info("========================");
    }

}
