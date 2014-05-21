package com.whatever.hackernews.Linked;

/**
 * Created by PetoU on 16/05/14.
 */
public class Node {

    private int data;
    private Node nextNode;


    public Node(int data){
        this.data = data;
    }

    public int getData() {
        return this.data;
    }

    public Node getNextNode() {
        return this.nextNode;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setNextNode(Node nextNode){
        this.nextNode = nextNode;
    }


    public String toString() {
        return "Data: " + this.data;
    }



}
