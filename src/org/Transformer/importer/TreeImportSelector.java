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

import java.io.InputStream;
import java.util.Map;
import java.util.Vector;

import org.Transformer.JobUtils;
import org.Transformer.dataset.DataSet;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class TreeImportSelector extends ImportSelector
{
    private String[] Mapping_Position;
    private String[] Mapping_Name;

    /**
     *
     */
    public TreeImportSelector()
    {
        super();
    }

    public void setMapping(String[] positions, String[] Names)
    {
        Mapping_Name = Names;
        Mapping_Position = positions;
    }

    @Override
    public boolean parseToDataSets(InputStream src)
    {
        TreeStructure Tree = new HtmlTreeStructure(src);
        if((null == Mapping_Position) || (null == Mapping_Name))
        {
            System.out.println("No Mapping !");
            return false;
        }
        Vector<DataSet> res = new Vector<DataSet>();

        // TODO more than one row
        DataSet row = new DataSet();
        for(int i = 0; i < Mapping_Position.length; i++)
        {
            String Val = Tree.getLeafFor(Mapping_Position[i]);
            row.addDataAtom(Val, Mapping_Name[i]);
        }
        res.add(row);

        data = (DataSet[])res.toArray(new DataSet[1]);
        return true;
    }

    @Override
    public String getConfig()
    {
        return "" + JobUtils.getConfigTextFor(Mapping_Position, "Mapping_Position") + "\n"
                  + JobUtils.getConfigTextFor(Mapping_Name, "Mapping_Name");
    }

    @Override
    public void setConfig(Map<String, String> cfg)
    {
        Mapping_Position = JobUtils.getStringArrayFromSettingMap(cfg, "Mapping_Position");
        Mapping_Name = JobUtils.getStringArrayFromSettingMap(cfg, "Mapping_Name");
    }

    @Override
    public String getName()
    {
        return "TreeImportSelector";
    }

}
