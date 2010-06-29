/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath;

public enum Axis {

    Child,
    Parent,
    Ancestor,
    FollowingSibling("following-sibling"),
    PrecedingSibling("preceding-sibling"),
    Descendant,
    Following,
    Preceding,
    Attribute,
    Self,
    DescendantOrSelf("descendant-or-self"),
    AncestorOrSelf("ancestor-or-self");

    private final String keyword;

    Axis() { keyword = toString().toLowerCase();  }
    Axis(String keyword) { this.keyword = keyword; }
    public String getKeyword() {
        return keyword;
    }
}
