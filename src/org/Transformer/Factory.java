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

package org.Transformer;

import org.Transformer.dataset.ConCatFilter;
import org.Transformer.dataset.DataFilter;
import org.Transformer.dataset.RegExpFilter;
import org.Transformer.dataset.RemoveEmptyLinesFilter;
import org.Transformer.exporter.CsvExportStyle;
import org.Transformer.exporter.ExportStyle;
import org.Transformer.exporter.Exporter;
import org.Transformer.exporter.FileExporter;
import org.Transformer.exporter.HtmlExportStyle;
import org.Transformer.importer.CsvImportSelector;
import org.Transformer.importer.FileImporter;
import org.Transformer.importer.ImportSelector;
import org.Transformer.importer.Importer;
import org.Transformer.importer.PagedUrlImporter;
import org.Transformer.importer.TreeImportSelector;
import org.Transformer.importer.UrlImporter;
import org.Transformer.importer.UrlListImporter;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class Factory
{

    public static Importer createImporterFor(String type)
    {
        Importer res = null;
        // All Importers
        if(true == "UrlImporter".equals(type))
        {
            res = new UrlImporter();
        }

        if(true == "UrlListImporter".equals(type))
        {
            res = new UrlListImporter();
        }

        if(true == "PagedUrlImporter".equals(type))
        {
            res = new PagedUrlImporter();
        }

        if(true == "FileImporter".equals(type))
        {
            res = new FileImporter();
        }

        if(null == res)
        {
            System.out.println("Invalid Importer type of " + type);
            return null;
        }

        return res;
    }

    public static ImportSelector createImportSelectorFor(String type)
    {
        ImportSelector res = null;
        // All Importers
        if(true == "CsvImportSelector".equals(type))
        {
            res = new CsvImportSelector();
        }

        if(true == "TreeImportSelector".equals(type))
        {
            res = new TreeImportSelector();
        }
        if(null == res)
        {
            System.out.println("Invalid ImportSelector type of " + type);
            return null;
        }

        return res;
    }

    public static DataFilter createDataFilterFor(String type)
    {
        DataFilter res = null;
        // All Importers
        if(true == "ConCatFilter".equals(type))
        {
            res = new ConCatFilter();
        }

        if(true == "RegExpFilter".equals(type))
        {
            res = new RegExpFilter();
        }

        if(true == "RemoveEmptyLinesFilter".equals(type))
        {
            res = new RemoveEmptyLinesFilter();
        }

        if(null == res)
        {
            System.out.println("Invalid DataFilter type of " + type);
            return null;
        }

        return res;
    }

    public static Exporter createExporterFor(String type)
    {
        Exporter res = null;
        // All Importers
        if(true == "FileExporter".equals(type))
        {
            res = new FileExporter();
        }

        if(null == res)
        {
            System.out.println("Invalid Exporter type of " + type);
            return null;
        }

        return res;
    }

    public static ExportStyle createExportStyleFor(String type)
    {
        ExportStyle res = null;
        // All Importers
        if(true == "CsvExportStyle".equals(type))
        {
            res = new CsvExportStyle();
        }

        if(true == "HtmlExportStyle".equals(type))
        {
            res = new HtmlExportStyle();
        }

        if(null == res)
        {
            System.out.println("Invalid ExportStyle type of " + type);
            return null;
        }

        return res;
    }

}
