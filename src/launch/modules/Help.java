package launch.modules;

import java.util.Map;
import java.util.TreeSet;

import config.AppConfig;
import launch.ARunModule;
import launch.AppLauncher;
import launch.annotation.RunModule;

@RunModule
public class Help extends ARunModule
{
	private Map<String, ARunModule> modules;

	@Override
	public void run(String[] args)
	{
		modules = AppLauncher.getModules();

		System.out.println("Scriptator " + AppConfig.APP_VERSION + " syntax:");
		System.out.println("<command> <required parameter> (optional parameter) ...");
		System.out.println();

		if (args.length == 2)
		{
			ARunModule module = modules.get(args[1]);

			if (module == null)
			{
				System.err.print("Given command does not exists\n");
				AppLauncher.printSimilarCommands(args[1]);
				return;
			}

			module.printModuleHelp();
		} else
		{
			// print it sorted
			ARunModule module;
			for (String key : new TreeSet<>(modules.keySet()))
			{
				module = modules.get(key);
				module.printModuleHelp();
				System.out.println();
			}
		}
	}
	
	@Override
	public String getRequiredParameters()
	{
		return "";
	}

	@Override
	public String getOptionalParameters()
	{
		return "commandname";
	}

	@Override
	public String getHelpDescription()
	{
		return "prints description of all commands or given one";
	}
}
