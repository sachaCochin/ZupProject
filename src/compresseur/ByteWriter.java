package compresseur;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteWriter {

	private OutputStream output;
	private int octet;
	private int nb_bit;

	public ByteWriter(String fichier) throws IOException {
		output = new FileOutputStream(fichier);
		octet = 0;
		nb_bit = 0;
	}

	public void finalize() throws IOException {output.close();}

	public void writeInt(int i)	throws IOException	{
		byte[] t = new byte[4];

		t[0] = (byte) (i >> 24);
		t[1] = (byte) (i >> 16);
		t[2] = (byte) (i >> 8);
		t[3] = (byte) (i /*>> 0*/);

		output.write(t);
	}

	public void writeChar(char c) throws IOException	{
		output.write(c);
	}

	public void writeBit(char b) throws IOException	{
		if(nb_bit == 8)	{
			output.write(octet);
			octet = 0;
			nb_bit = 0;
		}
		if(b == '0')
			octet = octet << 1;
		else
			octet = octet << 1 | 1;
		nb_bit++;
	}

	public void write(int a) throws IOException	{
		output.write(a);
	}


	public void flush()	throws IOException	{
		while(nb_bit != 8)	{
			nb_bit++;
			octet = octet << 1;
		}
		output.write(octet);
		octet = 0;
		nb_bit = 0;
	}
}