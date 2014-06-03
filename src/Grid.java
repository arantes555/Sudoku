import java.util.ArrayList;
import java.util.Stack;

public class Grid {
	ArrayList<Stack<Integer>> solutions = new ArrayList<Stack<Integer>>();
	int nbsol;
	Cell head;
	Cell[] columns;

	public Grid(int nbCol) {
		columns = new Cell[nbCol + 1];
		nbsol = 0;
		solutions.add((new Stack<Integer>()));
		head = Cell.head();
		columns[0] = head;
		Cell.column(head, nbCol, columns);

	}

	public void addRow(int l, int[] col) { // ajoute une ligne a la Grid déja
											// formée, avec le numero de lignes
											// l, avec des 1 dans les colonnes
											// de col
		// /!\ Entrer les lignes dans le desordre peut donner un tableau
		// incorrect

		/*
		 * System.out.print("Adding Row "+l+" : ["); for(int i : col)
		 * System.out.print(i+", "); System.out.println("]");
		 */

		if (col.length == 0)
			return;

		Cell c = new Cell(null, columns[col[0]], l, col[0]);

		columns[col[0]].row++;

		for (int i = 1; i < col.length; i++) {
			new Cell(c, columns[col[i]], l, col[i]);
			columns[col[i]].row++;
		}
	}
	
	public int countColumns(){
		int k=0;
		for(Cell i = head.R;i!=head;i=i.R){
			k++;
		}
		return k;
	}
	
	public Cell randomCell(){
		int c=(int) Math.random()*countColumns()+1;
		Cell col=head;
		for(int i=0;i<c;i++) // on choisi une colonne aléatoirement
			col=col.R;
		int r=(int) Math.random()*col.row+1;
		Cell cell=col;
		for(int i=0;i<r;i++) // on choisi une ligne aléatoirement
			cell=cell.D;
		return cell;
	}

	public void coverCol(int c) {
		Cell C = columns[c];
		C.hideH();
		for (Cell i = C.D; i != C; i = i.D) {
			for (Cell j = i.R; j != i; j = j.R) {
				columns[j.col].row--;
				j.hideV();
			}
		}
	}

	public void uncoverCol(int c) {
		Cell C = columns[c];
		for (Cell i = C.D; i != C; i = i.D) {
			for (Cell j = i.R; j != i; j = j.R) {
				columns[j.col].row++;
				j.unHideV();
			}
		}
		C.unHideH();
	}

	void solveAll() {
		if (head.R == head) { // Quand la matrice est vide,
			// cela signifie que la pile actuelle de solutions est une solution
			// => l'enregistrer, la cloner
			nbsol++;
			solutions.add((Stack<Integer>) solutions.get(nbsol-1).clone());
			return;
		}
		// choisir la colonne la plus petite
		int maxsize = Integer.MAX_VALUE;
		Cell smallestcol = null;
		for (Cell j = head.R; j != head; j = j.R)
			if (j.row < maxsize) {
				smallestcol = j;
				maxsize = j.row;
			}

		coverCol(smallestcol.col);
		// tester toutes les lignes
		for (Cell r = smallestcol.D; r != smallestcol; r = r.D) {
			solutions.get(nbsol).push(r.row); // ajouter la ligne r à la pile de
												// solutions en cours
			for (Cell j = r.R; j != r; j = j.R)
				coverCol(j.col);
			solveAll();
			for (Cell j = r.L; j != r; j = j.L)
				// eliminer les modifications
				uncoverCol(j.col);
			solutions.get(nbsol).pop();
		}
		uncoverCol(smallestcol.col);

		return;
	}

	boolean solve() {
		// System.out.println(head.R==head);
		if (head.R == head) {// Quand la matrice est vide, youpi on a une
								// solution !

			return true;
		}

		// choisir la colonne la plus petite
		int maxsize = Integer.MAX_VALUE;
		Cell smallestcol = null;
		for (Cell j = head.R; j != head; j = j.R)
			if (j.row < maxsize) {
				smallestcol = j;
				maxsize = j.row;
			}

		//System.out.println("Smallest collumn is " + smallestcol.col);

		coverCol(smallestcol.col);
		// tester toutes les lignes
		for (Cell r = smallestcol.D; r != smallestcol; r = r.D) {
			solutions.get(nbsol).push(r.row); // ajouter la ligne r à la pile de
												// solutions en cours
			for (Cell j = r.R; j != r; j = j.R)
				coverCol(j.col);
			if (solve())
				return true;
			for (Cell j = r.L; j != r; j = j.L)
				// eliminer les modifications
				uncoverCol(j.col);
			solutions.get(nbsol).pop();
		}
		uncoverCol(smallestcol.col);

		return false;
	}
	
	void coverLine(Cell c0){
		ArrayList<Cell> l = new ArrayList<Cell>();
		l.add(c0);
		
		for(Cell c=c0.R;c!=c0;c=c.R)
			l.add(c);
		
		for(Cell c:l){
			coverCol(c.col);
		}
		
	}
}
