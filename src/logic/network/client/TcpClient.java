package logic.network.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import libs.GeneralHelper;

public class TcpClient implements Runnable
{
	private String serverIp;
	private int serverPort;

	public TcpClient(String ip, int port)
	{
		this.serverIp = ip;
		this.serverPort = port;
	}

	@Override
	public void run()
	{
		try (Socket socket = new Socket(serverIp, serverPort);)
		{
			InetAddress address = socket.getInetAddress();

			System.out.println("Connecting : " + address.getHostAddress() + " / " + serverPort);

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(GeneralHelper.getGuid(20));

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String message = (String) ois.readObject();
			System.out.println("Message Received: " + message);
			
			ois.close();
			oos.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
