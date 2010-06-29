/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend.xpath;

public class XPath {


    public static Step descendant(NodeTest nodeTest, Predicate... predicate) {
        return new Step(Axis.Descendant, predicate);
    }

    public static Step descendant(NodeTest nodeTest) {
        return new Step(Axis.Descendant, nodeTest);
    }

    public static Step descendant(Predicate... predicate) {
        return new Step(Axis.Descendant, predicate);
    }

    public static Step following(NodeTest nodeTest) {
        return new Step(Axis.Following, nodeTest);
    }

    public static Step following(Predicate... predicate) {
        return new Step(Axis.Following, predicate);
    }

    public static Step child(Predicate... predicates) {
        return new Step(Axis.Child, predicates);
    }

    public static String relative(Step... step) {
        return compile("", step);
    }
    

    public static String absolute(Step... step) {
        return compile("/", step);
    }

    private static String compile(String prefix, Step[] steps) {
        StringBuilder xpath = new StringBuilder(prefix);
        for(int depth=0; depth!=steps.length;++depth) {
            if(depth > 0) {
                xpath.append("/");
            }
            xpath.append(steps[depth].toString());
        }
        return xpath.toString();
    }

    public static Predicate having(Step... step) {
        return new PredicateLiteral(compile("", step));
    }

    public static Predicate havingTextEqualTo(String text) {
        return new PredicateLiteral(XPath.format("normalize-space(.) = '%s'", text));
    }

    public static Predicate containingText(String text) {
        return new PredicateLiteral(XPath.format("contains(., '%s')", text));
    }

    public static RelationalExprBuilder<Integer> position() {
        return new RelationalExprBuilder<Integer>("position()");
    }

    public static Predicate position(int index) {
        return new PredicateLiteral("position() = " + index);
    }

    public static Predicate not(final Predicate predicate) {
        return new Predicate() {
            @Override
            public String toString() {
                return String.format("not(%s)", predicate.toString());
            }
        };
    }

    /**
     * Wrapper for a call to String.format with XPath. This is simply
     * used to mark certain calls as XPath for IDEs that support language injection
     * 
     * @see String#format(String, Object...)
     */
    public static String format(String xpath, Object... args) {
        return String.format(xpath, args);
    }

    public static class ElementNodeTest implements NodeTest {
        private String name;
        public ElementNodeTest(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static class Attribute implements NodeTest, Predicate {
        private String name;

        public Attribute(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "@" + name;
        }

        public Predicate equalTo(String text) {
            return new PredicateLiteral(String.format("@%s = '%s'", name, text));
        }

    }

    public static NodeTest element(String elementName) {
        return new ElementNodeTest(elementName);
    }

    public static Attribute attribute(String attributeName) {
        return new Attribute(attributeName);
    }


}
