package com.corp.myxof.clean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Card extends Cleaner {
	private static final Logger LOGGER = LoggerFactory.getLogger(Card.class);
	private SimpleDateFormat format;
	private Set<Integer> primaryKey;
	private Set<String> disps;
	
	public Card() throws IOException{
		PATH_READ = FILE_HEADER + "card.csv";
		PATH_WRITE = FILE_HEADER + "card_new.csv";
		format = new SimpleDateFormat("yyyy/MM/dd H:m");
		primaryKey = new HashSet<>();
		disps = new HashSet<>();
		readDisps();
	}
	
	@Override
	public String fixErrorFormat(String content) {
		String values[] = content.split(",");

		try {
			int cardID = Integer.parseInt(values[0]);
			if(primaryKey.contains(cardID)){
				throw new Exception("Dupilcate primary key");
			}else{
				primaryKey.add(cardID);
			}
			
			int dispId = Integer.parseInt(values[1]);
			if(!disps.contains(values[1])){
				throw new Exception("disps "+dispId+" does not exist");
			}
			
			String type = values[2];
			if(!(type.equals("junior") || type.equals("classic") || type.equals("gold"))){
				throw new Exception();
			}
			
			Date issued = format.parse(values[3]);
		} catch (ParseException e) {
			LOGGER.error("{}",content,e);
			errorLines.add(content);
			return "";
		} catch (Exception e) {
			LOGGER.error("{}",content,e);
			errorLines.add(content);
			return "";
		}

		return content+"\n";
	}
	
	private void readDisps() throws IOException{
		String path = FILE_HEADER+"disp_new.csv";
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			disps.add(line.split(",")[0]);
		}
		reader.close();
	} 
	
	public static void main(String[] args) throws IOException {
		Cleaner cleaner = new Card();
		cleaner.findErrorFormat();
		cleaner.printErrorLines();

	}
}
