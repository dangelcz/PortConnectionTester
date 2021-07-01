package logic.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
	private Map<Integer, Boolean> tcpResults;
	private Map<Integer, Boolean> udpResults;
	private List<AConnectionTesterWorker> workers;

	public ConnectionTester(String ip, List<Integer> tcpPorts, List<Integer> udpPorts)
	{
		this.ip = ip;
		this.tcpResults = new TreeMap<>();
		this.udpResults = new TreeMap<>();
		this.workers = new ArrayList<>();

		if (tcpPorts != null)
		{
			tcpPorts.forEach(i -> workers.add(new TcpConnectionTester(ip, i, this)));
		}

		if (udpPorts != null)
		{
			udpPorts.forEach(i -> workers.add(new UdpConnectionTester(ip, i, this)));
		}
	}

	public synchronized void saveResult(Protocol protocol, int serverPort, boolean connectionResult)
	{
		switch (protocol)
		{
			case TCP:
				this.tcpResults.put(serverPort, connectionResult);
				break;
			case UDP:
				this.udpResults.put(serverPort, connectionResult);
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
			while (!pool.awaitTermination(60, TimeUnit.SECONDS) && !gotAllResults())
			{
				System.out.println("Waiting for tasks to complete ... ");
				Thread.sleep(1000);
			}

		} catch (InterruptedException e)
		{
			System.out.println("Exception occured, terminating ... ");
			pool.shutdownNow();
		}
	}

	private boolean gotAllResults()
	{
		return (tcpResults.size() + udpResults.size()) == workers.size();
	}

	public void printResults()
	{
		System.out.println("---------------------");
		System.out.println("Printing results:");
		System.out.println("---------------------");
		System.out.println();

		if (!tcpResults.isEmpty())
		{
			System.out.println("TCP Ports");
			System.out.println("=============");
			tcpResults.forEach((i, b) -> System.out.println(i + ": " + (b ? "OK" : "FAILED")));
			System.out.println();
		}

		if (!udpResults.isEmpty())
		{
			System.out.println("UDP Ports");
			System.out.println("=============");
			udpResults.forEach((i, b) -> System.out.println(i + ": " + (b ? "OK" : "FAILED")));
		}
	}
}
