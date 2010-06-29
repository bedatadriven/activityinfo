/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.sigmah.server.report.generator.map;

/**
 *
 * Provides a sequence of latin letters: A...Z, AA..AZ
 *
 * @author Alex Bertram
 */
public class LatinAlphaSequence implements LabelSequence {

    int nextNumber = 0;

    @Override
    public String next() {
        int number = nextNumber ++;
        StringBuilder label = new StringBuilder();
        while(number >= 26) {
            int digit = number / 26;
            label.append((char)(65+digit));
            number -= digit * 26;
        }
        return label.toString();
    }
}
