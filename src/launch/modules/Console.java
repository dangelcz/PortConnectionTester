package launch.modules;

import java.util.NoSuchElementException;
import java.util.Scanner;

import launch.ARunModule;
import launch.AppLauncher;
import launch.annotation.RunModule;


@RunModule
public class Console extends ARunModule
{
	@Override
	public void run(String[] args)
	{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		
		while (true)
		{
			try
			{
				System.out.println("\n-----------------------");
				System.out.println("Run your command (or 'exit'):");
				System.out.print("> ");
				
				// String command = System.console().readLine();
				String command = sc.nextLine();
				
				if ("exit".equalsIgnoreCase(command))
				{
					return;
				}
				
				AppLauncher.main(command.split(" "));
			} catch (NoSuchElementException e)
			{
				return;
			} catch (Exception e)
			{
				System.err.println("You got exception, but console runs again!");
			}
		}
		
		//sc.close();
	}

	@Override
	public String getHelpDescription()
	{
		return "developer tool - reruns given command";
	}

	@Override
	public String getRequiredParameters()
	{
		return "any";
	}

	@Override
	public String getOptionalParameters()
	{
		return "any";
	}
}
