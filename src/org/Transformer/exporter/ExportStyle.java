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
package org.Transformer.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.Transformer.JobSerialize;
import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public abstract class ExportStyle implements JobSerialize
{
    public abstract void setOutputStream(OutputStream out);
    public abstract void setDatabaseConnection(Connection dbconnection);
    public abstract boolean formatTheData(DataSet[] theData) throws IOException, SQLException;
}
