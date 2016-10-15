package com.corp.myxof.clean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class District extends Cleaner{
	private static final Logger LOGGER = LoggerFactory.getLogger(District.class);
	private Set<Integer> primaryKey;

	public District(){
		PATH_READ = FILE_HEADER + "district.csv";
		PATH_WRITE = FILE_HEADER + "district_new.csv";
		primaryKey = new HashSet<>();
	}
	
	@Override
	public String fixErrorFormat(String content) {
		String values[] = content.split(",");

		try {
			int districtId = Integer.parseInt(values[0]);
			if (primaryKey.contains(districtId)) {
				throw new Exception("Dupilcate primary key");
			} else {
				primaryKey.add(districtId);
			}
			int hubNumber = Integer.parseInt(values[3]);
			int cityNumber = Integer.parseInt(values[4]);
			int acgSalary = Integer.parseInt(values[5]);
			double unEmployRate = Double.parseDouble(values[6]);
			int crimeNumber = Integer.parseInt(values[7]);
			
		} catch (Exception e) {
			LOGGER.error("{}",content,e);
			errorLines.add(content);
			return "";
		}

		return content+"\n";
	}

	public static void main(String[] args) throws IOException{
		Cleaner cleaner = new District();
		cleaner.findErrorFormat();
		cleaner.printErrorLines();
	}
}
