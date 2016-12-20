import java.io.*;
import java.net.*;
import java.util.*;

class TCPClient {
  String ipAddress;
  int portNumber;
  String fileName;
  Socket connectingSocket;

  public TCPClient(String aIP, String aPort, String aFileName) {
    try {
      InetAddress Addr;
      Addr = InetAddress.getByName(aIP);
      ipAddress = aIP;
    } catch(UnknownHostException e) {
      System.out.println("Your IP address " + aIP + " is in incorrect format. Required: IP Address (IP Address Format)");
      System.exit(0);
    }
    try {
      portNumber = Integer.parseInt(aPort);
    } catch(NumberFormatException e) {
      System.out.println("Your port number " + aPort + " is not a valid input. Required: Port Number (Integer).");
      System.exit(0);
    }
    fileName = aFileName;
  }

  public void connectToConnectableSocket(String aIP, int aPort) {
    try {
      connectingSocket = new Socket(aIP, aPort);
      System.out.println("Connected to the server at: <" + aIP + ", " + aPort + ">.");
      sendMessages(connectingSocket);
      closeConnection(connectingSocket);
    } catch(UnknownHostException e) {
      System.out.println("Host unreachable at <" + aIP + ", " + aPort + ">.");
    } catch(ConnectException e) {
      System.out.println("Connection timed out. Could not connect to <" + aIP + ", " + aPort + ">.");
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public void closeConnection(Socket aSocket) {
    try {
      aSocket.close();
    } catch(IOException e)
    {
      System.out.println(e);
    }
  }

  public void recieveMessage(Socket aSocket) {
    try {
      int fileBytes;
      InputStream inputStream = aSocket.getInputStream();
      DataInputStream dataInputStream = new DataInputStream(inputStream);
      String fileName = dataInputStream.readUTF();
      OutputStream outputStream = new FileOutputStream("from_server_" + fileName);
      long fileSize = dataInputStream.readLong();
      byte[] fileBuffer = new byte[1024];
      while(fileSize > 0 && (fileBytes = dataInputStream.read(fileBuffer, 0, (int) Math.min(fileBuffer.length, fileSize))) != 1) {
        outputStream.write(fileBuffer, 0, fileBytes);
        fileSize -= fileBytes;
      }
      outputStream.close();
      inputStream.close();
      System.out.println(fileName + " recieved from the server. Saved as: from_server_" + fileName);
    } catch(IOException e) {
      System.out.println(e);
    }
  }

  public void sendMessages(Socket aSocket) {
    try {
      DataOutputStream outputToServer = new DataOutputStream(aSocket.getOutputStream());
      outputToServer.writeBytes(fileName + "\n");
      //Recieving the messages
      recieveMessage(aSocket);
    } catch(IOException e) {
      System.out.println(e);
    }
  }

  public static void main(String[] args) throws Exception {
    if(args.length != 3) {
      System.out.println("Incorrect number of arguments. Required: 3 (IP Address, Port Number, File Name)");
      System.exit(0);
    }
    TCPClient myClient = new TCPClient(args[0], args[1], args[2]);
    System.out.println("Attempting to connect to nominated server...");
    myClient.connectToConnectableSocket(myClient.ipAddress, myClient.portNumber);
  }
}
