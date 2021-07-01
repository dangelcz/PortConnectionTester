package launch.modules;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import config.AppConfig;
import launch.ARunModule;
import launch.AppLauncher;
import launch.annotation.RunModule;
import launch.exceptions.ValidationException;
import libs.GeneralHelper;
import logic.network.server.TcpServer;
import logic.network.server.UdpServer;

/**
 * Command example:
 * pct listen upd x,y-z tcp a,b-c
 * 
 * @author Daniel
 *
 */
@RunModule
public class Listen extends ARunModule
{
	@Override
	public void run(String[] args)
	{
		String protocol1 = getArgument(args, 1);
		String strPorts1 = getArgument(args, 2);
		List<Integer> ports1 = GeneralHelper.parsePortsString(strPorts1, AppConfig.MAX_PORTS);

		String protocol2 = null;
		String strPorts2 = null;
		List<Integer> ports2 = null;

		if (args.length == 5)
		{
			protocol2 = getArgument(args, 3);
			strPorts2 = getArgument(args, 4);
			ports2 = GeneralHelper.parsePortsString(strPorts2, AppConfig.MAX_PORTS);
		}

		List<Integer> tcpPorts = null;
		List<Integer> udpPorts = null;

		if ("tcp".equalsIgnoreCase(protocol1))
		{
			tcpPorts = ports1;
			udpPorts = ports2;
		} else if ("udp".equalsIgnoreCase(protocol1))
		{
			tcpPorts = ports2;
			udpPorts = ports1;
		}

		// TODO

		// TcpServer s = new TcpServer(8888);
		// s.run();

		UdpServer u = new UdpServer(8889);
		u.run();
	}

	@Override
	public String getHelpDescription()
	{
		return "Example: listen upd 80,8080,10020-10030. Ports must be delimited by comma or dash for interval";
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
