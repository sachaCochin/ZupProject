package compresseur;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class Compresseur	{

	public static HashMap<Integer, String> get_codes(Bst current_node)	{
			/*
				Entrées :	- Bst = Racine de l'arbre binaire à explorer
				Sortie :	- HashMap<Character, String> = map ou sont stoqués les characteres associ￩s a leur code 

				Fonction recursive qui parcours l'arbre binaire pass￩ en parametre en calcule le code associé
				à chaque nSud selon l'algorythme de Huffman
			*/
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			get_codes(map, current_node, "");
			return map;
	}
	public static void get_codes(HashMap<Integer, String> map_pathes, Bst current_node, String current_code)	{
		/*
			Entrées :	- HashMap<Character, String> = map ou serons stoqués les characteres associés a leur code 
						- Bst = Racine de l'arbre binaire à explorer
						- String = Code courrant

			Fonction recursive qui parcours l'arbre binaire passé en parametre en calcule le code associé
			à chaque noeud selon l'algorythme de Huffman
		*/

		if( current_node != null )	{
			if( current_node.get_left() == null && current_node.get_right() == null )	
				{ map_pathes.put(current_node.get_char(), current_code);}
			else	{
				get_codes( map_pathes, current_node.get_left(), current_code + "0" ); 
				get_codes( map_pathes, current_node.get_right(), current_code + "1" );
			}
		}
	}


	public static HashMap<Character, Integer> get_index(String text) throws IOException	{
		
		/*
			Fonction qui construit l'index regrouant les characteres contenus dans dans le text passé en parametre
			associés à leur fr￩quence.
		*/
		HashMap<Character, Integer> map_index = new HashMap<Character, Integer>();
		// map_index va contenir la tables des characteres associé à leur fréquence

		for(int i=0; i < text.length(); i++)	{
			if(map_index.containsKey(text.charAt(i)))
				{map_index.put(text.charAt(i), map_index.get(text.charAt(i)) + 1);}
			else			
				{map_index.put(text.charAt(i), 1);}
		} 
		return map_index;
	}

	public static Bst build_bst(HashMap<Integer, Integer> map_index)	{
		ArrayList<Bst> list = new ArrayList<Bst>();	
		// On instancie une liste qui va contenir les noeuds de l'abre et nous aider ￠ la construction du Bst
		for( Map.Entry<Integer, Integer> entry : map_index.entrySet() )	
			// On remplit notre liste avec tout les couple charactere:frequence de notre HashMap
			{ list.add( new Bst(entry.getKey(), entry.getValue(), null, null ) ); }


		// On trie la liste de noeuds par ordre croissant de leur frequences
		while( list.size() > 1 )	{ // Tant qu'il y a plus d'un elements dans la liste
		
			Collections.sort( list, new Comparator<Bst>() {
				/*
					Comparateur de Bst par ordre croissant de leur fr￩quence
				*/
				public int compare(Bst n1, Bst n2)	{
					if(n1.get_count() < n2.get_count())
						return -1;
					else if(n1.get_count() > n2.get_count())
						return 1;
					else return 0;
				}
			});

			Bst aux = new Bst( '$', list.get(0).get_count() + list.get(1).get_count(), list.get(0), list.get(1));
			
			list.remove( 1 ); // On supprime les deux premiers elements de la liste
			list.remove( 0 ); 
			
			// On ajoute un nouveau noeud dans la liste, p￨re des deux premiers elements de la liste et contenant un compte egal a la somme des compte des deux premiers elements
			list.add(aux);
		} 
		return list.get( 0 );  // On retourne le dernier element de notre liste
	}

	public static void code(String filename) throws IOException	{
		InputStream stream = new FileInputStream(filename);
		HashMap<Integer, Integer> INDEX = new HashMap<Integer, Integer>(); 
		int iRead = 0;
		
		while((iRead = stream.read()) != -1)	{
			if(INDEX.containsKey(iRead))
				{INDEX.put(iRead, INDEX.get(iRead) + 1);}
			else			
				{INDEX.put(iRead, 1);}
		}
		stream.close();
		HashMap<Integer , String> CODES = new HashMap<Integer , String>();
		if(INDEX.size() > 1){
		// On construit notre bst grace ￠ l'index
		final Bst BST = build_bst(INDEX);

		// On calcul les code de chaque charactere dans l'abre selon l'algo
		CODES  = get_codes(BST);
		}
		else {
			for(Map.Entry<Integer, Integer> entry : INDEX.entrySet())
			CODES.put(entry.getKey(), "0");
		}

		int codeSize = 0;
		for(Map.Entry<Integer, String> entry : CODES.entrySet())
			codeSize = codeSize + entry.getValue().length() * INDEX.get(entry.getKey());

		ByteWriter writer = new ByteWriter(filename + ".zup");

		// On ecrit la nombre de bit flushés dans le code
		final int NBBITTOFLUSH = 8 - (codeSize % 8); // 1 octet
		writer.write(NBBITTOFLUSH);

		// On ecrit la taille du header sur 4 octets
		final int HEADERSIZE = CODES.size();
		writer.writeInt(HEADERSIZE);


		for(Map.Entry<Integer, String> entry : CODES.entrySet())	{
			int c = entry.getKey();
			String charcode = entry.getValue();
			int codesize = charcode.length();
			
			// On ecrit le charactere (1 octet)
			writer.write(c);
			// On ecrit la taille du code (1 octet)
			writer.write(codesize);

			// On ecrit le code du charactere
			for(int ii=0; ii < charcode.length(); ii++)
				writer.writeBit(charcode.charAt(ii));
			// Et on flush les derniers bit
			writer.flush();
		}

		// On flush le corp avant de l'ecrire
		for(int i=0; i < NBBITTOFLUSH; i++)
			writer.writeBit('0');
		// Et finalement on ecrit le texte codé grace a notre index
		stream = new FileInputStream(filename);
		iRead = 0;
		while((iRead = stream.read()) != -1)	{
			for(int i=0; i < CODES.get(iRead).length(); i++)
				writer.writeBit(CODES.get(iRead).charAt(i));
		}
	
		stream.close();
		writer.flush();
		writer.finalize();
	}
	

	public static void main(String[] args) throws IOException	{

		if(args.length == 0)
			System.out.println("Erreur, indiquez au moins le nom d'un fichier");
		else
			for(String s : args)
			{
				/*
					On compresse chaques fichiers pass￩s en argument
				*/
				code(s);
			}
	}
}
