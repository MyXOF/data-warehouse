package com.corp.myxof.clean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Disp extends Cleaner{
	private static final Logger LOGGER = LoggerFactory.getLogger(Disp.class);
	private Set<Integer> primaryKey;
	private Set<String> clients; 
	
	public Disp() throws IOException{
		PATH_READ = FILE_HEADER + "disp.csv";
		PATH_WRITE = FILE_HEADER + "disp_new.csv";
		primaryKey = new HashSet<>();
		clients = new HashSet<>();
		readClient();
		accounts = new HashSet<>();
		readAccount();
	}
	
	@Override
	public String fixErrorFormat(String content) {
		String values[] = content.split(",");

		try {
			int dispId = Integer.parseInt(values[0]);
			if (primaryKey.contains(dispId)) {
				throw new Exception("Dupilcate primary key");
			} else {
				primaryKey.add(dispId);
			}
			int clientID = Integer.parseInt(values[1]);
			
			if(!clients.contains(values[1])){
				throw new Exception("client "+clientID+" does not exist");
			}
			
			int accountId = Integer.parseInt(values[2]);
			if(!accounts.contains(values[2])){
				throw new Exception("account "+accountId+" does not exist");
			}

			String type = values[3];
			if(!(type.equals("DISPONENT") || type.equals("OWNER"))){
				throw new Exception();
			}
			
		} catch (Exception e) {
			LOGGER.error("{}",content,e);
			errorLines.add(content);
			return "";
		}

		return content+"\n";
	}
	
	private void readClient() throws IOException{
		String path = FILE_HEADER+"client_new.csv";
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			clients.add(line.split(",")[0]);
		}
		reader.close();
	}

	public static void main(String[] args) throws IOException{
		Cleaner cleaner = new Disp();
		cleaner.findErrorFormat();
		cleaner.printErrorLines();
	}

}
