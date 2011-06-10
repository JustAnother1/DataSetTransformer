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
package org.Transformer.importer;

import org.Transformer.JobSerialize;
import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public abstract class Importer implements JobSerialize
{
    protected DataSet[] theImportedData = new DataSet[1];
    protected boolean ImportSuccessfullyCompleted = false;

    public abstract void setSource(String src);
    public abstract void importData(ImportSelector infilt);

    public boolean wasSuccessfull()
    {
        return ImportSuccessfullyCompleted;
    }

    public DataSet[] getTheData()
    {
        if(null == theImportedData)
        {
            theImportedData[0] = new DataSet();
        }
        return theImportedData;
    }
}
