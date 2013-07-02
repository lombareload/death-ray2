package com.deathRay.dto;

import java.util.Arrays;
import java.util.List;


public class CsvGoogleSpreadsheet {

	private StringBuilder strBuilder;
	private char entryDelimiter;
	private char lineDelimiter;
	
	public CsvGoogleSpreadsheet(){
		strBuilder = new StringBuilder();
		entryDelimiter = ',';
		lineDelimiter = '\n';
	}
	
	public CsvGoogleSpreadsheet(char entryDelimiter, char lineDelimiter){
		this.entryDelimiter = entryDelimiter;
		this.lineDelimiter = lineDelimiter;
	}
	
	public CsvGoogleSpreadsheet appendLine(String... strings){
		List<String> entries = Arrays.asList(strings);
		for (String string : entries) {
			strBuilder.append('"').append(string).append('"').append(entryDelimiter);
		}
		strBuilder.append(lineDelimiter);
		return this;
	}
	
	public String getResult(){
		return strBuilder.toString().replaceAll(",\\n", "\n");
	}
}
