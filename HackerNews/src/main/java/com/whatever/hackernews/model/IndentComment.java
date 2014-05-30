package com.whatever.hackernews.model;

/**
 * Created by PetoU on 30/05/14.
 *
 * Class to encapsulate comment string and indentation data
 *
 */
public class IndentComment {

    public int indentation;
    public String comment;

    public IndentComment(int indentation, String comment ) {
        this.indentation = indentation;
        this.comment = comment;
    }

}
