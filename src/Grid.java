import java.util.ArrayList;
import java.util.Stack;


public class Grid {
	ArrayList<Stack<Integer>> solutions=new ArrayList<Stack<Integer>>();
	int nbsol;
	Cell head;
	Cell[] columns; 

	public Grid(int nbCol){
		columns=new Cell[nbCol+1];
		nbsol=0;
		solutions.add((new Stack<Integer>()));
		head=Cell.head();
		columns[0]=head;
		Cell.column(head, nbCol);
		for(Cell i=head.R;i!=head;i=i.R)
			columns[i.col]=i;

	}

	public void addRow(int l, int[] col){	//ajoute une ligne a la Grid déja formée, avec le numero de lignes l, avec des 1 dans les colonnes de col
		// /!\ Entrer les lignes dans le desordre peut donner un tableau incorrect
		if (col.length==0) return;

		Cell c= new Cell(null,columns[col[0]], l, col[0]);
		columns[col[0]].row++;

		for(int i=1;i<col.length;i++){
			new Cell(c,columns[col[i]],l,col[i]);
			columns[col[i]].row++;
		}
	}


	public void coverCol(int c){
		Cell C= columns[c];
		C.hideH();
		for(Cell i=C.D;i!=C;i=i.D){
			for(Cell j=i;j!=i;j=j.R){
				columns[j.col].row--;
				j.hideH();
			}
		}
	}

	public void uncoverCol(int c){
		Cell C= columns[c];
		C.unHideH();
		for(Cell i=C.D;i!=C;i=i.D){
			for(Cell j=i;j!=i;j=j.R){
				columns[j.col].row++;
				j.unHideH();
			}
		}
	}
	void solveAll() {
		if (head.R==head){ 									// Quand la matrice est vide,
			// cela signifie que la pile actuelle de solutions est une solution 
			// => l'enregistrer, la cloner
			nbsol++;
			solutions.add(solutions.get(nbsol-1));
			return; 
		}

		// choisir la colonne la plus petite
		int  maxsize = Integer.MAX_VALUE;
		Cell smallestcol=null;
		for (Cell j=head.R; j!=head; j=j.R)
			if (j.row<maxsize) {
				smallestcol = j;
				maxsize = j.row;
			}

		coverCol(smallestcol.col);
		// tester toutes les lignes
		for (Cell r=smallestcol.D; r!=smallestcol; r=r.D) {
			solutions.get(nbsol).push(r.row);            	// ajouter la ligne r à la pile de solutions en cours
			for (Cell j=r.R; j!=r; j=j.R)
				coverCol(j.col);
			solveAll();
			for (Cell j=r.L; j!=r; j=j.L)  					// eliminer les modifications
				uncoverCol(j.col);
			solutions.get(nbsol).pop();
		}
		uncoverCol(smallestcol.col);

		return;
	}
	boolean solve() {
		if (head.R==head){// Quand la matrice est vide, youpi on a une solution !

			return true; 
		}

		// choisir la colonne la plus petite
		int  maxsize = Integer.MAX_VALUE;
		Cell smallestcol=null;
		for (Cell j=head.R; j!=head; j=j.R)
			if (j.row<maxsize) {
				smallestcol = j;
				maxsize = j.row;
			}

		coverCol(smallestcol.col);
		// tester toutes les lignes
		for (Cell r=smallestcol.D; r!=smallestcol; r=r.D) {
			solutions.get(nbsol).push(r.row);            	// ajouter la ligne r à la pile de solutions en cours
			for (Cell j=r.R; j!=r; j=j.R)
				coverCol(j.col);
			if(solve()) return true;
			for (Cell j=r.L; j!=r; j=j.L)  					// eliminer les modifications
				uncoverCol(j.col);
			solutions.get(nbsol).pop();
		}
		uncoverCol(smallestcol.col);

		return false;
	}

	public static void main(String args[]){
		String test[] = {"0010110", "1001001", "0110010", "1001000", "0100001", "0001101"};
		int r = test.length, c = test[0].length();
		Grid e = new Grid(c);
		ArrayList<int[]> test2 = new ArrayList<int[]>();
		for (int i=0; i<r; i++){
			test2.add(new int[c]);

			for (int j=0; j<c; j++){
				test2.get(i).clone()[j]=Integer.parseInt(String.valueOf(test[i].charAt(j)));
			}
		}
		if (e.solve()) 
			for (int i: e.solutions.get(0))
				System.out.println(i+": "+test[i]);
		else System.out.println("no solution");
	}
	
}



