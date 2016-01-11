package com.sarika.parse;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParseVariables extends VoidVisitorAdapter{
	
	@Override
	public void visit(ReferenceType n,Object arg){
		
		
	}

}
