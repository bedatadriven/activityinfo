package org.activityinfo.client;

import org.activityinfo.client.page.PageId;

import java.util.ArrayList;
import java.util.List;

public class ViewPath {

	public final static int DefaultRegion = 0;
    public final static int SideBar = 1;

    public static List<Node> make(Node... nodes) {
         List<Node> path = new ArrayList<Node>();

         for(Node node : nodes) {
             path.add(node);
         }
         return path;
     }

    public static List<Node> make(PageId... pageIds) {
         List<Node> path = new ArrayList<Node>();

         for(PageId pageId : pageIds) {
             path.add(new Node(DefaultRegion, pageId));
         }
         return path;
     }

    public static class Node {

        public final int regionId;
        public final PageId pageId;

        public Node(int regionId, PageId pageId) {
            this.regionId = regionId;
            this.pageId = pageId;

        }
    }

    public static Node node(int regionId, PageId pageId) {
        return new Node(regionId, pageId);
    }

    public static Node node(PageId pageId) {
        return new Node(DefaultRegion, pageId);
    }
}
