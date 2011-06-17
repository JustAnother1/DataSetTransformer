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
    private DataSet[] theImportedData;
    private boolean importSuccessfullyCompleted = false;

    public abstract void setSource(String src);
    public abstract void importData(ImportSelector infilt);

    protected final void setSuccessfullyCompleted(final boolean value)
    {
        importSuccessfullyCompleted = value;
    }

    public final boolean wasSuccessfull()
    {
        return importSuccessfullyCompleted;
    }

    protected final void setTheData(final DataSet[] value)
    {
        theImportedData = value;
    }

    public final DataSet[] getTheData()
    {
        if(null == theImportedData)
        {
            theImportedData = new DataSet[0];
        }
        return theImportedData;
    }
}
