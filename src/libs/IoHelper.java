package libs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IoHelper
{
	public static List<String> getFolderContent(String folderPath)
	{
		return getFolderContent(folderPath, "");
	}

	public static List<String> getFolderContent(String folderPath, String fileType)
	{
		List<String> types = new ArrayList<>();
		if (fileType != null && fileType.length() > 1)
		{
			types.add(fileType);
		}
		return getFolderContent(folderPath, types);
	}

	public static List<String> getFolderContent(String folderPath, List<String> fileTypes)
	{
		List<String> files = new ArrayList<>(10);

		File folder = new File(folderPath);
		if (!folder.exists() || !folder.isDirectory())
		{
			System.err.println("Directory '" + folder.getName() + "' is unreachable.");
			return files;
		}

		if (fileTypes == null || fileTypes.size() == 0)
		{
			return Arrays.asList(folder.list());
		}

		FileFilter filter = new FileFilter()
		{
			public boolean accept(File f)
			{
				if (f.isDirectory())
				{
					return false;
				}

				String typ = getFileType(f.getName());
				if (typ.length() > 0)
				{
					for (String format : fileTypes)
					{
						if (typ.compareTo(format) == 0)
						{
							return true;
						}
					}

				}

				return false;
			}
		};

		for (File f : folder.listFiles(filter))
		{
			files.add(f.getName());
		}

		return files;
	}

	public static void saveDataFile(String path, byte[] content, boolean rewrite)
	{
		File file = new File(path);

		if (file.exists() && !rewrite)
		{
			return;
		}

		try
		{
			file.getParentFile().mkdirs();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(content);
			bos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static byte[] loadDataFile(String path)
	{
		File file = new File(path);
		byte[] data = new byte[(int) file.length()];

		try
		{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.read(data);
			bis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return data;
	}

	public static void saveTextFile(String path, String content, boolean rewrite)
	{
		File file = new File(path);

		if (file.exists() && !rewrite)
		{
			return;
		}

		try
		{
			file.getParentFile().mkdirs();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			bw.write(content);
			bw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String loadTextFile(String path)
	{
		StringBuffer sb = new StringBuffer();

		File file = new File(path);

		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			int tmp;

			while ((tmp = br.read()) != -1)
			{
				sb.append((char) tmp);
			}

			br.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	public static Map<String, String> loadProperiesFile(String path)
	{
		Map<String, String> data = new HashMap<>();

		File file = new File(path);
		String line, key, value;
		String[] parts;

		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			while ((line = br.readLine()) != null)
			{
				if (line.length() > 0 && !line.startsWith("#"))
				{
					parts = line.split("=");
					key = parts[0].trim();
					value = parts.length == 2 ? parts[1].trim() : "";

					data.put(key, value);
				}
			}

			br.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return data;
	}

	public static String fillTypeIfMissing(String fileName, String type)
	{
		if (!hasType(fileName))
		{
			fileName += "." + type;
		}

		return fileName;
	}

	public static boolean hasType(String fileName)
	{
		return getFileType(fileName).length() > 0;
	}

	public static String getFileType(String fileName)
	{
		String type = "";

		int i = fileName.lastIndexOf(".");
		if (i > 0)
		{
			type = fileName.substring(i + 1).toLowerCase();
		}

		return type;
	}

	public static void copyFile(String pathFrom, String pathTo)
	{
		try
		{
			File from = new File(pathFrom);
			File to = new File(pathTo);
			to.getParentFile().mkdirs();

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(from));
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(to));

			int i;
			while ((i = in.read()) != -1)
			{
				out.write(i);
			}

			in.close();
			out.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getFileNameWithoutType(String fileName)
	{
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	public static List<String> getFileNamesWithoutTypes(List<String> fileNameList)
	{
		for (int i = 0; i < fileNameList.size(); i++)
		{
			fileNameList.set(i, getFileNameWithoutType(fileNameList.get(i)));
		}

		return fileNameList;
	}

	public static void makeDirs(String filePath)
	{
		makeDirs(new File(filePath));
	}

	public static void makeDirs(File file)
	{
		if (file.isFile())
		{
			file = file.getParentFile();
		}

		file.mkdirs();
	}

	public static void deleteFile(String filePath)
	{
		File file = new File(filePath);

		if (file.exists() && file.isFile())
		{
			file.delete();
		}
	}

}
