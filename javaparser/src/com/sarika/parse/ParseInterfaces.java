package com.sarika.parse;

import java.util.HashSet;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParseInterfaces extends VoidVisitorAdapter {
	HashSet<String> interfaces = new HashSet<String>();
	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object args){
		
		if(n.isInterface()){
		setInterfaces(n.getName());
		}
	}
	public HashSet<String> getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(String interfaceName) {
		interfaces.add(interfaceName);
		
	}

}
