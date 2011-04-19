/**
 *
 */
package org.Transformer.Slides;

import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.Transformer.BaseWindow;
import org.Transformer.Job;
import org.Transformer.dataset.DataFilter;
import org.Transformer.dataset.DataSet;
import org.Transformer.exporter.Exporter;
import org.Transformer.importer.Importer;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class JobExecutorTask extends SwingWorker<Integer, Integer>
{
    private JProgressBar pb;
    private Job job;
    private BaseWindow baseWindow;

    /**
     * @param baseWindow
     *
     */
    public JobExecutorTask(JProgressBar pb, Job theJob, BaseWindow baseWindow)
    {
        this.pb = pb;
        job = theJob;
        this.baseWindow = baseWindow;
    }

    @Override
    protected Integer doInBackground() throws Exception
    {
        publish(new Integer(0));
        if(true == isCancelled()){return 0;}
        Importer imp = job.getImporter();
        publish(new Integer(1));
        if(true == isCancelled()){return 1;}
        // Import Data using Import Filter
        imp.importData(job.getImportSelector());
        publish(new Integer(20));
        if(true == isCancelled()){return 20;}
        if(false == imp.wasSuccessfull())
        {
            System.err.println("Import Failed");
            return 21;
        }
        publish(new Integer(33));
        if(true == isCancelled()){return 33;}
        // Importing worked !
        // Get Data Set from Importer
        DataSet theData[] = imp.getTheData();
        publish(new Integer(35));
        if(true == isCancelled()){return 35;}

        DataFilter filter = job.getDataFilter();
        if(null != filter)
        {
            theData = filter.applyFilterTo(theData);
        }

        publish(new Integer(66));
        if(true == isCancelled()){return 66;}

        // export
        Exporter exp = job.getExporter();
        publish(new Integer(68));
        if(true == isCancelled()){return 68;}
        exp.export(theData, job.getExportStyle());
        publish(new Integer(98));
        if(true == isCancelled()){return 98;}
        if(false == exp.wasSuccessfull())
        {
            System.err.println("Export Failed");
            return 99;
        }
        publish(new Integer(100));
        return 100;
    }

    @Override
    protected void process(List<Integer> chunks)
    {
        Integer curState = chunks.get(chunks.size() -1);
        pb.setValue(curState);
    }

    @Override
    protected void done()
    {
        baseWindow.nextButtonPressed();
    }


}
