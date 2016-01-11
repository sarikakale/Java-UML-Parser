package com.sarika.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import net.sourceforge.plantuml.geom.CollectionUtils;

public class ParseClassPlant extends VoidVisitorAdapter {
	private String expression = "";
	List<String> exps = new ArrayList<>();
	List<String> settersGetters = new ArrayList<>();
	Map<String, String> associations = new HashMap<>();
	Map<String, String> associationsMany = new HashMap<>();
	Map<String, String> uses = new HashMap<>();
	HashSet<String> interfaces = new HashSet<String>();
	ParseInterfaces parseInterfaces=new ParseInterfaces();

	public List<File> getClasses() {
		return classes;
	}

	public ParseInterfaces getParseInterfaces() {
		return parseInterfaces;
	}

	public void setParseInterfaces(ParseInterfaces parseInterfaces) {
		this.parseInterfaces = parseInterfaces;
	}

	public void setClasses(List<File> f) {
		this.classes = f;
	}

	public HashSet<String> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(HashSet<String> interfaces) {
		this.interfaces = interfaces;
	}

	private List<File> classes = null;

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {

		getAllClasses();

		String exp = "";

		// extract classes
		// extract interfaces

		// inheritance
			setInterfaces(getParseInterfaces().getInterfaces());
		if (n.isInterface()) {

			exps.add("interface " + n.getName());
			if (!(n.getImplements().isEmpty())) {
				putInterfaces(exp, n, exps);

			}
		} else {

			if (!(n.getImplements().isEmpty())) {
				putInterfaces(exp, n, exps);

			}

			if (isExtends(n)) {
				putExtends(n, exp);
			}

		}

		// has members

		if (hasMembers(n)) {
			putMembers(n);

		}

	}

	public void getAllClasses() {
		// TODO Auto-generated method stub

	}

	public void putMembers(ClassOrInterfaceDeclaration n) {

		String expression = "";
		if (n.isInterface()) {
			interfaces.add(n.getName());
			expression = "interface " + n.getName() + "{\n";
		} else {
			expression = "class " + n.getName() + " {\n";
		}
		for (BodyDeclaration l : n.getMembers()) {

			if (l instanceof ConstructorDeclaration) {
				expression += "+" + ((ConstructorDeclaration) l).getDeclarationAsString(false, false, true) + " \n";
				List<Parameter> parameters = ((ConstructorDeclaration) l).getParameters();
				if (!(parameters.isEmpty())) {
					for (Parameter p : parameters) {
						isUses(p, n);
					}
				}
			} else {

				if (l instanceof MethodDeclaration) {

					if (hasAccessSpecifier(l).equalsIgnoreCase("public")) {
						// main method
						if (mainMethod(l)) {
							// do main method processing
							expression += "{static}" + "+"
									+ ((MethodDeclaration) l).getDeclarationAsString(false, false, true) + " \n";
							processMainMethod(l, n);
							continue;
						}
						if (compareSettersGetters(l)) {

						} else {
							if (n.isInterface()) {
								expression += "+" + ((MethodDeclaration) l).getDeclarationAsString(false, false, true)
										+ " \n";
							} else {

								System.out.println(expression += "+"
										+ ((MethodDeclaration) l).getDeclarationAsString(false, false, true) + " \n");
								List<Parameter> parameters = ((MethodDeclaration) l).getParameters();
								if (!(parameters.isEmpty())) {
									for (Parameter p : parameters) {
										isUses(p, n);
									}
								}
							}

						}
					} else {
						if (n.isInterface()) {
							expression += "+" + ((MethodDeclaration) l).getDeclarationAsString(false, false, true)
									+ " \n";
						}
					}

				} else {
					if (isCollection(l)) {
						getRelationship(l, n);
					} else {
						if (isReferenceType(l)) {
							getRelationshipReference(l, n);
						} else {
							if (hasAccessSpecifier(l).equalsIgnoreCase("public")) {
								expression += processAccessSpecifier(l);
							}
							if (hasAccessSpecifier(l).equalsIgnoreCase("private")) {
								if (analyzeGettersSetters(n, l)) {
									expression += l.toStringWithoutComments().replaceFirst("private", "+") + "\n";
								} else
									expression += processAccessSpecifier(l);
							}
						}
					}

				}
			}
		}
		 displayAssociation();
		 displayUses();
		// intersection();
		exps.add(expression += "}");

	}

