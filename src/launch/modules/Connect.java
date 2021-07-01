package launch.modules;

import java.util.List;

import config.AppConfig;
import launch.ARunModule;
import launch.annotation.RunModule;
import launch.exceptions.ValidationException;
import libs.GeneralHelper;
import logic.network.ConnectionTester;
import logic.network.client.TcpClient;
import logic.network.client.UdpClient;

/**
 * Command example:
 * pct connect ip.add.re.ss upd x,y-z tcp a,b-c
 * 
 * @author Daniel
 *
 */
@RunModule
public class Connect extends ARunModule
{
	@Override
	public void run(String[] args)
	{
		String ip = getArgument(args, 1);
		if (!GeneralHelper.isIp(ip))
		{
			throw new ValidationException("Not valid ip address");
		}

		String protocol1 = getArgument(args, 2);
		String strPorts1 = getArgument(args, 3);
		List<Integer> ports1 = GeneralHelper.parsePortsString(strPorts1, AppConfig.MAX_PORTS);

		String protocol2 = null;
		String strPorts2 = null;
		List<Integer> ports2 = null;

		if (args.length == 6)
		{
			protocol2 = getArgument(args, 4);
			strPorts2 = getArgument(args, 5);
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

		ConnectionTester tester = ConnectionTester.getTester(ip, tcpPorts, udpPorts);
		tester.performTests();
		tester.printResults();
	}

	@Override
	public String getHelpDescription()
	{
		return "Example: connect 127.0.0.1 upd 80,8080,10020-10030. Ports must be delimited by comma or dash for interval";
	}

	@Override
	public String getRequiredParameters()
	{
		return "ip";
	}

	@Override
	public String getOptionalParameters()
	{
		return "udp udp_ports_string tcp tcp_ports_string";
	}
}
