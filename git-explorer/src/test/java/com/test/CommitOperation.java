package com.test;

import com.google.gson.annotations.Expose;

public class CommitOperation {
    
    @Expose
    final public int beginA;

    @Expose
    final public int endA;

    @Expose
	final public int beginB;

    @Expose
    final public int endB;

    @Expose
    final String blockContent;

    public CommitOperation(int beginA, int endA, int beginB, int endB, String blockContent) {
        this.beginA = beginA;
        this.endA = endA;
        this.beginB = beginB;
        this.endB = endB;
        this.blockContent = blockContent;
    }
}
