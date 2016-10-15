package com.corp.myxof.clean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Account extends Cleaner {
	private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);
	private SimpleDateFormat format;
	private Set<Integer> primaryKey;
	
	public Account() {
		PATH_READ = FILE_HEADER + "account.csv";
		PATH_WRITE = FILE_HEADER + "account_new.csv";
		format = new SimpleDateFormat("yyyy/MM/dd H:m");
		primaryKey = new HashSet<>();
	}

	@Override
	public String fixErrorFormat(String content) {
		String values[] = content.split(",");

		try {
			int accountID = Integer.parseInt(values[0]);
			if(primaryKey.contains(accountID)){
				throw new Exception("Dupilcate primary key");
			}else{
				primaryKey.add(accountID);
			}
			
			int districtId = Integer.parseInt(values[1]);
			String frequency = values[2];
			if(!(frequency.equals("POPLATEK MESICNE") || frequency.equals("POPLATEK TYDNE") || frequency.equals("POPLATEK PO OBRATU"))){
				throw new Exception();
			}
			
			Date date = format.parse(values[3]);
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
	
	

	public static void main(String[] args) throws IOException {
		Cleaner cleaner = new Account();
		cleaner.findErrorFormat();
		cleaner.printErrorLines();
	}

}
