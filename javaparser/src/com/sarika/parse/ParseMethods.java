package com.sarika.parse;

import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParseMethods extends VoidVisitorAdapter {
	 private String methodName=null;
	private Type methodType=null;
	private  List<Parameter> parameters;
	@Override
public void visit(MethodDeclaration n,Object arg){
	 methodName=n.getName();
	 methodType=n.getType();
	parameters=n.getParameters();
	}
	public List<Parameter> getParameters() {
		return parameters;
	}
	public String getMethodName(){
		
		return methodName;
	}
	public Type getMethodType(){
		return methodType;
	}
}
