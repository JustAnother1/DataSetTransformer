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

import java.util.Scanner;
import java.util.Vector;

/**
 * @author Lars P&ouml;tter
 * (<a href=mailto:Lars_Poetter@gmx.de>Lars_Poetter@gmx.de</a>)
 */
public final class Tool
{

    /**
     *
     */
    private Tool()
    {
    }

    public static String[] stringToArray(final String text)
    {
        final Scanner sc = new Scanner(text);
        final Vector<String> vec = new Vector<String>();
        while(sc.hasNextLine())
        {
            vec.add(sc.nextLine());
        }
        return vec.toArray(new String[1]);
    }

}
