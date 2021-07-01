package logic.network.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServer implements Runnable
{
	private int port;

	public UdpServer(int port)
	{
		this.port = port;
	}

	@Override
	public void run()
	{
		int bufferSize = 128;
		try (DatagramSocket datagramSocket = new DatagramSocket(port))
		{
			while (true)
			{
				byte[] buffer = new byte[bufferSize];
				DatagramPacket clientPacket = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(clientPacket);

				InetAddress clientAddress = clientPacket.getAddress();
				System.out.println("Client from: " + clientAddress.getHostAddress() + " on UDP " + port);

				//String message = new String(buffer);
				//System.out.println("Message Received: " + message);
				
				datagramSocket.send(clientPacket);
			}
		} catch (Exception e)
		{
			System.err.println(e);
		}
	}

}
