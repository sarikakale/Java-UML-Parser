package com.sarika.parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

public class ParseSourceCode {
	public CompilationUnit parseCode(String filename) throws ParseException, IOException {
		FileInputStream in = null;
		CompilationUnit compilationUnit = null;
		try {
			in = new FileInputStream(filename);

			compilationUnit = JavaParser.parse(in);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
		}
		return compilationUnit;
	}

	
}