	public void processMainMethod(BodyDeclaration l, ClassOrInterfaceDeclaration n) {

		for (Statement s : ((MethodDeclaration) l).getBody().getStmts()) {
			if (s instanceof ExpressionStmt) {
				// s.getChildrenNodes() instanceof GenericDeclaration;
				for (int i = 0; i < classes.size(); i++) {

					String[] classesStrings = classes.get(i).toString().split("\\\\");
					String classesString = classesStrings[classesStrings.length - 1];
					System.out.println("CLASSES STRING:" + classesString);
					int indexOfLast = classesString.lastIndexOf(".");
					classesString = classesString.substring(0, indexOfLast);
					// classesString.replaceAll(".java", "");
					System.out.println("CLASSES STRING:" + classesString);

					if (classesString.equalsIgnoreCase(s.getChildrenNodes().get(0).toString().split(" ")[0])) {
						System.out.println("Main: " + (((MethodDeclaration) l).getBody().getStmts().get(0))
								.getChildrenNodes().toString().split(" ")[0]);
						String mainDec = n.getName() + " ..> " + (s.getChildrenNodes().get(0).toString().split(" ")[0]);

						exps.add(n.getName() + " ..> " + (s.getChildrenNodes().get(0).toString().split(" ")[0]));

					}
				}

			}
		}

	}

	public boolean mainMethod(BodyDeclaration l) {
		String mainDeclaration = ((MethodDeclaration) l).getName();
		if (mainDeclaration.equals("main")) {
			return true;
		}
		return false;
	}

	// access specifiers
	public String hasAccessSpecifier(BodyDeclaration l) {
		String tokens[] = l.toStringWithoutComments().split(" ");
		/* System.out.println("ACCESS SPECIFIER" + tokens[0]); */
		if (tokens[0].equalsIgnoreCase("public")) {
			return "public";
		} else {
			if (tokens[0].equalsIgnoreCase("private")) {
				return "private";
			} else {
				if (tokens[0].equalsIgnoreCase("protected")) {
					return "protected";
				} else
					return "";
			}

		}
	}

	// putting =,-,#,~
	public String processAccessSpecifier(BodyDeclaration l) {
		String expression = "";
		if (hasAccessSpecifier(l).equalsIgnoreCase("public")) {
			expression = l.toStringWithoutComments().replaceFirst("public", "+") + "\n";

		}
		if (hasAccessSpecifier(l).equalsIgnoreCase("private")) {
			expression = l.toStringWithoutComments().replaceFirst("private", "-") + "\n";
		}
		return expression;
	}

	public boolean isReferenceType(BodyDeclaration l) {
		CharSequence arrayBracks = "[]";
		if (!(((FieldDeclaration) l).getType() instanceof PrimitiveType)
				&& !(((FieldDeclaration) l).getType().toStringWithoutComments().equals("String"))
				&& !(((FieldDeclaration) l).getType().toStringWithoutComments().contains(arrayBracks))) {

			return true;
		} else {
			return false;
		}
	}

	public void getRelationshipReference(BodyDeclaration l, ClassOrInterfaceDeclaration n) {

		String expression = "";

		Node c = ((FieldDeclaration) l).getType().getChildrenNodes().get(0);

		expression = n.getName() + " -->\"1\"" + c.toString();

		associations.put(c.toString(),n.getName() );
		//exps.add(expression);

	}

	public void getRelationship(BodyDeclaration l, ClassOrInterfaceDeclaration n) {
		String expression = "";

		Node c = ((FieldDeclaration) l).getType().getChildrenNodes().get(0);
		System.out.println("NODE " + c);
		expression = n.getName() + " -->\"*\"" + c.getChildrenNodes().get(0);
		associationsMany.put(c.getChildrenNodes().get(0).toString(), n.getName());
		
		//exps.add(expression);

	}

	public boolean isCollection(BodyDeclaration l) {

		System.out.println("Class Dec Collection: " + ((FieldDeclaration) l).getType().getChildrenNodes().toString());
		if (((FieldDeclaration) l).getType().toStringWithoutComments().startsWith("Collection")
				|| ((FieldDeclaration) l).getType().toStringWithoutComments().startsWith("Set")
				|| ((FieldDeclaration) l).getType().toStringWithoutComments().startsWith("List")
				|| ((FieldDeclaration) l).getType().toStringWithoutComments().startsWith("Map")
				|| ((FieldDeclaration) l).getType().toStringWithoutComments().startsWith("Array")) {

			return true;
		}

		return false;
	}

	public boolean hasMembers(ClassOrInterfaceDeclaration n) {
		if (n.getMembers().isEmpty())
			return false;
		else
			return true;
	}

	public List<String> getExps() {
		return exps;
	}

	public void setExps(List<String> exps) {
		this.exps = exps;
	}

