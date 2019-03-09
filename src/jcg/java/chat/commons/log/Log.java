package jcg.java.chat.commons.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public static enum LogType {
		INFO, WARNING, ERROR, DUMP
	};

	public static enum LogNameStyle {
		EXPANDED, SIMPLE
	};
	
	public static enum BufferType {
		UNBUFFERED, BUFFERED
	};

	// Prevents Log from being instantiated
	private Log() {}
	
	public static void main(String[] args) {
		System.out.println(filename);
	}
	
	private static String filename = "debug-" + simpleDate() + ".txt";
	private static String debugtext = "";
	private static LogNameStyle NAME_TYPE = LogNameStyle.EXPANDED;
	// If BUFFERED, it will wait until the buffer is full to flush the stream
	// If UNBUFFERED, it will flush the stream as soon as it is written too
	private static BufferType BUFFER_TYPE = BufferType.UNBUFFERED;

	public static void log(LogType logtype, String... msgs) {
		String traceBackCaller_CLASS = new Exception().getStackTrace()[1].getClassName();
		String traceBackCaller_METHOD = new Exception().getStackTrace()[1].getMethodName();
		/*Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a ");
		String formattedDate = sdf.format(date);*/
		for (String msg : msgs) {
			if (logtype == LogType.ERROR) {
				debugtext += "> [ERROR] :: [" + simpleDate() + "] :: " + msg + "\n";
				debugtext += "\t> [ERROR] :: Error originated at <" + traceBackCaller_CLASS.toString() + "." + traceBackCaller_METHOD.toString() + "()> \n";
			} else if (logtype == LogType.WARNING) {
				debugtext += "> [WARNING] :: [" + simpleDate() + "] :: " + msg + "\n";
				debugtext += "\t> [WARNING] :: Warning originated at <" + traceBackCaller_CLASS.toString() + "." +  traceBackCaller_METHOD.toString() + "()> \n";
			} else if(logtype == LogType.DUMP) {
				debugtext += "> [DUMP] :: [" + simpleDate() + "] :: " + msg + "\n";
				debugtext += "\t> [DUMP] :: Dump originated at <" + traceBackCaller_CLASS.toString() + traceBackCaller_METHOD.toString() + "()> \n";
			} else {
				debugtext += "> [INFO] ::  [" + simpleDate() + "] :: [" + traceBackCaller_CLASS.substring(traceBackCaller_CLASS.lastIndexOf('.')+1) + ".class] :: " + msg + "\n";
			}
		}
		if(BUFFER_TYPE == BufferType.UNBUFFERED) { 
			saveLog();
		} 
	}
	
	public static void clearLogBuffer() {
		debugtext = null;
	}
	
	public static void flushLogBuffer() {
		try { 
			FileWriter writer = new FileWriter("debug/" + filename);
			writer.write(debugtext);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void saveLog() {
		try {
			File file = new File("debug");
			file.mkdir();
			FileWriter writer = new FileWriter("debug/" + filename);
			writer.write(debugtext);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getLogPath() {
		File file = new File(filename);
		return file.getAbsolutePath();
	}

	public static File getLogFile() {
		File file = new File(filename);
		return file;
	}
	
	public static LogNameStyle getLogNameStyle() {
		return NAME_TYPE;
	}

	public static void setNameStyle(LogNameStyle type) {
		NAME_TYPE = type;
		if (type == LogNameStyle.EXPANDED) {
			filename = "DEBUG-" + expandedDate() + ".txt";
		} else if (type == LogNameStyle.SIMPLE) {
			filename = "DEBUG-" + simpleDate() + ".txt";
		}
	}
	
	public static void setBufferType(BufferType buffer) {
		BUFFER_TYPE = buffer;
	}
	
	public static BufferType getBufferType() {
		return BUFFER_TYPE;
	}

	public static String simpleDate() {
		Date date = new Date();
		/* MM - month
		 * dd - day
		 * yyyy - year
		 * h - hour
		 * mm - minute
		 * ss - seconds
		 * a - AM or PM
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy h:mma");
		String formattedDate = sdf.format(date);
		//formattedDate = formattedDate.replace("/", "-");
		//formattedDate = formattedDate.replace(":", "-");
		//formattedDate = formattedDate.substring(0, 15);
		return formattedDate;
	}

	public static String expandedDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM:dd:yyyy h:mm:ss a ");
		String formattedDate = sdf.format(date);
		//formattedDate = formattedDate.replace("/", "-");
		//formattedDate = formattedDate.replace(":", "-");
		return formattedDate;
	}

	public static void logStackTrace() {
		Log.log(LogType.DUMP, Thread.getAllStackTraces().toString());
		//Thread.getAllStackTraces().toString();
		Thread.dumpStack();
	}

}