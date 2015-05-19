package decompresseur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.io.*;

import java.util.HashMap;

public class Decompresseur	{


	public static void decode(String filename)	throws IOException{

		// On stock le contenu du fichier dans un tableau de byte
		Path path = Paths.get(filename);
		byte[] data = Files.readAllBytes(path);

		// On lit le premier octet qui contient 
		final int NBBITTOFLUSH  = data[0];

		// On lit les quatres octets suivants qui contiennent la taille du header (en nombre de charactere)
		final int HEADERSIZE = (data[1]<<24) & 0xff000000 | (data[2] << 16) & 0xff0000 | ( data[3] << 8 ) & 0xff00 | ( data[4] /*<< 0*/) & 0xff;

		final HashMap<String, Character> CODES = new HashMap<String, Character>();

		// On initialise un compteur à l'octet ou demarre le header (5) et on le parcours pour construire l'index
		int iRead = 5;
		for(int i=0; i < HEADERSIZE; i++)	{
			// On lit le charactere contenu dans le premier octet
			char c = (char) data[iRead];
			iRead++;
			// On lit la taille du code contenu dans le deuxieme octet
			int codesize = data[iRead];
			iRead++;

			//On lit le code du charactere en ignorant les bit de flush suivant le code
			String charcode = "";
			while(charcode.length() != codesize)	{
				int mask = 0x80;
				// On recupere l'octet suivant
				int bits = data[iRead];
				iRead++;
				while(mask != 0 && charcode.length() != codesize)	{
					if((bits & mask) == 0)
						charcode = charcode + "0";
					else
						charcode = charcode + "1";
					mask = mask >> 1;
				}
			}
			// On ajoute le couple code:charactere dans notre map
			CODES.put(charcode, c);
		}

		// On ouvre un FileWriter pour ecrire le fichier de sortie
		OutputStream writer = new FileOutputStream(filename.replace(".zup", ""));
		String current_code = "";
		int mask = 0x80;
		for(int i=0; i < NBBITTOFLUSH; i++)  // On decale le mask afin d'ignorer les bits de flush à la premiere iteration  
			mask = mask >> 1;

		while(iRead < data.length)	{
			// On recupere l'octet suivant
			int bits = data[iRead];
			iRead++;
			while(mask != 0)	{ // Tant qu'on a pas lu entierement l'octet
				if((bits & mask) == 0)
					current_code = current_code + "0";
				else
					current_code = current_code + "1";
				mask = mask >> 1;

				if(CODES.containsKey(current_code))	{ // Si le code est connu (si il est dans notre index)
					writer.write(CODES.get(current_code)); // On ecrit le charactere correspondant dans le fichier de sortie
					current_code = ""; // On remet le code courrant a la chaine vide
				}
			}
			mask = 0x80;
		}
		writer.close();
	}

	public static void main(String[] args) throws IOException {
		if(args.length == 0)
			System.out.println("Erreur, indiquez au moins le nom d'un fichier");
		for(String s : args)	{
			decode(s);
		}
	}
}