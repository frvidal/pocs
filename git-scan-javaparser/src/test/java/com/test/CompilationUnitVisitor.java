package com.test;

import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

public interface CompilationUnitVisitor  {
    void visit(MarkerAnnotationExpr mae);
}
