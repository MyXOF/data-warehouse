package com.corp.myxof.clean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Cleaner {
	protected final String FILE_HEADER = "src/main/resources/";
	protected String PATH_READ;
	protected String PATH_WRITE;
	protected List<String> errorLines;
	protected Set<String> accounts;
	
	public Cleaner() {
		errorLines = new ArrayList<>();
	}
	
	public void findErrorFormat() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(PATH_READ));
		BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_WRITE));
		String line = reader.readLine();
		writer.write(line + "\n");
		while ((line = reader.readLine()) != null) {
			writer.write(fixErrorFormat(line));
		}
		writer.close();
		reader.close();
	}
	
	public void printErrorLines(){
		for(String error : errorLines){
			System.out.println(error);
		}
	}

	protected void readAccount() throws IOException{
		String path = FILE_HEADER+"account_new.csv";
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			accounts.add(line.split(",")[0]);
		}
		reader.close();
	}
	
	public abstract String fixErrorFormat(String content);
}
