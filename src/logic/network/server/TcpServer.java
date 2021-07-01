package logic.network.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements Runnable
{
	private int port;

	public TcpServer(int port)
	{
		this.port = port;
	}

	@Override
	public void run()
	{
		try (ServerSocket serverSocket = new ServerSocket(port, 100, InetAddress.getByName("0.0.0.0")))
		{

			while (true)
			{
				Socket clientSocket = serverSocket.accept();
				InetAddress clientAddress = clientSocket.getInetAddress();
				System.out.println("Client from: " + clientAddress.getHostAddress());

				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				String message = (String) ois.readObject();
				System.out.println("Message Received: " + message);
				
				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				oos.writeObject(message);

				ois.close();
				oos.close();
				clientSocket.close();
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
