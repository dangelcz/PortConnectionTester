package launch;

import static libs.GeneralHelper.INNL;

import java.util.Arrays;
import java.util.List;

import launch.annotation.RunModule;
import launch.exceptions.ValidationException;
import libs.GeneralHelper;

public abstract class ARunModule
{
	public String getCommandName()
	{
		RunModule annotation = this.getClass().getAnnotation(RunModule.class);
		String commandName = annotation.value();

		if (GeneralHelper.INL(commandName))
		{
			commandName = this.getClass().getSimpleName().toLowerCase();
		}

		return commandName;
	}
	
	public void printModuleHelp()
	{
		String required = String.join("> <", getRequiredParameters().split(" "));
		required = INNL(required) ? " <" + required + ">" : "";

		String optional = String.join(") (", getOptionalParameters().split(" "));
		optional = INNL(optional) ? " (" + optional + ")" : "";

		String helpDescription = getHelpDescription().replace("\n", "\n\t\t");
		
		System.out.format("%s%s%s\n\t: %s",	getCommandName(),
												required,
												optional,
												helpDescription);
	}

	protected String getArgument(String[] args, int index)
	{
		if (args.length <= index)
		{
			throw new ValidationException("Missing argument number: " + index);
		}

		return args[index];
	}
	
	protected String getArgumentOrDefault(String[] args, int index, String defaultValue)
	{
		if (args.length <= index)
		{
			return defaultValue;
		}
		
		return args[index];
	}

	protected int getIntArgument(String[] args, int index)
	{
		if (args.length <= index)
		{
			throw new ValidationException("Missing argument number: " + index);
		}

		try
		{
			return Integer.parseInt(args[index]);
		} catch (NumberFormatException e)
		{
			throw new ValidationException("Given parameter is not a number");
		}
	}
	
	protected int getIntArgumentOrDefault(String[] args, int index, int defaultValue)
	{
		if (args.length <= index)
		{
			return defaultValue;
		}

		try
		{
			return Integer.parseInt(args[index]);
		} catch (NumberFormatException e)
		{
			throw new ValidationException("Given parameter is not a number");
		}
	}

	protected List<String> fromString(String separatedLine)
	{
		return Arrays.asList(separatedLine.split(" "));
	}

	public abstract void run(String[] args);

	public abstract String getRequiredParameters();

	public abstract String getOptionalParameters();

	public abstract String getHelpDescription();
}
