package com.sarika.parse;

import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParseParameters extends VoidVisitorAdapter {
	
	@Override
	public void visit(Parameter type,Object arg){
		 
		System.out.println("Classorinterface  "+(type.getType()).getParentNode());
		}

}
