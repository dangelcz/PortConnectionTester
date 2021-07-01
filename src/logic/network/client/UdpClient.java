package logic.network.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import libs.GeneralHelper;

public class UdpClient implements Runnable
{
	private String serverIp;
	private int serverPort;

	public UdpClient(String ip, int port)
	{
		this.serverIp = ip;
		this.serverPort = port;
	}

	@Override
	public void run()
	{

		try (DatagramSocket ds = new DatagramSocket();)
		{
			String message = GeneralHelper.getGuid(20);
			byte[] buffer = message.getBytes();

			InetAddress address = InetAddress.getByName(serverIp);

			System.out.println("Connecting : " + address.getHostAddress() + " / " + serverPort);
			
			DatagramPacket send = new DatagramPacket(buffer, buffer.length, address, serverPort);
			ds.send(send);
			
			DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
			ds.receive(recv);
			
			message = new String(buffer);
			System.out.println("Message Received: " + message);

		} catch (Exception e)
		{
			System.err.println(e);
		}
	}

}
