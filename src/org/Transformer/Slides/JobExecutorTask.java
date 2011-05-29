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
package org.Transformer.Slides;

import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.Transformer.BaseWindow;
import org.Transformer.Executor;
import org.Transformer.IntReporter;
import org.Transformer.Job;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class JobExecutorTask extends SwingWorker<Integer, Integer> implements IntReporter
{

    private JProgressBar pb;
    private Job job;
    private BaseWindow baseWindow;
    private int curProgress = 0;

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
        Executor exec = new Executor();
        exec.addProgressReporter(this);
        exec.executeJob(job);
        return curProgress;
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

    @Override
    public void reportProgress(int progress)
    {
        curProgress = progress;
        publish(new Integer(progress));
    }


}
