package compresseur;
public class Bst	{
	private Bst left;
	private Bst right;

	private int the_char;
	private int the_count;

	public Bst()				{left = null; right = null;}
	public Bst(int c, int i, Bst l, Bst r)	{left = l; right = r; the_char = c; the_count = i;}

	public int get_char()			{return the_char;}
	public void set_char(int c)	{the_char = c;}
	
	public int get_count()			{return the_count;}
	public void set_count(int c)	{the_count = c;}

	public Bst get_right()		{return right;}
	public void set_right(Bst n){right = n;}
	
	public Bst get_left()		{return left;}
	public void set_left(Bst n)	{left = n;}

	public void affiche()	{affiche(this, "       ");}
	public void affiche(Bst node, String s)	{
		if(node != null)	{
			affiche(node.get_right(), s + "      ");
			System.out.println(s + node);
			affiche(node.get_left(), s + "      ");
		}
	}


	public String toString()		{return the_char + ": ( " + the_count + " )";}
}