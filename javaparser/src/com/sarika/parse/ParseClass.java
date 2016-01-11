package com.sarika.parse;

import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.MultiTypeParameter;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.TypeDeclarationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParseClass extends VoidVisitorAdapter {
	private String expressionformat = "";

	public String getExpressionformat() {
		return expressionformat;
	}

	public void setExpressionformat(String expressionformat) {
		this.expressionformat = expressionformat;
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		
		String expression = null;
		List<String> expressions = new ArrayList<>();

		if (n.isInterface()) {
			expression = surroundWithInterface(expression, n);
			if (!(n.getImplements().isEmpty())) {
				List<String> expressionImpl = getImplements(expression, n, expressions);

			}
		} else {
			expression = surroundWithBrackets(expression, n);
			if (!(n.getImplements().isEmpty())) {
				List<String> expressionImpl = getImplements(expression, n, expressions);

			}

			if (!(n.getExtends().isEmpty())) {
				String expressionExtend = getExtends(expression, n);
				expressions.add(expressionExtend);
			}
		}

		if (hasMembers(n)) {
			new ParseMethods().visit(n, null);
			expression = surroundMembers(expression, n, expressions);
			expressions.add(expression);
		} else {
			expression = surround(expression);
			expressions.add(expression);
		}

		// expressions.add(expression);
		for (int i = expressions.size(); i > 0; i--) {
			expressionformat = expressionformat + "," + expressions.get(i - 1);
			System.out.println(expressions.get(i - 1));
		}
		/*
		 * for(String exp:expressions){ expressionformat+=exp+","; }
		 */
	}

	private List<String> getImplements(String expression, ClassOrInterfaceDeclaration n, List<String> expressionList) {
		List<ClassOrInterfaceType> cl = n.getImplements();

		for (ClassOrInterfaceType c : cl) {
			String expressions = surround("<<interface>>;" + c.toString()) + "^-.-" + surround(expression);
			expressionList.add(expressions);
		}
		return expressionList;
	}

	public String getExtends(String expression, ClassOrInterfaceDeclaration n) {
		Object c = n.getExtends();
		expression = c.toString() + "^-" + surround(expression);
		return expression;
	}

	public String surround(String expression) {
		expression = "[" + expression + "]";
		return expression;
	}

	public String surroundWithBrackets(String expression, ClassOrInterfaceDeclaration n) {

		expression = n.getName();
		return expression;
	}

	public String surroundWithInterface(String expression, ClassOrInterfaceDeclaration n) {

		expression = "<<interface>>;" + n.getName();
		return expression;
	}

	public boolean hasMembers(ClassOrInterfaceDeclaration n) {

		if (n.getMembers().isEmpty()) {
			return false;
		} else
			return true;

	}

	public String surroundMembers(String expression, ClassOrInterfaceDeclaration n, List<String> expressions) {

		expression = surround(expression + "|" + getFormattedMembers(expression, n, expressions));
		return expression;
	}

	public char getAccessSpecifier(BodyDeclaration l) {
		String tokens[] = l.toString().split(" ");
		System.out.println("ACCESS SPECIFIER" + tokens[0]);
		if (tokens[0].equalsIgnoreCase("public")) {
			return '+';
		} else {
			if (tokens[0].equalsIgnoreCase("private")) {
				return '-';
			} else
				return ' ';

		}
	}

	public String getFormattedMembers(String expression, ClassOrInterfaceDeclaration n, List<String> expressions) {
		/*
		 * ParseMethods parseMethods = new ParseMethods(); parseMethods.visit(n,
		 * null);
		 */
		// For primitive datatypes
		// For methods
		// For arrays
		// For Collections
		// For Objects Declaration
		// String methodName = parseMethods.getMethodName();

		List<BodyDeclaration> list = n.getMembers();
		System.out.println();
		System.out.println(list);
		expression = "";
		for (BodyDeclaration l : list) {
			List<Node> child = l.getChildrenNodes();
			if (l instanceof ConstructorDeclaration) {
				System.out.println(((ConstructorDeclaration) l).getDeclarationAsString());

			} else {
				if (isArray(l.toString())) {

					expression = expression + " " + getAccessSpecifier(l) + child.get(1) + ":"
							+ child.get(0).toString().replace("[]", "(*)") + ";";

				} else {
					if (l instanceof MethodDeclaration) {
						ParseMethods parseMethods = new ParseMethods();
						parseMethods.visit((MethodDeclaration) l, null);
						// uses logic
						expression = expression + " " + getAccessSpecifier(l) + parseMethods.getMethodType().toString()
								+ ":" + parseMethods.getMethodName() + "()" + ";";
						List<Parameter> parameters = parseMethods.getParameters();
						if (!(parameters.isEmpty())) {
							for (Parameter p : parameters) {
								expressions.add(isUses(p, n));
							}
						}
					} else {
						if (isCollection(l, n)) {

							String collectionexp = getRelationship(l, n);
							expressions.add(collectionexp);

						} else {
							if (isReferenceType(l)) {
								String collectionexp = getRelationshipReference(l, n);
								expressions.add(collectionexp);

							} else {

								expression = expression + " " + getAccessSpecifier(l) + child.get(1) + ":"
										+ child.get(0) + ";";
							}
						}
					}
				}
			}

			/*
			 * List<Node> child = l.getChildrenNodes();
			 * 
			 * expression = expression + " " + child.get(1) + ":" + child.get(0)
			 * + ";";
			 */

		}

		return expression;

	}

	public String isUses(Parameter p, ClassOrInterfaceDeclaration n) {
		String expression = "";
		p.getType().getClass();
		System.out.println("Give Me" + p.getType());
		new ParseParameters().visit(p, null);
		
		if(p.getType() instanceof ReferenceType){
			/*System.out.println(((ClassOrInterfaceDeclaration) p.getType()).isInterface());*/
			
		}
		
		if ((!(p.getType() instanceof PrimitiveType)) && (!(p.getType().toString().equals("String")))) {
			
			expression = surround(n.getName()) + "uses -.->" + surround(p.getType().toString());
		}
		return expression;

	}

	private String getRelationshipReference(BodyDeclaration l, ClassOrInterfaceDeclaration n) {
		String expression = "";
	//	System.out.println("TYPE "+((TypeDeclaration)l).getName());
		Node c = ((FieldDeclaration) l).getType().getChildrenNodes().get(0);

		expression = surround(n.getName()) + " - 1[" + c + "]";

		return expression;
	}

	private String getRelationship(BodyDeclaration l, ClassOrInterfaceDeclaration n) {
		// TODO Auto-generated method stub
		String expression = "";

		Node c = ((FieldDeclaration) l).getType().getChildrenNodes().get(0);

		expression = surround(n.getName()) + " - *" + c.getChildrenNodes().toString();

		return expression;

	}

	private boolean isReferenceType(BodyDeclaration l) {

		if (!(((FieldDeclaration) l).getType() instanceof PrimitiveType)
				&& !(((FieldDeclaration) l).getType().toString().equals("String"))) {

			return true;
		} else {
			return false;
		}
	}

	public boolean isArray(String l) {
		CharSequence arrayBracks = "[]";
		if (l.contains(arrayBracks)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCollection(BodyDeclaration l, ClassOrInterfaceDeclaration n) {
		System.out.println("Class Dec : "+((FieldDeclaration) l).getType().getChildrenNodes());

		ParseReference parseReference = new ParseReference();
		parseReference.visit(n, l);
		return parseReference.flag;

	}

}
