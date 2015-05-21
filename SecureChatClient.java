/* CS 1501
   This client connects to a server so that messages can be typed and forwarded
   to all other clients.  Try it out in conjunction with ImprovedChatServer.java.
   You will need to modify / update this program to incorpora elow is not the
   one required in the assignment -- be sure to change that and also be sure to
   check on the location of the server program regularly (it may change).
*/
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

public class SecureChatClient extends JFrame implements Runnable, ActionListener
{
	//Use 8765 for the port
    public static int PORT = 8765;

	SymCipher cipher = null;
    PrintWriter myChatWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
	Socket connection;
	ObjectOutputStream myWriter;
	ObjectInputStream myReader;
	InetAddress addr;

    public SecureChatClient ()
    {
        try
        {
        	myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
        	serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
        	addr = InetAddress.getByName(serverName);
        	connection = new Socket(addr, PORT);   // Connect to server with new // Socket

        	myWriter = new ObjectOutputStream(connection.getOutputStream());
			myReader = new ObjectInputStream(connection.getInputStream());

        	BigInteger E = (BigInteger) myReader.readObject();
        	System.out.println("E : " + E + "\n");
			BigInteger N = (BigInteger) myReader.readObject();
			System.out.println("N : " + N + "\n");
			String encType = (String) myReader.readObject();

			//User E and N to get encrypt
			//Find the cipher and create the key
			if (encType.equals("Add"))
			{
				System.out.println("Chosen Cipher is Add128");
				cipher = new Add128();
				byte [] temp = cipher.getKey();
				//Convert to key to BigInteger
				BigInteger key = new BigInteger(1, temp);
				BigInteger bigKey = key.modPow(E, N);
				myWriter.writeObject(bigKey);
			}
			else if(encType.equals("Sub"))
			{
				System.out.println("Chosen Cipher is Substitute");
				cipher = new Substitute();
				byte [] temp = cipher.getKey();
				BigInteger key = new BigInteger(1, temp);
				BigInteger bigKey = key.modPow(E, N);
				myWriter.writeObject(bigKey);
			}

			byte [] tempName = cipher.encode(myName);
        	myWriter.writeObject(tempName);   // Send name to Server.  Server will need // this to announce sign-on and sign-off // of clients
        	this.setTitle(myName);      // Set title to identify chatter

        	Box b = Box.createHorizontalBox();  // Set up graphical environment for
        	outputArea = new JTextArea(8, 30);  // user
        	outputArea.setEditable(false);
        	b.add(new JScrollPane(outputArea));

        	outputArea.append("Welcome to the Chat Group, " + myName + "\n");

        	inputField = new JTextField("");  // This is where user will type input
        	inputField.addActionListener(this);

        	prompt = new JLabel("Type your messages below:");

        	Container c = getContentPane();

        	c.add(b, BorderLayout.NORTH);
        	c.add(prompt, BorderLayout.CENTER);
        	c.add(inputField, BorderLayout.SOUTH);

        	Thread outputThread = new Thread(this);  // Thread is to receive strings
        	outputThread.start();                    // from Server

        	setSize(500, 200);
        	setVisible(true);
       	}
        catch (Exception e)
        {
            System.out.println("Problem starting client!");
        }
	}
    public void run()
   	{
		boolean ok = true;
		while (ok)
		{
			try
			{
				//Read object from the server
				byte [] currMsg = (byte []) myReader.readObject();
				//Decode object into a string
				String message = cipher.decode(currMsg);
				//Update the chat window
				outputArea.append(message+"\n");
			}
			catch (Exception e)
			{
			      System.out.println("Client closing: " + e);
			      ok = false;
			}
		}
    }

    public void actionPerformed(ActionEvent e)
    {
        String currMsg = e.getActionCommand();      // Get input value
        inputField.setText("");
        byte [] message = cipher.encode(myName + ": " + currMsg);
        try
        {
			if(currMsg.equals("CLIENT CLOSING"))
			{
				message = cipher.encode(currMsg);
				myWriter.writeObject(message);
				System.exit(0);
			}
			myWriter.writeObject(message); //Add name and send it to Server
		}
       	catch(Exception ioe)
       	{
			System.out.println("Output error" + e);
		}
    }

    public static void main(String [] args)
    {
         SecureChatClient JR = new SecureChatClient();
         JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}


