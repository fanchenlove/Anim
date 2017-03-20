package com.fanchen.anim.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author fanchen
 *
 */
public class Node {

    private Element element;

    public Node(String html) {
        this.element = Jsoup.parse(html).body();
    }

    public Node(Element element) {
        this.element = element;
    }

    public Node id(String id) throws Exception {
        return new Node(element.getElementById(id));
    }
    
    public boolean isEmpty(){
    	return element == null;
    }

    public List<Node> list(String cssQuery) {
        List<Node> list = new LinkedList<>();
        Elements elements = element.select(cssQuery);
        for (Element e : elements) {
            list.add(new Node(e));
        }
        return list;
    }
    
    public int childsSize(String cssQuery){
    	return element.select(cssQuery).size();
    }
    
    public int size(){
    	return element.getAllElements().size();
    }

    public String text() {
        return element.text();
    }

    public String text(String cssQuery) {
        try {
            return element.select(cssQuery).first().text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String text(String cssQuery, int start, int end) {
        String s = text(cssQuery);
        if (s == null) {
            return "";
        }
        if (end < 0) {
            return s.substring(start, s.length() + 1 + end);
        }
        return s.substring(start, end);
    }

    public String text(String cssQuery, int start) {
        return text(cssQuery, start, -1);
    }

    public String text(String cssQuery, String regex, int index) {
        String s = text(cssQuery);
        if (s != null) {
            return s.split(regex)[index];
        }
        return "";
    }

    public String attr(String attr) {
        try {
            return element.attr(attr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String attr(String attr, String regex, int index) {
        return attr(attr).split(regex)[index];
    }

    public String attr(String cssQuery, String attr) {
        try {
            return element.select(cssQuery).first().attr(attr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String attr(String cssQuery, String attr, String regex, int index) {
        return attr(cssQuery, attr).split(regex)[index];
    }
    
    public Node last(String cssQuery){
    	return new Node(element.select(cssQuery).last());
    }
    
    public Node first(String cssQuery){
    	return new Node(element.select(cssQuery).first());
    }

    public String html(String cssQuery){
    	return element.select(cssQuery).html();
    }
}
