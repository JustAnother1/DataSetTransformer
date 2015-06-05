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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

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

    public static void setLogLevel(String LogLevel)
    {
        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try
        {
            final JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            final String logCfg =
            "<configuration>" +
              "<appender name='STDOUT' class='ch.qos.logback.core.ConsoleAppender'>" +
                "<encoder>" +
                  "<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>" +
                "</encoder>" +
              "</appender>" +
              "<root level='" + LogLevel + "'>" +
                "<appender-ref ref='STDOUT' />" +
              "</root>" +
            "</configuration>";
            ByteArrayInputStream bin;
            try
            {
                bin = new ByteArrayInputStream(logCfg.getBytes("UTF-8"));
                configurator.doConfigure(bin);
            }
            catch(UnsupportedEncodingException e)
            {
                // A system without UTF-8 ? - No chance to do anything !
                e.printStackTrace();
                System.exit(1);
            }
        }
        catch (JoranException je)
        {
          // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
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
        setLogLevel("debug");
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
