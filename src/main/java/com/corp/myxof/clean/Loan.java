package com.corp.myxof.clean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Loan extends Cleaner {

	private static final Logger LOGGER = LoggerFactory.getLogger(Loan.class);
	private SimpleDateFormat format;
	private Set<Integer> primaryKey;
	
	public Loan() throws IOException{
		PATH_READ = FILE_HEADER + "loan.csv";
		PATH_WRITE = FILE_HEADER + "loan_new.csv";
		format = new SimpleDateFormat("yyyy/MM/dd H:m");
		primaryKey = new HashSet<>();
		accounts = new HashSet<>();
		readAccount();
	}

	@Override
	public String fixErrorFormat(String content) {
		String values[] = content.split(",");

		try {
			int loanId = Integer.parseInt(values[0]);
			if (primaryKey.contains(loanId)) {
				throw new Exception("Dupilcate primary key");
			} else {
				primaryKey.add(loanId);
			}
			
			int accountId = Integer.parseInt(values[1]);
			if(!accounts.contains(values[1])){
				throw new Exception("account "+accountId+" does not exist");
			}
			
			
			Date date = format.parse(values[2]);
			
			int amount = Integer.parseInt(values[3]);
			
			int duration = Integer.parseInt(values[4]);
			if(duration < 0 || duration % 12 != 0){
				throw new Exception("duration "+ duration + " error");
			}
			
			int payments = Integer.parseInt(values[5]);
			if(payments * duration != amount){
				throw new Exception("payments "+ payments + " error");
			}
			
			String status = values[6];
			if(!(status.equals("A") || status.equals("B") || status.equals("C") || status.equals("D"))){
				throw new Exception("status "+ status + " error");
			}
			
			int payDuration = Integer.parseInt(values[7]);
			if(payDuration >= duration){
				throw new Exception("payDuration "+ payDuration + " error");
			}
		} catch (Exception e) {
			LOGGER.error("{}",content,e);
			errorLines.add(content);
			return "";
		}

		return content+"\n";
	}
	
	
	public static void main(String[] args) throws IOException{
		Cleaner cleaner = new Loan();
		cleaner.findErrorFormat();
		cleaner.printErrorLines();
	}
}
