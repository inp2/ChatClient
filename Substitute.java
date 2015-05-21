//Imani Palmer
//July 05

import java.util.*;
import java.math.*;

public class Substitute implements SymCipher
{
	public static byte [] byteKey;
	//public static byte [] array;
	public static byte [] temp;
	public byte [] key;
	public static byte [] encrypt;
	public static String decrypt;
	public static BigInteger bigInt;

	//Create a random 256 byte array which is a permutation
	//of the 256 possible byte values
	//Serve as a map from bytes to their substitution values
	public Substitute()
	{
		Random rgen = new Random();
		temp = new byte[256];
		key = new byte[256];

		for(int i = 0; i < 256; i++)
		{
			temp[i] = (byte)i;
			key[i] = (byte)i;
		}

		//Shuffle by exchanging each element randomly
		for(int i = 0; i < key.length; i++)
		{
			int randomPosition = rgen.nextInt(key.length);
			byte tem = key[i];
			key[i] = key[randomPosition];
			key[randomPosition] = tem;
		}
		System.out.println("Key: " + Arrays.toString(key));
	}

	//Use the byte array parameter as its key
	public Substitute(byte [] array)
	{
		key = array;
	}

	// Return an array of bytes that represent the key for the cipher
	public byte [] getKey()
	{
		return key;
	}

	// Encode the string using the key and return the result as an array of
	// bytes.  Note that you will need to convert the String to an array of bytes
	// prior to encrypting it.
	public byte [] encode(String S)
	{
		System.out.println("Original Message : " + S);
		//Convert string to byte array
		byte[] array = S.getBytes();
		//Encode bytes
		System.out.println("Array of Bytes : " + Arrays.toString(array));
		encrypt = new byte[array.length];

		//Look up each number in Array1
		//See which number in Array2 matches that number
		//Output that value
		for(int i = 0; i < array.length; i++)
		{
			for(int j = 0; j < temp.length; j++)
			{
				if(array[i] == temp[j])
				{
					encrypt[i] = key[j];
				}
			}
		}
		System.out.println("Encrypted Array of Bytes : " + Arrays.toString(encrypt));
		return encrypt;
	}

	// Decrypt the array of bytes and generate and return the corresponding String
	public String decode(byte [] bytes)
	{
		System.out.println("Array of Bytes : " + Arrays.toString(bytes));
		temp = new byte[256];
		for(int i = 0; i < 256; i++)
		{
			temp[i] = (byte)i;
		}
		byte[] decryptArray = new byte[bytes.length];
		for(int i = 0; i < bytes.length; i++)
		{
			for(int j = 0; j < key.length; j++)
			{
				if(bytes[i] == key[j])
				{
					decryptArray[i] = temp[j];
				}
			}
		}
		decrypt = new String(decryptArray);
		System.out.println("Decrypted Array of Bytes : " + Arrays.toString(decryptArray));
		System.out.println("Corresponding String : " + decrypt);
		return decrypt;
	}
}