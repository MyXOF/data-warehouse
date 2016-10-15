package com.corp.myxof.clean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Order extends Cleaner{
	private static final Logger LOGGER = LoggerFactory.getLogger(Order.class);
	private Set<Integer> primaryKey;

	public Order() throws IOException{
		PATH_READ = FILE_HEADER + "order.csv";
		PATH_WRITE = FILE_HEADER + "order_new.csv";
		primaryKey = new HashSet<>();
		accounts = new HashSet<>();
		readAccount();
	}

	@Override
	public String fixErrorFormat(String content) {
		String values[] = content.split(",");

		try {
			int orderId = Integer.parseInt(values[0]);
			if (primaryKey.contains(orderId)) {
				throw new Exception("Dupilcate primary key");
			} else {
				primaryKey.add(orderId);
			}
			int accountId = Integer.parseInt(values[1]);
			
			if(!accounts.contains(values[1])){
				throw new Exception("account "+accountId+" does not exist");
			}
			
			String bankTo = values[2];
			if(bankTo.length() != 2){
				throw new Exception("bankTo "+bankTo+" error");
			}
			
			long accountTo = Long.parseLong(values[3]);
			
			int account = Integer.parseInt(values[4]);

			String symbol = values[5];
			if(!(symbol.equals("POJISTNE") || symbol.equals("SIPO") || symbol.equals("LEASING") || symbol.equals("UVER") || symbol.equals(" "))){
				throw new Exception("symbol "+symbol+" error");
			}
			
			
		} catch (Exception e) {
			LOGGER.error("{}",content,e);
			errorLines.add(content);
			return "";
		}

		return content+"\n";
	}
	
	public static void main(String[] args) throws IOException{
		Cleaner cleaner = new Order();
		cleaner.findErrorFormat();
		cleaner.printErrorLines();
	}

}
