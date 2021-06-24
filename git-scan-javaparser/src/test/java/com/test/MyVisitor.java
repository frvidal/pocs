package com.test;

import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

public interface MyVisitor { 
    void visit(MarkerAnnotationExpr mae);
}