	public void putInterfaces(String exp, ClassOrInterfaceDeclaration n, List<String> exps2) {
		for (ClassOrInterfaceType s : n.getImplements())
			exps.add(n.getName() + " ..|> " + s);

	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void putExtends(ClassOrInterfaceDeclaration n, String expression) {
		exps.add(n.getName() + " --|> " + n.getExtends().get(0));

	}

	public boolean isExtends(ClassOrInterfaceDeclaration n) {
		if (n.getExtends().isEmpty())
			return false;
		else
			return true;
	}

	public void isUses(Parameter p, ClassOrInterfaceDeclaration n) {
		String expression = "";

		CharSequence arrayBrack = "[]";
		if ((!(p.getType() instanceof PrimitiveType)) && (!(p.getType().toStringWithoutComments().equals("String")))
				&& !(p.getType().toStringWithoutComments().contains(arrayBrack))) {

			expression = n.getName() + " ..) " + p.getType().toString();
			
			
			if(interfaces.contains(p.getType().toString())){
			 uses.put(p.getType().toString(),n.getName());
			//exps.add(expression);
			}
		}

	}

	public boolean analyzeGettersSetters(ClassOrInterfaceDeclaration n, BodyDeclaration l) {

		// if private variable
		// check if there is setter getter for it
		// no setter getter
		// make private variable public
		boolean set = false, get = false;

		CharSequence ret = "return";
		CharSequence s = l.getChildrenNodes().get(0).toString();
		CharSequence thisName = "this." + l.getChildrenNodes().get(1).toString();
		for (BodyDeclaration bd : n.getMembers()) {
			if (bd instanceof MethodDeclaration && hasAccessSpecifier(bd).equalsIgnoreCase("public")) {
				if (((MethodDeclaration) bd).getName().startsWith("get") || ((MethodDeclaration) bd).getName()
						.endsWith(l.getChildrenNodes().get(1).toStringWithoutComments())) {
					if (((MethodDeclaration) bd).getChildrenNodes().get(1).toStringWithoutComments().contains(ret)
							&& ((MethodDeclaration) bd).getChildrenNodes().get(1).toStringWithoutComments()
									.contains(l.getChildrenNodes().get(1).toStringWithoutComments())
							&& ((MethodDeclaration) bd).getChildrenNodes().get(0).toStringWithoutComments()
									.equals(l.getChildrenNodes().get(0).toString())) {
						get = true;
						System.out.println("GETTERS " + ((MethodDeclaration) bd).getName());
						settersGetters.add(((MethodDeclaration) bd).getDeclarationAsString(false, false, true));
						System.out.println("EXPS " + settersGetters);
					}
					// ((MethodDeclaration)
					// bd).getChildrenNodes().get(1).toStringWithoutComments().contains(ret)

				}
				if (((MethodDeclaration) bd).getName().startsWith("set") || ((MethodDeclaration) bd).getName()
						.endsWith(l.getChildrenNodes().get(1).toStringWithoutComments())) {
					if (((MethodDeclaration) bd).getParameters().toString().contains(s) && (((MethodDeclaration) bd)
							.getChildrenNodes().get(2).toStringWithoutComments().contains(thisName)
							|| (((MethodDeclaration) bd).getChildrenNodes().get(2).toStringWithoutComments()
									.contains(l.getChildrenNodes().get(1).toString())))) {
						System.out.println("SETTERS " + ((MethodDeclaration) bd).getName());
						set = true;
						settersGetters.add(((MethodDeclaration) bd).getDeclarationAsString(false, false, true));
						System.out.println("EXPS " + settersGetters);

					}
				}

			}
		}
		if (set == true || get == true) {

			return true;
		}
		return false;
	}

	public boolean compareSettersGetters(BodyDeclaration l) {

		if (settersGetters.contains(((MethodDeclaration) l).getDeclarationAsString(false, false, true))) {
			return true;
		} else
			return false;

	}

	public void observerDesignPatter() {

	}

	public void displayAssociation() {
		for (Map.Entry<String, String> entry : associations.entrySet()) {
			System.out.println("Key = " + entry.getValue() + ", Value = " + entry.getKey());
			exps.add(entry.getValue() + " -->\"1\"" + entry.getKey());
		}
		
		for (Map.Entry<String, String> entry : associationsMany.entrySet()) {
			System.out.println("Key = " + entry.getValue() + ", Value = " + entry.getKey());
			exps.add(entry.getValue() + " -->\"*\"" + entry.getKey());
		}

	}

	public void displayUses() {
		// if a class dont show dependency
		for (Map.Entry<String, String> entry : uses.entrySet()) {
			System.out.println(entry.getValue() + "..>" + entry.getKey());
			if (interfaces.contains(entry.getKey())) {
				exps.add(entry.getValue() + "..>" + entry.getKey());
			}

		}
	}
	
	public void intersection(){
		for (Map.Entry<String, String> entry : associations.entrySet()) {
			for (Map.Entry<String, String> ent : uses.entrySet()) {
				if(ent.equals(entry)){
					exps.remove(entry.getValue() + "..>" + entry.getKey());
				}
				
			}
		}
		
		
		for (Map.Entry<String, String> entry : associationsMany.entrySet()) {
			for (Map.Entry<String, String> ent : uses.entrySet()) {
				if(ent.equals(entry)){
					exps.remove(entry.getValue() + "..>" + entry.getKey());
				}
				
			}
		}
		
	}

	

}
