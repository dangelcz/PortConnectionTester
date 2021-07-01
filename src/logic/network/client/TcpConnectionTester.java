package logic.network.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import config.AppConfig;
import libs.GeneralHelper;
import logic.network.ConnectionTester;
import logic.network.Protocol;

public class TcpConnectionTester extends AConnectionTesterWorker
{
	public TcpConnectionTester(String ip, int port, ConnectionTester tester)
	{
		super(ip, port, tester);
	}

	@Override
	public boolean connectionTest()
	{
		try (Socket socket = new Socket(serverIp, serverPort);)
		{
			socket.setSoTimeout(AppConfig.CONNECTION_TIMEOUT);
			String message = GeneralHelper.getGuid(20);
			
			InetAddress address = socket.getInetAddress();

			//System.out.println("Connecting : " + address.getHostAddress() + " / TCP / " + serverPort);

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String receivedMessage = (String) ois.readObject();
			//System.out.println("Message Received: " + message);

			ois.close();
			oos.close();
			
			return message.equals(receivedMessage);

		} catch (Exception e)
		{
			//System.err.println("TCP ERR: " + e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public Protocol getProtocol()
	{
		return Protocol.TCP;
	}
}
