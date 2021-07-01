package launch;

import java.util.*;

import launch.annotation.RunModule;
import launch.exceptions.ValidationException;
import libs.PackageReflection;
import libs.PackageReflection.ClassCriteria;

public class AppLauncher
{
	private static Map<String, ARunModule> modules;

	private static final String MODULES_PACKAGE = ARunModule.class.getPackage().getName() + ".modules";
	private static final String TEST_MODULES_PACKAGE = ARunModule.class.getPackage().getName() + ".modules.testing";

	public static void main(String[] args)
	{
		try
		{
			loadModules();
			launchModule(args);
		} catch (Exception e)
		{
			// log application failure error
			System.err.println(e);
		}
	}

	public static Map<String, ARunModule> getModules()
	{
		return modules;
	}

	private static void loadModules() throws InstantiationException, IllegalAccessException
	{
		if (modules != null)
		{
			return;
		}

		modules = new HashMap<>();

		List<Class<?>> modulesClasses = PackageReflection.getClassesOfPackage(MODULES_PACKAGE, ClassCriteria.BY_ANNOTATION, RunModule.class);
		List<Class<?>> testsModulesClasses = PackageReflection.getClassesOfPackage(TEST_MODULES_PACKAGE, ClassCriteria.BY_ANNOTATION, RunModule.class);
		modulesClasses.addAll(testsModulesClasses);
		
		ARunModule module;
		for (Class<?> clazz : modulesClasses)
		{
			module = (ARunModule) clazz.newInstance();
			modules.put(module.getCommandName(), module);
		}
	}

	private static void launchModule(String[] args)
	{
		if (args.length == 0)
		{
			printBadCommand();
			return;
		}

		ARunModule module = modules.getOrDefault(args[0].toLowerCase(), null);

		if (module == null)
		{
			printBadCommand(args[0]);
			return;
		}

		try
		{
			module.run(args);
		} catch (ValidationException e)
		{
			e.printStackTrace();
			System.out.println();
			module.printModuleHelp();
		}
	}

	private static void printBadCommand()
	{
		System.err.print("Bad command. Use 'help' parameter for help.\n");
	}

	private static void printBadCommand(String commandName)
	{
		printBadCommand();
		printSimilarCommands(commandName);
	}

	public static void printSimilarCommands(String commandName)
	{
		List<String> similarities = getSimilarCommands(commandName);

		if (similarities.size() > 0)
		{
			System.out.println("Similar commands:");
		}

		System.out.println(String.join(" ", similarities));
	}

	protected static List<String> getSimilarCommands(String target)
	{
		target = target.toLowerCase().trim();

		List<String> similarities = new ArrayList<>();
		for (String moduleName : modules.keySet())
		{
			if (moduleName.contains(target) || target.contains(moduleName))
			{
				similarities.add(moduleName);
			}
		}

		return similarities;
	}
}
