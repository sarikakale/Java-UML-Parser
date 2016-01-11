package com.sarika.parse;

import java.io.IOException;
import java.lang.reflect.GenericDeclaration;
import java.util.Collection;
import java.util.Collections;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParseReference extends VoidVisitorAdapter {
	public boolean flag = false;
	
	@Override
	public void visit(ReferenceType n,Object l){
	
		
		if(((FieldDeclaration)l).getType().toString().contains("Collection")){
		
		flag=true;
			}
			
		
	}

}
