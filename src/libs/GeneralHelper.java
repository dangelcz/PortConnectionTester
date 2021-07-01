package libs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralHelper
{
	private static Random random = new Random();
	public static final char STRING_END = '\0';

	// is null length
	public static boolean INL(String s)
	{
		return s == null || s.length() == 0;
	}

	// is not null length
	public static boolean INNL(String s)
	{
		return s != null && s.length() > 0;
	}

	public static boolean INLs(String... strings)
	{
		return Arrays.stream(strings).allMatch(s -> INL(s));
	}

	public static boolean INNLs(String... strings)
	{
		return Arrays.stream(strings).allMatch(s -> INNL(s));
	}

	public static boolean compareStrings(String a, String b)
	{
		if (INL(a))
		{
			return INL(b);
		}

		return a.equals(b);
	}

	public static String getGuid(int length)
	{
		String s = "";
		length--;
		s += (char) (random.nextInt(25) + 65);

		while (length-- > 0)
		{
			if (random.nextBoolean())
				s += random.nextInt(10);
			else
				s += (char) (random.nextInt(25) + 65);
		}

		return s;
	}

	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		} catch (InterruptedException e)
		{
		}
	}

	public static void sleepSeconds(long seconds)
	{
		sleep(seconds * 1000);
	}

	public static String substringRegex(String input, String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		if (matcher.find())
		{
			return matcher.group(0);
		}

		return "";
	}

	public static Timestamp getCurrentTimestamp()
	{
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * String must be in following formats
	 * 1) yy-MM-dd hh:mm:ss
	 * 2) yy-MM-dd
	 * 3) hh:mm:ss
	 * 4) mm:ss
	 * 
	 * @param sqlTimeString
	 * @return
	 */
	public static Timestamp getTimestampFromSqlString(String sqlTimeString)
	{
		return Timestamp.valueOf(sqlTimeString);
	}

	public static Timestamp addToTimestampYMD(int years, int months, int days)
	{
		return addToTimestamp(years, months, days, 0, 0, 0);
	}

	public static Timestamp addToTimestampHMS(int hours, int minutes, int seconds)
	{
		return addToTimestamp(0, 0, 0, hours, minutes, seconds);
	}

	public static Timestamp addToTimestamp(int year, int month, int day, int hours, int minutes, int seconds)
	{
		Timestamp current = getCurrentTimestamp();
		return getTimestampFrom(current, year, month, day, hours, minutes, seconds);
	}

	public static Timestamp getTimestampFrom(Timestamp referent, int year, int month, int day, int hours, int minutes, int seconds)
	{
		LocalDateTime ldt = referent.toLocalDateTime();
		ldt = ldt.plusYears(year);
		ldt = ldt.plusMonths(month);
		ldt = ldt.plusDays(day);
		ldt = ldt.plusHours(hours);
		ldt = ldt.plusMinutes(minutes);
		ldt = ldt.plusSeconds(seconds);

		return Timestamp.valueOf(ldt);
	}

	public static Timestamp getTimeTimestamp(int hour, int minute)
	{
		return Timestamp.valueOf(String.format("%d:%d", hour, minute));
	}

	public static String getTimeString()
	{
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");

		return ft.format(dNow);
	}

	public static String getDateString()
	{
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");

		return ft.format(dNow);
	}

	/**
	 * Returns date string in format dd.MM.yyyy HH:mm:ss
	 * 
	 * @return
	 */
	public static String getDateTimeString()
	{
		return getDateString() + " " + getTimeString();
	}

	public static int getRandomInt(int from, int to)
	{
		return random.nextInt(to - from) + from;
	}

	public static void writeString(ByteBuffer bb, String s)
	{
		for (char c : s.toCharArray())
		{
			// osetreni proti preteceni bufferu
			if (bb.remaining() > 6)
			{
				bb.putChar(c);
			}
		}

		bb.putChar(STRING_END);
	}

	public static String readString(ByteBuffer bb)
	{
		StringBuffer sb = new StringBuffer(20);
		char c;
		while ((c = bb.getChar()) != STRING_END)
		{
			sb.append(c);
		}

		return sb.toString();
	}

	public static void writeArray(byte[] array, ByteBuffer bb)
	{
		bb.putInt(array.length);
		bb.put(array);
	}

	public static byte[] readArray(ByteBuffer bb)
	{
		int len = bb.getInt();
		byte[] array = new byte[len];
		bb.get(array);

		return array;
	}

	public static void sleepWithPrint(int seconds)
	{
		System.out.print("Sleep countdown: ");

		while (seconds-- >= 0)
		{
			System.out.print((1 + seconds) + " ");
			sleepSeconds(1);
		}

		System.out.println("done");
	}

	public static String readInput()
	{
		return new String(System.console().readLine());
	}

	public static String readPassword()
	{
		return new String(System.console().readPassword());
	}

	public static boolean isIp(String ip)
	{
		if (INL(ip))
		{
			return false;
		}

		String[] parts = ip.split("\\.");

		if (parts.length != 4)
		{
			return false;
		}

		try
		{
			for (String part : parts)
			{
				int i = Integer.parseInt(part);
				if (i < 0 || i > 255)
				{
					return false;
				}
			}
		} catch (NumberFormatException e)
		{
			return false;
		}

		return true;
	}

	public static <T> T getFirstItemOrNull(List<T> list)
	{
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public static boolean runningFromJar()
	{
		URI uri = null;
		try
		{
			uri = GeneralHelper.class.getResource(GeneralHelper.class.getPackage().toString()).toURI();
		} catch (URISyntaxException e)
		{
			// really nothing
		}
		return uri != null && uri.getScheme().equals("jar");
	}

	public static String runCommand(String commandLine) throws Exception
	{
		System.out.println(commandLine);

		Process process = Runtime.getRuntime().exec(commandLine);

		BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		String trecEvalOutput = null;
		StringBuilder output = new StringBuilder("Console output:\n");

		for (String line = null; (line = stdout.readLine()) != null;)
		{
			output.append(line).append("\n");
		}

		trecEvalOutput = output.toString();

		int exitStatus = 0;
		try
		{
			exitStatus = process.waitFor();
		} catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}

		// log.info(exitStatus);

		stdout.close();
		stderr.close();

		return trecEvalOutput;
	}

	public static void printList(List<?> itemList)
	{
		for (Object object : itemList)
		{
			System.out.println(object.toString());
		}
	}

	public static List<Integer> parsePortsString(String portsString, int maxPorts)
	{
		List<Integer> ports = new ArrayList<>();
		String[] parts = portsString.replace(" ", "").trim().split(",");

		if (parts.length > maxPorts)
		{
			return null;
		}

		for (String part : parts)
		{
			if (part.contains("-"))
			{
				String[] parts2 = part.split("-");
				if (parts2.length == 2)
				{
					int startPort = Integer.parseInt(parts2[0]);
					int endPort = Integer.parseInt(parts2[1]);

					int count = endPort - startPort;

					if (count <= 0 || count > maxPorts)
					{
						return null;
					}

					for (int port = startPort; port <= endPort; port++)
					{
						ports.add(port);
					}
				} else
				{
					return null;
				}
			} else
			{
				ports.add(Integer.parseInt(part));
			}
		}

		return ports;
	}

	public static boolean nullOrEmpty(List<?> list)
	{
		return list == null || list.isEmpty();
	}
}
