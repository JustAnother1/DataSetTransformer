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

import org.Transformer.Job;


/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public abstract class OneNextConfigurationSlide implements ConfigurationSlide
{
    private ConfigurationSlide nextSlide;
    protected Job job;

    /**
     *
     */
    public OneNextConfigurationSlide()
    {
        nextSlide = null;
    }

    public final void addNextSlide(final ConfigurationSlide next)
    {
        nextSlide = next;
    }

    public final boolean hasNextSlide()
    {
        if(null != nextSlide)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public final ConfigurationSlide getNextSlide()
    {
        return nextSlide;
    }

    @Override
    public void setJob(Job theJob)
    {
        job = theJob;
        update();
    }

    public void update()
    {

    }

    @Override
    public Job getJob()
    {
        return job;
    }
}
