package com.corp.myxof.clean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client extends Cleaner {
	private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
	private final static int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private SimpleDateFormat format;
	private Set<Integer> primaryKey;

	public Client() {
		PATH_READ = FILE_HEADER + "client.csv";
		PATH_WRITE = FILE_HEADER + "client_new.csv";
		format = new SimpleDateFormat("yyMMdd");
		primaryKey = new HashSet<>();
	}

	@Override
	public String fixErrorFormat(String content) {
		String values[] = content.split(",");

		try {
			int clientID = Integer.parseInt(values[0]);
			if (primaryKey.contains(clientID)) {
				throw new Exception("Dupilcate primary key");
			} else {
				primaryKey.add(clientID);
			}

			int birthId = Integer.parseInt(values[1]);
			if (birthId < 100000 || birthId > 1000000) {
				throw new Exception("birth id error" + birthId);
			}
			Gender gender = Gender.MALE;
			int year = birthId / 10000 + 1900;

			int month = birthId % 10000 / 100;
			if (month == 0 || (month > 12 && month < 51)) {
				throw new Exception("month error");
			} else if (month > 0 && month < 13) {

			} else if (month > 50 && month < 63) {
				month -= 50;
				gender = Gender.FEMALE;
			} else {
				throw new Exception("month error");
			}
			int day = birthId % 100;

			if (!isValidDate(year, month, day)) {
				throw new Exception(String.format("birthday format error %d/%d/%d", year, month, day));
			}
			String star = caculateStar(month, day);
			int districtId = Integer.parseInt(values[2]);

			return String.format("%d,%d/%d/%d 0:00,%d,%s,%s\n", clientID, year, month, day, districtId, gender, star);
		} catch (Exception e) {
			LOGGER.error("{}", content, e);
			errorLines.add(content);
			return "";
		}

	}

	private boolean isValidDate(int year, int month, int day) {
		try {
			if (year <= 0)
				return false;
			if (month <= 0 || month > 12)
				return false;
			if (day <= 0 || day > DAYS[month])
				return false;
			if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean isGregorianLeapYear(int year) {
		return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
	}

	public String caculateStar(int month, int day) {
		String star = "";
		if (month == 1 && day >= 21 || month == 2 && day <= 19) {
			star = "Aquarius";
		}
		if (month == 2 && day >= 20 || month == 3 && day <= 20) {
			star = "Pisces";
		}
		if (month == 3 && day >= 21 || month == 4 && day <= 20) {
			star = "Aries";
		}
		if (month == 4 && day >= 21 || month == 5 && day <= 21) {
			star = "Taurus";
		}
		if (month == 5 && day >= 22 || month == 6 && day <= 21) {
			star = "Gemini";
		}
		if (month == 6 && day >= 22 || month == 7 && day <= 22) {
			star = "Cancer";
		}
		if (month == 7 && day >= 23 || month == 8 && day <= 23) {
			star = "Leo";
		}
		if (month == 8 && day >= 24 || month == 9 && day <= 23) {
			star = "Virgo";
		}
		if (month == 9 && day >= 24 || month == 10 && day <= 23) {
			star = "Libra";
		}
		if (month == 10 && day >= 24 || month == 11 && day <= 22) {
			star = "Scorpio";
		}
		if (month == 11 && day >= 23 || month == 12 && day <= 21) {
			star = "Sagittarius";
		}
		if (month == 12 && day >= 22 || month == 1 && day <= 20) {
			star = "Capricorn";
		}
		return star;
	}

	public enum Gender {
		MALE, FEMALE;
	}

	public static void main(String[] args) throws IOException {
		//client_id,birth_number,district_id,gender,star
		Cleaner cleaner = new Client();
		cleaner.findErrorFormat();
		cleaner.printErrorLines();

	}
}
