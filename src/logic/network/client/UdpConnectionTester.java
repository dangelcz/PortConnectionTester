package logic.network.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import config.AppConfig;
import libs.GeneralHelper;
import logic.network.ConnectionTester;
import logic.network.Protocol;

public class UdpConnectionTester extends AConnectionTesterWorker
{
	public UdpConnectionTester(String ip, int port, ConnectionTester tester)
	{
		super(ip, port, tester);
	}

	@Override
	public boolean connectionTest()
	{
		try (DatagramSocket ds = new DatagramSocket();)
		{
			ds.setSoTimeout(AppConfig.CONNECTION_TIMEOUT);

			String message = GeneralHelper.getGuid(20);
			byte[] buffer = message.getBytes();

			InetAddress address = InetAddress.getByName(serverIp);

			//System.out.println("Connecting : " + address.getHostAddress() + " / UDP / " + serverPort);

			DatagramPacket send = new DatagramPacket(buffer, buffer.length, address, serverPort);
			ds.send(send);

			DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
			ds.receive(recv);

			String receivedMessage = new String(buffer);
			// System.out.println("Message Received: " + message);

			return message.equals(receivedMessage);

		} catch (Exception e)
		{
			//System.err.println("UDP" + serverPort + " ERR: " + e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public Protocol getProtocol()
	{
		return Protocol.UDP;
	}
}
