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
public final class Main
{

    /** not used
     *
     */
    private Main()
    {
    }

    /** Executes the Jobs specified in the Parameter.
     *
     * @param Parameter Filename of File containing Job definitions
     */
    public static void execute(final String Parameter)
    {
        final File configFile = new File(Parameter);
        System.out.println("Parameter is : " + Parameter);

        if(true == configFile.canRead())
        {
            Job jobs = null;
            // Read Job Description from File
            jobs = Job.readFromFile(configFile);
            if(true == jobs.isExecuteable())
            {
                final Executor exec = new Executor();
                exec.executeJob(jobs);
            }
            else
            {
                System.err.println("The Job " + Parameter + " can not be executed !");
            }
        }
        else
        {
            // complain
            System.err.println("Could not read Job File " + Parameter + " !");
            System.out.println("Provide a job File as Parameter !");
        }
    }

    /** Main function of DataSet Transformer Project.
     * @param args command line parameters
     */
    public static void main(final String[] args)
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
            execute(args[0]);
        }
        else
        {
            System.out.println("Provide name of Job File as parameter !");
        }
    }

}
