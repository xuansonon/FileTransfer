import java.io.*;
import java.net.*;
import java.util.*;

class TCPServer {
  int portNumber;
  ServerSocket serverSocket;
  Socket connectableSocket;

  public TCPServer(int aPort) {
    portNumber = aPort;
  }

  public void openInitialSocket(int aPort) {
    try {
      serverSocket = new ServerSocket(aPort);
    } catch(IOException e) {
      System.out.println(e);
    }
  }

  public void acceptIncomingConnections() {
    try {
      connectableSocket = serverSocket.accept();
      String remoteIP = connectableSocket.getInetAddress().getHostAddress().toString();
      System.out.println("A connection has been established with: <" + remoteIP + ">.");
      System.out.println("A request has been recieved! Processing...");
      processRequest(connectableSocket);
    } catch(IOException e) {
      System.out.println(e);
    }
  }

  public File getNamedFile(String aFileName) {
    File file = new File(aFileName);
    return file;
  }

  public void processRequest(Socket aSocket) {
    try {
      InputStreamReader inputStreamReader = new InputStreamReader(aSocket.getInputStream());
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String fileName = bufferedReader.readLine().trim();
      System.out.println("A user has requested a file named: " + fileName);
      File requestedFile = getNamedFile(fileName);
      //http://codereview.stackexchange.com/questions/20961/java-multi-thread-file-server-and-client
      byte[] fileBytes = new byte [(int) requestedFile.length()];
      FileInputStream fInput = new FileInputStream(requestedFile);
      BufferedInputStream bufferedFileInput = new BufferedInputStream(fInput);
      DataInputStream dataStream = new DataInputStream(bufferedFileInput);
      dataStream.readFully(fileBytes, 0, fileBytes.length);
      DataOutputStream outputStream = new DataOutputStream(aSocket.getOutputStream());
      outputStream.writeUTF(requestedFile.getName());
      outputStream.writeLong(fileBytes.length);
      outputStream.write(fileBytes, 0, fileBytes.length);
      outputStream.flush();
      System.out.println("File " + fileName + " has been sent to client.");
    } catch(IOException e) {
      System.out.println();
    }
  }

  public static void main(String[] args) {
    if(args.length != 1) {
      System.out.println("Error: Incorrect number of arguments. Required: 1 (Port Number).");
      System.exit(0);
    }
    int tempPort = 0;
    try {
      System.out.println("Checking for any invalid arguments...");
      tempPort = Integer.parseInt(args[0]);
    } catch(NumberFormatException e) {
      System.out.println("Error: Argument is in the wrong format. Required: Port Number (Integer).");
      System.exit(0);
    }
    String myIP = "";
    try {
      InetAddress myHost = InetAddress.getLocalHost();
      myIP = myHost.getHostAddress();
    } catch(UnknownHostException e) {
      System.out.println(e);
    }
    System.out.println("Arguments are good. Let's start the server!\n");
    System.out.println("Creating the FileSharing Server...");
    TCPServer myServer = new TCPServer(tempPort);
    myServer.openInitialSocket(myServer.portNumber);
    System.out.println("Server opened: <" + myIP + ", " + myServer.portNumber + ">");
    while(true) {
      System.out.println("\nWaiting for a connection to a host...");
      myServer.acceptIncomingConnections();
    }
  }
}
