package com.corp.myxof.clean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Trans extends Cleaner{
	private static final Logger LOGGER = LoggerFactory.getLogger(Trans.class);
	private SimpleDateFormat format;
	private Set<Integer> primaryKey;
	
	public Trans() throws IOException{
		PATH_READ = FILE_HEADER + "trans.csv";
		PATH_WRITE = FILE_HEADER + "trans_new.csv";
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		primaryKey = new HashSet<>();
		accounts = new HashSet<>();
		readAccount();
	}

	@Override
	public String fixErrorFormat(String content) {
		String values[] = content.split(",");

		try {
			int transId = Integer.parseInt(values[0]);
			if (primaryKey.contains(transId)) {
				throw new Exception("Dupilcate primary key");
			} else {
				primaryKey.add(transId);
			}
			
			int accountId = Integer.parseInt(values[1]);
			if(!accounts.contains(values[1])){
				throw new Exception("account "+accountId+" does not exist");
			}
			
			Date date = format.parse(values[2]);
			
			String type = values[3];
			if(!(type.equals("PRIJEM") || type.equals("VYDAJ"))){
				throw new Exception();
			}
			
			String operation = values[4];
			if(!(operation.equals("PREVOD NA UCET") || 
				 operation.equals("VYBER") ||
				 operation.equals("VYBER KARTOU") ||
				 operation.equals("VKLAD") ||
				 operation.equals("PREVOD Z UCTU"))){
				throw new Exception("operation "+ operation+" error");
			}
			
			int amount = Integer.parseInt(values[5]);
			
			if(amount < 0){
				throw new Exception("amount "+ amount+" < 0");
			}
			
			int balance = Integer.parseInt(values[6]);
			
			String kSymbol = values[7];
			if(!(kSymbol.equals("POJISTNE") ||
					kSymbol.equals("SLUZBY") ||
					kSymbol.equals("UROK") ||
					kSymbol.equals("DUCHOD") ||
					kSymbol.equals("UVER") ||
					kSymbol.equals("SANKC. UROK") ||
					kSymbol.equals("SIPO") ||
					kSymbol.equals(" "))){
				throw new Exception("k_symbol "+ kSymbol+" error");
			}	
			
			String bankTo = values[8];
			if(bankTo.length() != 2){
				throw new Exception("bankTo "+ bankTo+" error");
			}
		} catch (Exception e) {
			LOGGER.error("{}",content,e);
			errorLines.add(content);
			return "";
		}

		return content+"\n";
	}
	
	
	public static void main(String[] args) throws IOException{
		Cleaner cleaner = new Trans();
		cleaner.findErrorFormat();
		cleaner.printErrorLines();
	}

}
