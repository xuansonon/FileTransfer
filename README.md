# FileTransfer
A Java console-based application that allows a client to retrieve a file from a host. This connection uses TCP to transfer files. Ideally, it's better utilised for LAN connections. This console application that does not yet use encryption for file transfer.
<br />
<br />
**How to start the application**<br />
You need to start the Server first:
```
java TCPServer [IP-Address] [Port-Number]
```
It is then you can start the Client to retrieve files:
```
java TCPClient [IP-Address] [Port-Number] [File-Name]
```

**Still to do**<br />
- [ ] Encryption of files
