package org.cocos2dx.cpp;

public class GraphicsElement {
    public String id;
    public boolean created = false;
    public String parentId = "";
    public String rootId = "";

    public GraphicsElement(String id) {
        this.id = id;
    }
}
