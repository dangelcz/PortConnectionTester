package launch.modules;

import config.AppConfig;
import launch.ARunModule;
import launch.annotation.RunModule;

@RunModule
public class Version extends ARunModule
{
	@Override
	public void run(String[] args)
	{
		System.out.println(AppConfig.APP_VERSION);
	}

	@Override
	public String getRequiredParameters()
	{
		return "";
	}

	@Override
	public String getOptionalParameters()
	{
		return "";
	}

	@Override
	public String getHelpDescription()
	{
		return "prints current application version";
	}
}
