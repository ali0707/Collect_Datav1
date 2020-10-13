/*
 hello 007
 */
package com.example.mobile_data_achrf.providers;

public class CSVCharEscapeWrapper {

	public static final String SPACE = " ";
	public static final String COMMA = ",";
	public static final String DOUBLE_QUOTE = "\"";
	public static final String LINE_FEED = "\n";
	public static final String CR = "\r";
		
	public static String safeEscape(String stringToFormat) {
		
		if ( stringToFormat == null) return "";
		
		boolean hasSpaces = stringToFormat.contains(SPACE);
		boolean hasCommas = stringToFormat.contains(COMMA);
		boolean hasDoubleQuotes = stringToFormat.contains(DOUBLE_QUOTE);
		boolean hasLineBreaks = stringToFormat.contains(LINE_FEED) || stringToFormat.contains(CR);
		boolean needsEscaped = (hasSpaces || hasCommas || hasDoubleQuotes || hasLineBreaks);
		
		String updatedValue = stripLeadingAndTrailingSpaces(stringToFormat);
		
		if ( hasDoubleQuotes ) {
			updatedValue = updatedValue.replace("\"", "\"\"");
		}
		
		return (needsEscaped) ? surroundWithDoubleQuotes(updatedValue) : updatedValue;
		
	}
	

	private static String stripLeadingAndTrailingSpaces(String stringToFormat) {
		return stringToFormat.trim();
	}

	private static String surroundWithDoubleQuotes(String stringToFormat) {
		return "\"" + stringToFormat + "\"";
	}

}

