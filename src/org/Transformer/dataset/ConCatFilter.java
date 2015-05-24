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
package org.Transformer.dataset;

import java.util.Map;

import org.Transformer.JobUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public class ConCatFilter extends DataFilter
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String[] fieldThatMustBeEqual = new String[0];
    private String[] fieldsThatWillBeConCatenated = new String[0];
    private String conCatSeparator = "";

    private DataSet[] res = null;

    /**
     *
     */
    public ConCatFilter()
    {
    }

    public final void setFieldThatMustBeEqual(final String[] newFieldThatMustBeEqual)
    {
        if(null != newFieldThatMustBeEqual)
        {
            fieldThatMustBeEqual = newFieldThatMustBeEqual;
        }
    }

    public final void setFieldsThatWillBeConCatenated(final String[] newFieldsThatWillBeConCatenated)
    {
        if(null != newFieldsThatWillBeConCatenated)
        {
            fieldsThatWillBeConCatenated = newFieldsThatWillBeConCatenated;
        }
    }

    public final void setConCatSeperator(final String conCatSeperator)
    {
        conCatSeparator = conCatSeperator;
    }

    private void handleFirstDataSet(final DataSet[] theData)
    {
        for(int i = 0; i < fieldThatMustBeEqual.length; i++)
        {
            // copy the Fields
            res[0].addDataAtom(theData[0].getDataAtom(fieldThatMustBeEqual[i]), fieldThatMustBeEqual[i]);
        }
        for(int i = 0; i < fieldsThatWillBeConCatenated.length; i++)
        {
            // copy the Fields
            res[0].addDataAtom(theData[0].getDataAtom(fieldsThatWillBeConCatenated[i]),
                               fieldsThatWillBeConCatenated[i]                         );
        }
    }

    private boolean checkEquals(final DataSet[] theData)
    {
        for(int k = 0; k < fieldThatMustBeEqual.length; k++)
        {
            final String should = res[0].getDataAtom(fieldThatMustBeEqual[k]);
            if(null != should)
            {
                for(int i = 1; i < theData.length; i++)
                {
                    final String is = theData[i].getDataAtom(fieldThatMustBeEqual[k]);
                    if(false == should.equals(is))
                    {
                        log.error("Checked field is different !(" + should + "|" + is + ")");
                        return false;
                    }
                }
            }
            else
            {
                log.error("Checked field should Value is null !");
            }
        }
        return true;
    }

    @Override
    public final DataSet[] applyFilterTo(final DataSet[] theData)
    {
        res = new DataSet[1];
        res[0] = new DataSet();
        if((null == fieldThatMustBeEqual) || (null == fieldsThatWillBeConCatenated) || (null == theData))
        {
            return res;
        }
        if(1 > theData.length)
        {
            return res;
        }

        handleFirstDataSet(theData);

        if(2 > theData.length)
        {
            // only one element
            return res;
        }

        // check Eqals
        if(false == checkEquals(theData))
        {
            return res;
        }
        // else ok -> go on

        // concat
        for(int k = 0; k < fieldsThatWillBeConCatenated.length; k++)
        {
            final StringBuffer sb = new StringBuffer();
            for(int i = 0; i < theData.length; i++)
            {
                sb.append(conCatSeparator + theData[i].getDataAtom(fieldsThatWillBeConCatenated[k]));
            }
            final String line = sb.toString();
            res[0].addDataAtom(line, fieldsThatWillBeConCatenated[k]);
        }

        return res;
    }

    public final String getConfig()
    {
        return "ConCatSeparator = " + conCatSeparator + "\n"
               + JobUtils.getConfigTextFor(fieldThatMustBeEqual, "FieldThatMustBeEqual")
               + JobUtils.getConfigTextFor(fieldsThatWillBeConCatenated, "FieldsThatWillBeConCatenated");
    }

    public final void setConfig(final Map<String, String> cfg)
    {
        conCatSeparator = cfg.get("ConCatSeparator");
        fieldThatMustBeEqual = JobUtils.getStringArrayFromSettingMap(cfg, "FieldThatMustBeEqual");
        fieldsThatWillBeConCatenated = JobUtils.getStringArrayFromSettingMap(cfg, "FieldsThatWillBeConCatenated");
    }

    public final String getName()
    {
        return "ConCatFilter";
    }

}
