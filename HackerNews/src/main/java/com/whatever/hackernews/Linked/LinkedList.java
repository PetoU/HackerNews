package com.whatever.hackernews.Linked;

/**
 * Created by PetoU on 16/05/14.
 */
public class LinkedList {

    private Node head;

    public void insertAtHead(int data) {
        Node newNode = new Node(data);
        newNode.setNextNode(this.head);

        this.head = newNode;

    }

    public String toString() {
        String result = "{";
        Node current = this.head;


        while (current != null) {
            result += current.toString() + ",";
            current = current.getNextNode();
        }

        result += "}";

        return result;

    }
}
