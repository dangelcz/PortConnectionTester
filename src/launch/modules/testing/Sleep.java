package launch.modules.testing;

import libs.GeneralHelper;
import launch.ARunModule;
import launch.annotation.RunModule;

@RunModule
public class Sleep extends ARunModule
{
	@Override
	public void run(String[] args)
	{
		int sleepInterval = 60;

		if (args.length > 1)
		{
			sleepInterval = Integer.parseInt(args[1]);
		}

		sleep(sleepInterval);
	}
	
	private void sleep(int seconds)
	{
		GeneralHelper.sleepWithPrint(seconds);
	}

	@Override
	public String getHelpDescription()
	{
		return "sleeps for given amount of time and prints out countdown";
	}

	@Override
	public String getRequiredParameters()
	{
		return "";
	}

	@Override
	public String getOptionalParameters()
	{
		return "seconds";
	}
}
