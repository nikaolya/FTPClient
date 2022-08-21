package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;


public class Server {
	private static int port;
	private static ServerSocket server;
	private static Properties prop;
	private static String localFilePath;

	private static DataOutputStream outputStream;
	private static DataInputStream inputStream;

	public static void main(String[] args) throws IOException {

		prop = new Properties();
		try (InputStream io = Server.class.getClassLoader().getResourceAsStream("config.properties")){
			prop.load(io);
		} catch (IOException e) {
			e.printStackTrace();
		}

		port = Integer.parseInt(prop.getProperty("port"));
		localFilePath = prop.getProperty("fileOnServer");

		server = new ServerSocket(port);

		while(true){
			Socket socket = server.accept();
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());

			String input = inputStream.readUTF();

			if (input.equals("SERVER_SENDS_FILE_TO_CLIENT")){
				sendFile();
			} else if(input.equals("CLIENT_SENDS_FILE_BACK_TO_SERVER")){
				receiveFile();
			}

			socket.close();

			if(input.equals("STOP")){
				break;
			}
		}
	}

	public static void sendFile() throws IOException {
		File localFile = new File(localFilePath);
		byte[] bytes = new byte[(int) localFile.length()];
		try (FileInputStream io = new FileInputStream(localFile)){
			io.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputStream.write(bytes);
	}

	private static void receiveFile(){
		byte[] temp = new byte[Integer.parseInt(prop.getProperty("maxFileSize"))];
		int numberOfBytesRead;
		try (FileOutputStream os = new FileOutputStream(localFilePath, false)){
			while((numberOfBytesRead = inputStream.read(temp, 0, temp.length)) != -1)
			{
				os.write(temp, 0, numberOfBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}