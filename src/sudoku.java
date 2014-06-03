import java.util.Stack;

public class sudoku {

	public static void main(String[] args) {
		/* Pour la génération d'un sudoku d'ordre n donné(ici 3), il faut écrire java sudoku g 3
		 * Pour la résolution d'un sudoku d'ordre n donné (ici 3), il écrire java nimportequoi 3 001 214
		 * La syntaxe étant pour ajouter un chiffre dans la grille à la position (x,y) et de valeur v xyv
		 */
		String choice = args[0];
		int n = Integer.parseInt(args[1]);
		int[][] matrix;
		if (choice.equals("g")){ // generate
			matrix = generateSudoku(n);
		}
		else{
			matrix = parse(args);
		}
		printMatrix(matrix);
		System.out.println("\nSolving...\n");
		Grid grid = sudokuToDLXGrid(matrix);
		grid.solve();
		Stack<Integer> g = grid.solutions.get(0);//donne une solution
		printMatrix(DLXRowsToSudoku(g));

		// solve(matrix, 0, 0);
		// printMatrix(matrix);
	}
	static int[][] parse(String[] args) { // Parse les arguments donn�s en
		// entr�e pour les transformer
		// en une matrice utilisable

		int n = Integer.parseInt(args[0]);
		int[][] grid = new int[n * n][n * n]; // n=2 ou n=3, les cases sont par
		// d�faut a 0

		for (int i = 1; i < args.length; ++i) {
			int l = Integer.parseInt(args[i].substring(0, 1));
			int c = Integer.parseInt(args[i].substring(1, 2));
			int val = Integer.parseInt(args[i].substring(2, 3));
			grid[l][c] = val;
		}
		// TODO : eventuellement changer le parsing pour accepter des lettres
		// pour rendre compatible
		// avec des tailles de grille sup�rieures
		return grid;
	}

	static void printMatrix(int[][] matrix) {
		int n = (int) Math.sqrt(matrix.length);
		for (int i = 0; i < n * n; i++) {
			if (i % n == 0)
				System.out.println(" "
						+ new String(new char[(n * n * 2 + (n - 1) * 2 + 1)])
						.replace("\0", "-"));
			for (int j = 0; j < n * n; j++) {
				if (j % n == 0)
					System.out.print("| ");
				if (matrix[i][j] == 0)
					System.out.print(" ");
				else
					System.out.print(Integer.toString(matrix[i][j]));
				System.out.print(' ');
			}
			System.out.println("|");
		}
		System.out.println(" "
				+ new String(new char[(n * n * 2 + (n - 1) * 2 + 1)]).replace(
						"\0", "-"));
	}

	static boolean isValid(int[][] matrix, int x, int y) { // verifie si une
		// case est valide
		if (matrix[x][y]==0) return false;
		int v = matrix[x][y];
		int n = (int) Math.sqrt(matrix.length);

		for (int i = 0; i < n * n; i++) { // verifie sur la ligne
			if (matrix[i][y] == v && i != x)
				return false;
		}

		for (int i = 0; i < n * n; i++) {// verifie sur la colonne
			if (matrix[x][i] == v && i != y)
				return false;
		}

		int xi = (x / n) * n; // coordonn�es de la case en haut a gauche du
		// bloc
		// auquel appartient la case trait�e
		int yi = (y / n) * n;

		for (int i = 0; i < n; i++) { // v�rifie dans le bloc
			for (int j = 0; j < n; j++) {
				if (matrix[xi + i][yi + j] == v && (xi + i != x || yi + j != y))
					return false;
			}
		}

		return true; // si aucun des testes pr�c�dents n'a retourn� false,
		// la
		// case est valide
	}

	static boolean solve(int[][] matrix, int x, int y) { // r�sout le sudoku
		// pass� en
		// cherchant une
		// solution par un
		// algorithme de
		// backtracking
		// basic

		int n = (int) Math.sqrt(matrix.length);

		if (y == n * n)
			return true;

		int nextX = x + 1; // calcule les coordonn�es de la prochaine case
		int nextY = y;
		if (nextX == n * n) {
			nextX = 0;
			nextY++;
		}

		if (matrix[x][y] != 0)
			return solve(matrix, nextX, nextY);
		else {
			for (int i = 1; i <= n * n; i++) {
				matrix[x][y] = i;
				if (isValid(matrix, x, y))
					if (solve(matrix, nextX, nextY))
						return true;
			}
			matrix[x][y] = 0;
			return false;

		}
	}

	static class contrainte {
		/*
		 * Classe repr�sentant les diff�rentes contraintes possibles
		 * 
		 * Si type = 0, on cherche la contrainte correspondant au fait qu'une
		 * case soit remplie. On a alors a1=x, a2=y
		 * 
		 * Si type = 1, on cherche la contrainte correspondant au fait qu'une
		 * valeur existe dans une ligne. On a alors a1 le num�ro de ligne, a2
		 * la valeur
		 * 
		 * Si type = 2, on cherche la contrainte correspondant au fait qu'une
		 * valeur existe dans une colonne. On a alors a1 le num�ro de colonne,
		 * a2 la valeur
		 * 
		 * Si type = 3, on cherche la contrainte correspondant au fait qu'une
		 * valeur existe dans un bloc. On a alors a1 le num�ro du bloc, a2 la
		 * valeur
		 */
		int type, a1, a2;

