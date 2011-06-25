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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class Executor
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private IntReporter progRep = null;;

    /**
     *
     */
    public Executor()
    {
    }

    public final void addProgressReporter(final IntReporter rep)
    {
        progRep = rep;
    }

    private void reportProgress(final int progress)
    {
        if(null != progRep)
        {
            progRep.reportProgress(progress);
        }
        // else no reporting
    }

    private DataSet[] importData(final Job job)
    {
        final Importer imp = job.getImporter();
        log.debug("Importer is : " + imp.getName());
        log.debug("Importer Configuration is : " + imp.getConfig());
        reportProgress(1);

        // Import Data using Import Filter
        final ImportSelector impSelector = job.getImportSelector();
        log.debug("Import Selector is : " + impSelector.getName());
        log.debug("Import Selector Configuration is : " + impSelector.getConfig());
        imp.importData(impSelector);
        reportProgress(20);

        if(false == imp.wasSuccessfull())
        {
            log.error("Import Failed");
            reportProgress(21);
            return new DataSet[0];
        }
        reportProgress(33);

        // Importing worked !
        // Get Data Set from Importer
        return imp.getTheData();
    }

    private DataSet[] filterData(final Job job, final DataSet[] theData)
    {
        final DataFilter filter = job.getDataFilter();
        if(null != filter)
        {
            log.debug("Data Filter is : " + filter.getName());
            final DataSet[] res = filter.applyFilterTo(theData);
            return res;
        }
        else
        {
            return theData;
        }
    }

    private Boolean exportData(final Job job, final DataSet[] theData)
    {
        final Exporter exp = job.getExporter();
        log.debug("Exporter is : " + exp.getName());
        reportProgress(68);

        final ExportStyle expStyle = job.getExportStyle();
        log.debug("Export Style is : " + expStyle.getName());
        exp.export(theData, expStyle);
        reportProgress(98);

        return exp.wasSuccessfull();
    }

    private boolean isEmpty(final DataSet[] someData)
    {
        if(null == someData)
        {
            return true;
        }
        if(1 > someData.length)
        {
            return true;
        }
        for(int i = 0; i < someData.length; i++)
        {
            if(someData[i].getNumberOfAtoms() > 0)
            {
                return false;
            }
        }
        return true;
    }

    public final void executeJob(final Job job)
    {
        reportProgress(0);
        log.info("=== Starting ===");
        final DataSet[] theData = importData(job);
        if(true == isEmpty(theData))
        {
            log.error("Importer could not import any Data !");
            return;
        }
        reportProgress(35);
        log.info("=== Imported the Data ===");
        printDataSetArray(theData);

        filterData(job, theData);
        if(true == isEmpty(theData))
        {
            log.error("No Data leaft after filtering !");
            return;
        }
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

    private void printDataSetArray(final DataSet[] theData)
    {
        log.info("=== Current Data Set ===");
        log.debug("Data Set Entries:");
        for(int k = 0; k < theData.length; k++)
        {
            final DataSet curSet = theData[k];
            if(null != curSet)
            {
                log.debug("Entry " + k + " has " + theData[k].getNumberOfAtoms() + " Data Atoms !");
                final String[] names = theData[k].getNamesOfAllDataAtoms();
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
