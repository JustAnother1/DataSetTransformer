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

    /**
     *
     */
    public Executor()
    {
    }

    private DataSet[] importData(final Job job)
    {
        final Importer imp = job.getImporter();
        log.debug("Importer is : " + imp.getName());
        log.debug("Importer Configuration is : " + imp.getConfig());

        // Import Data using Import Filter
        final ImportSelector impSelector = job.getImportSelector();
        log.debug("Import Selector is : " + impSelector.getName());
        log.debug("Import Selector Configuration is : " + impSelector.getConfig());
        imp.importData(impSelector);

        if(false == imp.wasSuccessfull())
        {
            log.error("Import Failed");
            return new DataSet[0];
        }

        // Importing worked !
        // Get Data Set from Importer
        return imp.getTheData();
    }

    private DataSet[] filterData(final Job job, final DataSet[] theData)
    {
        DataSet[] myData = theData;
        for(int i = 0; i < job.getNumberOfDataFilters(); i++)
        {
            final DataFilter filter = job.getDataFilter(i);
            if(null != filter)
            {
                log.debug("Data Filter is : " + filter.getName());
                myData = filter.applyFilterTo(myData);
            }
            // else nothing to do
        }
        return myData;
    }

    private Boolean exportData(final Job job, final DataSet[] theData)
    {
        final Exporter exp = job.getExporter();
        log.debug("Exporter is : " + exp.getName());

        final ExportStyle expStyle = job.getExportStyle();
        log.debug("Export Style is : " + expStyle.getName());
        exp.export(theData, expStyle);

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
        if(null == someData[0])
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
        log.info("=== Starting ===");
        DataSet[] theData = importData(job);
        if(true == isEmpty(theData))
        {
            log.error("Importer could not import any Data !");
            return;
        }
        log.info("=== Imported the Data ===");
        printDataSetArray(theData);

        theData = filterData(job, theData);
        if(true == isEmpty(theData))
        {
            log.error("No Data leaft after filtering !");
            return;
        }
        log.info("=== Filtered the Data ===");
        printDataSetArray(theData);

        if(false == exportData(job, theData))
        {
            log.error("Export Failed");
            return;
        }
        log.info("=== Success ===");
    }

    private void printDataSetArray(final DataSet[] theData)
    {
        log.debug("current Data Set has {} Entries", theData.length);

        log.debug("=== Current Data Set ===");
        log.debug("Data Set Entries:");
        for(int k = 0; k < theData.length; k++)
        {
            final DataSet curSet = theData[k];
            if(null != curSet)
            {
                log.debug("Entry {} has {} Data Atoms !", k,  theData[k].getNumberOfAtoms());
                final String[] names = theData[k].getNamesOfAllDataAtoms();
                for (int h = 0; h < names.length; h++)
                {
                    String help = theData[k].getDataAtom(names[h], 30);
                    log.debug("[{} : {}]", names[h], help);
                }
            }
            else
            {
                log.debug("DataSet number " + k + " is null !");
            }
        }
        log.debug("========================");

    }

}
