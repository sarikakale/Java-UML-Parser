package com.sarika.parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.TypeDeclarationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParseAccessSpecifier {

	public static void main(String[] args) throws ParseException {
		try {

			ReferenceType referenceType = new ReferenceType();
			FileInputStream in = new FileInputStream("F:/SampleTests/ClassA.java");
			//FileInputStream in1 = new FileInputStream("F:/SampleTestCases/B.java");
			CompilationUnit compilationUnit;
			//CompilationUnit compilationUnit1;
			compilationUnit = JavaParser.parse(in);
			//compilationUnit1 = JavaParser.parse(in1);
		//	new ParseClass().getComp(compilationUnit1);
			//new ParseClass().visit(compilationUnit, null);
			/*
			 * new ParseVariables().visit(compilationUnit,null); 	 */
			   new ParseMethodss().visit(compilationUnit,null);
			  
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

	private static class ParseClass extends VoidVisitorAdapter {
		CompilationUnit cu;

		public void getComp(CompilationUnit cu) {
			this.cu = cu;
		}

		/*
		 * public void visit(ArrayAccessExpr n, Object arg) {
		 * System.out.println(n.getIndex()); }
		 */
		/*
		 * @Override public void visit(MethodDeclaration n, Object arg) {
		 * System.out.println(n.getParameters());
		 * System.out.println(n.getType()); System.out.println(n.getName()); }
		 */
		/*
		 * @Override public void visit(FieldAccessExpr n, Object arg) {
		 * System.out.println(n.getField()); }
		 */
		/*
		 * @Override
		 * public void visit(ClassOrInterfaceDeclaration n, Object
		 * arg) { System.out.println(n.getMembers()); List<BodyDeclaration> list
		 * = n.getMembers();
		 * 
		 * for (BodyDeclaration l : list) { System.out.println("List " + l);
		 * System.out.println(l.toString().startsWith("public"));
		 * System.out.println(l.getChildrenNodes());
		 * 
		 * } System.out.println(n.isInterface()); }
		 */

		/*
		 * @Override public void visit(FieldDeclaration n, Object arg) {
		 * System.out.println(n.getVariables());
		 * 
		 * }
		 */
		/*
		 * @Override public void visit(MethodDeclaration n, Object arg) {
		 * System.out.println();
		 * 
		 * 
		 * 
		 * }
		 */

		/*
		 * @Override public void visit(ReferenceType n, Object arg) {
		 * System.out.println(n.getType());
		 * 
		 * 
		 * 
		 * }
		 */
		/*
		 * @Override public void visit(Parameter n, Object arg) {
		 * System.out.println(n.getType()); }
		 */
		/*
		 * @Override public void visit(PrimitiveType n, Object arg) {
		 * System.out.println(n.getType());
		 * 
		 * 
		 * 
		 * }
		 */
		/*
		 * @Override public void visit(ClassOrInterfaceType n, Object arg) {
		 * System.out.println(n.getName());
		 * 
		 * 
		 * 
		 * }
		 */
		/*
		 * @Override public void visit(Parameter n, Object arg) {
		 * System.out.println(n.getType());
		 * 
		 * }
		 */
		/*
		 * @Override public void visit(ReferenceType n, Object arg) {
		 * System.out.println(n.getType());
		 * 
		 * }
		 */

		/*@Override
		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
			System.out.println(n.getMembers());
		}*/
	}

	/*private static class ParseVariables extends VoidVisitorAdapter {
		@Override
		public void visit(VariableDeclarator n, Object arg) {
			System.out.println(n.getParentNode());
		}
		
		
			@Override
		public void visit(ArrayAccessExpr a, Object args){
			System.out.println("HIIII");
			System.out.println("Array "+a.toStringWithoutComments());
		}
	}*/
}

	class ParseMethodss extends VoidVisitorAdapter {
		CharSequence thisName="this.message";
		/*@Override
		public void visit(ClassOrInterfaceType n, Object arg) {
			System.out.println(n.getParameters());
			System.out.println("Children "+n.getParameters().toString().contains("String"));
			System.out.println("children nodes "+n.getChildrenNodes());
			System.out.println( n.getChildrenNodes().get(1).toStringWithoutComments().contains(thisName));
			ClassOrInterfaceType c=new ClassOrInterfaceType();
			System.out.println("Class Name: "+n.getName());
			System.out.println(n.getChildrenNodes());
		}*/
		@Override
		public void visit(MethodDeclaration n, Object arg) {
			System.out.println(n.getParameters());
			System.out.println("Children "+n.getParameters().toString().contains("String"));
			System.out.println("children nodes "+n.getChildrenNodes());
			System.out.println( n.getChildrenNodes().get(1).toStringWithoutComments().contains(thisName));
			ClassOrInterfaceType c=new ClassOrInterfaceType();
			System.out.println("Class Name: "+n.getName());
			//System.out.println(n.getChildrenNodes());
			System.out.println((n.getBody().getStmts().get(0)));
			System.out.println((n.getBody().getStmts().get(0)) instanceof ExpressionStmt);
			System.out.println((n.getBody().getStmts().get(0)).getChildrenNodes() instanceof GenericDeclaration/*.toString().contains("ClassB")*/);
			
			/*Class object = com.sarika.test.ClassA.class;
			Method method;
			method.
			for(Method m:object.getMethods()){
			System.out.println("Reflection :");*/
			}
		}
		
		/*
		@Override
		public void visit(ArrayAccessExpr a, Object args){
			System.out.println("HIIII");
			System.out.println("Array "+a.toStringWithoutComments());
		}*/
		
		
		/*@Override
		public void visit(VariableDeclarator a, Object args){
			System.out.println("HIIII");
			System.out.println("Array "+a.getChildrenNodes());
		}
		*/
		
		
	
	/*
	private static class ParseConstructor extends VoidVisitorAdapter {
		@Override
		public void visit(ConstructorDeclaration n, Object arg) {
			
			System.out.println();
			
		}
		
	}
*/

