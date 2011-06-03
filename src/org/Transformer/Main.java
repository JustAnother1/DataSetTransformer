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

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.xml.DOMConfigurator;

/** Main Class of DataSet Transformer Project.
 * parses the command line and then starts Job Processing or the Wizard(GUI).
 *
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

    /** Executes the Jobs specified in the Parameter.
     *
     * @param Parameter Filename of File containing Job definitions
     */
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
            Executor exec = new Executor();
            exec.executeJob(curJob);
        }
    }

    /** Main function of DataSet Transformer Project.
     * @param args command line parameters
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
            rootlog.setLevel(Level.DEBUG); // TODO ERROR
            final Layout layout = new SimpleLayout();
            final Appender app = new ConsoleAppender(layout);
            rootlog.addAppender(app);
        }

        if(0 < args.length)
        {
            if(false == "-edit".equals(args[0]))
            {
                execute(args[0]);
            }
            else
            {
                Job job[] = Job.readFromFile(new File(args[1]));
                final BaseWindow ConfigCreator = new BaseWindow(job[0]);
                javax.swing.SwingUtilities.invokeLater(ConfigCreator);
            }
        }
        else
        {
            final BaseWindow ConfigCreator = new BaseWindow();
            javax.swing.SwingUtilities.invokeLater(ConfigCreator);
        }
    }

}
