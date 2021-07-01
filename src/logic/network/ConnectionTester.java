package logic.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import logic.network.client.AConnectionTesterWorker;
import logic.network.client.TcpConnectionTester;
import logic.network.client.UdpConnectionTester;

public class ConnectionTester
{
	private static final int WORKERS_COUNT = 10;
	private String ip;
	private Map<Integer, Boolean> tcpPorts;
	private Map<Integer, Boolean> udpPorts;
	private List<AConnectionTesterWorker> workers;

	public ConnectionTester(String ip, List<Integer> tcpPorts, List<Integer> udpPorts)
	{
		this.ip = ip;
		this.tcpPorts = new HashMap<>();
		this.udpPorts = new HashMap<>();
		this.workers = new ArrayList<>();

		tcpPorts.forEach(i -> workers.add(new TcpConnectionTester(ip, i, this)));
		udpPorts.forEach(i -> workers.add(new UdpConnectionTester(ip, i, this)));
	}

	public void saveResult(Protocol protocol, int serverPort, boolean connectionResult)
	{
		switch (protocol)
		{
			case TCP:
				this.tcpPorts.put(serverPort, connectionResult);
				break;
			case UDP:
				this.udpPorts.put(serverPort, connectionResult);
				break;
		}
	}

	public void performTests()
	{
		ExecutorService pool = Executors.newFixedThreadPool(WORKERS_COUNT);
		workers.forEach(w -> pool.execute(w));
		pool.shutdown();

		try
		{
			while (!pool.awaitTermination(60, TimeUnit.SECONDS))
			{
				System.out.println("Waiting for tasks to complete ... ");
			}

		} catch (InterruptedException e)
		{
			System.out.println("Exception occured, terminating ... ");
			pool.shutdownNow();
		}
	}

	public void printResults()
	{
		System.out.println("---------------------");
		System.out.println("Printing results:");
		System.out.println("---------------------");
		System.out.println();

		if (!tcpPorts.isEmpty())
		{
			System.out.println("TCP Ports");
			System.out.println("=============");
			tcpPorts.forEach((i, b) -> System.out.println(i + ": " + (b ? "OK" : "FAILED")));
			System.out.println();
		}

		if (!udpPorts.isEmpty())
		{
			System.out.println("UDP Ports");
			System.out.println("=============");
			udpPorts.forEach((i, b) -> System.out.println(i + ": " + (b ? "OK" : "FAILED")));
		}
	}
}
