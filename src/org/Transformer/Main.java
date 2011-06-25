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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static void execute(final String Parameter, final Logger log)
    {
        final File configFile = new File(Parameter);
        log.info("Parameter is : " + Parameter);

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
                log.error("The Job " + Parameter + " can not be executed !");
            }
        }
        else
        {
            // complain
            log.error("Could not read Job File " + Parameter + " !");
            log.info("Provide a job File as Parameter !");
        }
    }

    /** Main function of DataSet Transformer Project.
     * @param args command line parameters
     */
    public static void main(final String[] args)
    {
        final Logger logger = LoggerFactory.getLogger(Main.class);

        if(0 < args.length)
        {
            execute(args[0], logger);
        }
        else
        {
            logger.info("Provide name of Job File as parameter !");
        }
    }

}
