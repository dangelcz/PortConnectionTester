package logic.network.client;

import logic.network.ConnectionTester;
import logic.network.Protocol;

public abstract class AConnectionTesterWorker implements Runnable
{
	protected String serverIp;
	protected int serverPort;
	private ConnectionTester tester;

	public AConnectionTesterWorker(String ip, int port, ConnectionTester tester)
	{
		this.serverIp = ip;
		this.serverPort = port;
		this.tester = tester;
	}

	@Override
	public void run()
	{
		tester.saveResult(getProtocol(), serverPort, connectionTest());
	}

	public abstract boolean connectionTest();
	public abstract Protocol getProtocol();
}
