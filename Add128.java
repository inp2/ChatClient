//Imani Palmer
//July 05

import java.util.*;
import java.math.*;

public class Add128 implements SymCipher
{
	public static byte [] temp;
	public byte [] key;
	public static byte [] add;
	public static byte [] encrypt;
	public static String decrypt;

	//Create a random 128 byte additive key
	//Store it in an array of bytes
	public Add128()
	{
		Random rgen = new Random();
		key = new byte[128];

		for(int i = 0; i < 128; i++)
		{
			int randomNumber = rgen.nextInt(256);
			key[i] = (byte)randomNumber;
		}
		System.out.println("Key: " + Arrays.toString(key));
	}

	//Use the byte array parameter as its key
	public Add128(byte [] array)
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
		System.out.println("Array of Bytes : " + Arrays.toString(array));
		//Encode bytes
		encrypt = new byte[array.length];

		if(array.length <= key.length)
		{
			//Go through only the array
			for(int i = 0; i < array.length; i++)
			{
				encrypt[i] = (byte)(array[i] + key[i]);
			}
		}
		else if(array.length > key.length)
		{
			int j = 0;
			//Loop through the key until message is done
			for(int i = 0; i < array.length; i++)
			{
				//System.out.println(j + " " + key.length);
				if(j >= key.length)
				{
					j = 0;
				}
				encrypt[i] = (byte)(array[i] + key[j]);
				j++;
			}
		}
		System.out.println("Encrypted Array of Bytes : " + Arrays.toString(encrypt));
		return encrypt;
	}

	// Decrypt the array of bytes and generate and return the corresponding String
	public String decode(byte [] bytes)
	{
		System.out.println("Array of Bytes : " + Arrays.toString(bytes));
		byte [] decryptArray = new byte[bytes.length];

		if(decryptArray.length < key.length)
		{
			//Go through only the array
			for(int i = 0; i < decryptArray.length; i++)
			{
				decryptArray[i] = (byte)(bytes[i] - key[i]);
				System.out.println(decryptArray[i]);
			}
		}
		else if(decryptArray.length > key.length)
		{
			int j = 0;
			//Loop through the key until message is done
			for(int i = 0; i < bytes.length; i++)
			{
				//System.out.println(j + " " + key.length);
				if(j >= key.length)
				{
					j = 0;
				}
				decryptArray[i] = (byte)(bytes[i] - key[j]);
				j++;
			}
		}
		decrypt = new String(decryptArray);
		System.out.println("Decrypted Array of Bytes : " + Arrays.toString(decryptArray));
		System.out.println("Corresponding String : " + decrypt);
		return decrypt;
	}
}