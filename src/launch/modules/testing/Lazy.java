package launch.modules.testing;

import java.util.Scanner;

import launch.ARunModule;
import launch.AppLauncher;
import launch.annotation.RunModule;

/**
 * Just stops the program at the begginnig, then launches module by given parameters
 * 
 * Good for performance debugging - pause before full start
 * 
 * @author Daniel
 *
 */
@RunModule
public class Lazy extends ARunModule
{
	@Override
	public void run(String[] args)
	{
		System.out.println("Press enter to continue ...");

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		sc.nextLine();

		String[] newArgs = new String[args.length - 1];
		for (int i = 0; i < newArgs.length; i++)
		{
			newArgs[i] = args[i + 1];
		}

		AppLauncher.main(newArgs);
	}

	@Override
	public String getHelpDescription()
	{
		return "just stops the program at the begginnig, then launches module by given parameters";
	}

	@Override
	public String getRequiredParameters()
	{
		return "parameters of another module";
	}

	@Override
	public String getOptionalParameters()
	{
		return "";
	}
}
