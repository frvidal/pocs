package com.test;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

public class AppCompilationUnit  implements BaseCompilationUnit {

	private CompilationUnit cu;
 
	AppCompilationUnit (CompilationUnit cu) {
		this.cu = cu;
	}

	@Override
	public void accept(CompilationUnitVisitor v) {
		v.visit(this);
	}
 
}
