package com.sarika.parse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import net.sourceforge.plantuml.SourceStringReader;

public class Parse {

	public static void main(String[] args) {

		ParseSourceCode parseSourceCode = new ParseSourceCode();

		CompilationUnit cu = null;
		CompilationUnit cu1 = null;
		try {
			// get the classes && surround them with [] find extends and
			// implements
			// if variables present,get the variable put them in [] of class
			// after |
			// get the methods
			// if methods contain reference types dependency
			// write it in a file
			ParseClassPlant parseClassPlant1 = new ParseClassPlant();
			String expressionFormat = null;
			String dir = "F:/SampleTestCases/";
			String dire = "C:/Users/SarikaNitinKale/workspace/UMLParser/javaparser/src/";
			File directory = new File(args[0]);
			File[] files = directory.listFiles();

			for (File fileLoop : files) {
				System.out.println(fileLoop.getName());
				if (fileLoop.isFile() && fileLoop.getName().endsWith(".java")) {

					cu = parseSourceCode.parseCode(args[0] + fileLoop.getName());
					// cu =
					// parseSourceCode.parseCode("F:/SampleTestCases/Component.java");
					ParseClass parseClass = new ParseClass();
					parseClass.visit(cu, null);
					System.out.println(expressionFormat += parseClass.getExpressionformat());
				}
			}
			try {
				/*// URL myURL = new URL("http://yuml.me/diagram/scruffy/class/");
				URL myURL = new URL("http://yuml.me/diagram/scruffy/class/".concat(expressionFormat));
				URLConnection myURLConnection = myURL.openConnection();
				myURLConnection.connect();

				BufferedImage img = ImageIO.read(myURL);
				String outputImg = "F:/UMLImg/output.png";
				ImageIO.write(img, "png", new File(args[1]));
				JFrame frame = new JFrame();
				frame.setSize(300, 300);
				JLabel label = new JLabel(new ImageIcon(img));
				frame.add(label);
				frame.setVisible(true);*/

				// ---------------------------------------------------------------------------------
				// PLANTUML

				String expressionFormat1 = "";

				boolean alreadyexecuted = false;
				// File[] f = new File[files.length];
				List<File> f = new ArrayList<>();
				int i = 0;
				ParseInterfaces parseInterfaces=new ParseInterfaces();
				HashSet<String> interfaces = new HashSet<String>();
				for (File file : files) {
					if (file.isFile() && file.getName().endsWith(".java")) {
						f.add(file);
						cu = parseSourceCode.parseCode(args[0] + file.getName());
						parseInterfaces.visit(cu,null);;
						
					}
				}
				for (File fileLoop : f) {
					System.out.println(fileLoop.getName());

					cu = parseSourceCode.parseCode(args[0] + fileLoop.getName());
					// cu =
					// parseSourceCode.parseCode("F:/SampleTestCases/Component.java");
					ParseClassPlant parseClassPlant = new ParseClassPlant();

					parseClassPlant.setClasses(f);
					parseClassPlant.setParseInterfaces(parseInterfaces);
					parseClassPlant.visit(cu, null);
					for (String s : parseClassPlant.getExps()) {
						System.out.println(expressionFormat1 += s + "\n");
					}

				}

				OutputStream png = new FileOutputStream(args[1]);
				String source = "@startuml\n";
				source += "skinparam classAttributeIconSize 0\n";
				source += expressionFormat1 + "\n";
				source += "@enduml\n";

				SourceStringReader reader = new SourceStringReader(source);
				String desc = reader.generateImage(png);

				png.close();
			} catch (MalformedURLException e) {
				// new URL() failed
				// ...
			} catch (IOException e) {
				// openConnection() failed
				// ...
			}
			/*
			 * cu=parseSourceCode.parseCode("F:/SampleTestCases/ClassB.java");
			 * new ParseClass().visit(cu,null);
			 */
			/*
			 * cu1=parseSourceCode.parseCode("F:/SampleTestCases/B.java"); new
			 * ParseClass().visit(cu1,null);
			 */

			/*
			 * cu=parseSourceCode.parseCode("F:/SampleTestCases/A.java");
			 * System.out.println("Types "+cu.getTypes()); new
			 * ParseClass().visit(cu, null);
			 * cu=parseSourceCode.parseCode("F:/SampleTestCases/B.java");
			 * System.out.println("Types "+cu.getTypes()); new
			 * ParseClass().visit(cu, null);
			 * cu=parseSourceCode.parseCode("F:/SampleTestCases/C.java");
			 * System.out.println("Types "+cu.getTypes()); new
			 * ParseClass().visit(cu, null);
			 * cu=parseSourceCode.parseCode("F:/SampleTestCases/D.java");
			 * System.out.println("Types "+cu.getTypes()); new
			 * ParseClass().visit(cu, null);
			 */
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