		public contrainte(int type, int a1, int a2) {
			this.type = type;
			this.a1 = a1;
			this.a2 = a2;
		}
	}

	static int findCol(int n, contrainte c) { // Cette fonction trouve le
		// num�ro de colonne
		// correspondant � une
		// contrainte.
		return (c.type) * (n * n * n * n) + c.a2 * n * n + c.a1 + 1;
	}

	static contrainte findContrainte(int n, int numCol) { // Cette fonction
		// trouve la
		// contrainte
		// correspondante
		// � un num�ro
		// de colonne
		numCol--;
		int type = numCol / (n * n * n * n);
		int a2 = (numCol % (n * n * n * n)) / (n * n);
		int a1 = (numCol % n*n) ;
		return new contrainte(type, a1, a2);

	}

	static int findBloc(int n, int x, int y) { // Cette fonction trouve le
		// num�ro du bloc
		// correspondant � une case
		return (y / n) * n + (x / n);
	}

	static int findRow(int n, int x, int y, int val) { // Trouve le numero de
		// ligne correspondant
		// au couple case,valeur
		return y * n * n * n * n + x * n * n + val + 1;
	}

	static int[] findCell(int n, int row){ //Trouve x, y et val avec le numero de la ligne dans Grid
		int r=row-1;
		int[] c= new int[3];
		c[1] = r/(n*n*n*n);
		c[0] = (r%(n*n*n*n))/(n*n);
		c[2] = (r%(n*n))+1;

		//System.out.println(row+ " " + c[0]+" "+c[1]+" "+c[2]);
		return c;

	}

	static int[] findContraintesCase(int n, int x, int y, int val) {

		contrainte[] contraintes = new contrainte[4];

		contraintes[0] = new contrainte(0, x, y);
		contraintes[1] = new contrainte(1, y, val);
		contraintes[2] = new contrainte(2, x, val);
		contraintes[3] = new contrainte(3, findBloc(n, x, y), val);

		int[] col = new int[4];

		for (int i = 0; i < 4; i++) {
			col[i] = findCol(n, contraintes[i]);
		}

		/*System.out.print("Case " + x + "," + y + " valeur:" + val
				+ ". Contraintes : ");
		for (int c : col) {
			System.out.print(c + ", ");
		}
		System.out.print('\n');*/

		return col;

	}

	static Grid sudokuToDLXGrid(int[][] matrix) {

		int n = (int) Math.sqrt(matrix.length);

		Grid g = new Grid(4 * n * n * n * n);

		for (int y = 0; y < n * n; y++) {
			for (int x = 0; x < n * n; x++) {
				if (matrix[y][x] == 0) {
					for (int i = 0; i < n * n; i++) {
						int[] col = findContraintesCase(n, x, y, i);
						g.addRow(findRow(n, x, y, i), col);
						// for(int c:col)
						// g.add(findRow(n,x,y,i),c);
					}
				} else {
					int[] col = findContraintesCase(n, x, y, matrix[y][x] - 1);
					g.addRow(findRow(n, x, y, matrix[y][x] - 1), col);
					// for(int c:col)
					// g.add(findRow(n,x,y,matrix[y][x]-1),c);
				}
			}
		}

		return g;
	}

	static int[][] DLXRowsToSudoku(Stack<Integer> g){

		int n2 = (int) Math.sqrt(g.size());
		int n = (int) Math.sqrt(n2);

		int[][] matrix = new int[n2][n2];

		for(int r : g){
			int[] c=findCell(n,r);
			matrix[c[1]][c[0]]=c[2];
		}

		return matrix;
	}
	/*static int[][] generateSudoku(int n){
		
		
		Grid g= sudokuToDLXGrid(new int[n*n][n*n]);
		
		Stack<Integer> stack = new Stack<Integer>();
		for(int i=0;i<(n*n*n*n);i++){
			Cell c= g.randomCell();
			stack.add(c.row);
			g.coverLine(c);
		}
			
		
		return DLXRowsToSudoku(stack);
	}*/
	
	static int[][] generateSudoku(int n){
		int[][] matrix = new int[n*n][n*n];
		generateSudoku2(matrix,0,0);
		return matrix;
	}
	
	static boolean generateSudoku2(int[][] matrix, int x, int y) { // r�sout le sudoku
		// pass� en
		// cherchant une
		// solution par un
		// algorithme de
		// backtracking
		// basic

		int n = (int) Math.sqrt(matrix.length);

		if (y == n * n)
			return true;

		int nextX = x + 1; // calcule les coordonn�es de la prochaine case
		int nextY = y;
		if (nextX == n * n) {
			nextX = 0;
			nextY++;
		}

		if (matrix[x][y] != 0)
			return solve(matrix, nextX, nextY);
		else {
			while (true) {
				matrix[x][y] = (int)Math.random()*n*n +1;
				if (isValid(matrix, x, y))
					if (solve(matrix, nextX, nextY))
						return true;
			}

		}
	}
	
}
