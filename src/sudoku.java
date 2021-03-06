import java.util.ArrayList;
import java.util.Stack;

public class sudoku {

	public static void main(String[] args) {
		/*
		 * Pour la génération d'un sudoku d'ordre n donné(ici 3), il faut
		 * écrire java sudoku g 3 Pour la résolution d'un sudoku d'ordre n
		 * donné (ici 3), il écrire java nimportequoi 3 001 214 La syntaxe
		 * étant pour ajouter un chiffre dans la grille à la position (x,y) et
		 * de valeur v xyv
		 */
		String choice = args[0];
		int n = Integer.parseInt(args[1]);
		int[][] matrix = null;
		if (choice.equals("g")) { // Arguments : "g" n, on genere une grille minimale de taille n 
			matrix = generateSudoku(n);
			printMatrix(matrix);
			System.out.println("Nombre de dévoilés : "+nbDevoile(matrix));
			System.out.println("\nSolution :\n");
			Grid grid = sudokuToDLXGrid(matrix);
			grid.solve();
			Stack<Integer> g = grid.solutions.get(0);// donne une solution
			printMatrix(DLXRowsToSudoku(g));
		} else if(choice.equals("G")){ // Arguments : "G" n g, on genere g grilles minimales de taille n, et on donne celle ayant le plus petit nombre de devoiles 
			int iMax = Integer.parseInt(args[2]);
			int nbDevMin = 81;
			for(int i =0;i<iMax; i++){
				int[][] matrixTemp = generateSudoku(n);
				if(nbDevoile(matrixTemp)<nbDevMin){
					matrix = matrixTemp;
					nbDevMin = nbDevoile(matrix);
				}
			}
			printMatrix(matrix);
			System.out.println("Nombre de dévoilés : "+nbDevoile(matrix));
			System.out.println("\nSolution :\n");
			Grid grid = sudokuToDLXGrid(matrix);
			grid.solve();
			Stack<Integer> g = grid.solutions.get(0);// donne une solution
			printMatrix(DLXRowsToSudoku(g));
		} else if (choice.equals("b")) { //Arguments : "b" n sudoku, on resout la grille sudoku de taille n par bactracking simple 
			matrix = parse(args);
			printMatrix(matrix);
			System.out.println("\nSolving...\n");
			long startTime = System.nanoTime();
			solve(matrix, 0, 0);
			long endTime = System.nanoTime();

			double duration = (endTime - startTime)/1000000.;

			printMatrix(matrix);
			System.out.println("Execution time : " + duration + "ms");
		} else if (choice.equals("t")){
			int iMax = Integer.parseInt(args[2]);
			double[] tempsG = new double[iMax]; //liste des temps de generation
			double[] tempsB = new double[iMax]; //liste des temps de solve en backTracking
			double[] tempsX = new double[iMax]; //liste des temps de solve en dancingLinks
			
			for(int i =0;i<iMax; i++){ //on genere iMax grilles
				long startTime = System.nanoTime();
				matrix = generateSudoku(n);
				long endTime = System.nanoTime();
				tempsG[i]=(endTime - startTime)/1000000.;		
				
				startTime = System.nanoTime();
				solve(matrix, 0, 0); //on resout par backtraking simple
				endTime = System.nanoTime();
				tempsB[i]=(endTime - startTime)/1000000.;
				
				startTime = System.nanoTime();
				Grid grid = sudokuToDLXGrid(matrix);
				grid.solve(); // on resout par dancing links
				matrix = DLXRowsToSudoku(grid.solutions.get(0));
				endTime = System.nanoTime();
				tempsX[i]=(endTime - startTime)/1000000.;		
				
			}
			System.out.println("Sur "+iMax+" grilles :");
			System.out.println("Temps de generation moyen : "+mean(tempsG)+" ms");
			System.out.println("Temps de resolution par Backtracking simple moyen : "+mean(tempsB)+" ms");
			System.out.println("Temps de resolution par Dancing Links moyen : "+mean(tempsX)+" ms");

		} else if (choice.equals("x")) { //Arguments : "x" n sudoku, on resout la grille sudoku de taille n par dancing links 
			matrix = parse(args);
			printMatrix(matrix);
			System.out.println("\nSolving...\n");
			long startTime = System.nanoTime();
			Grid grid = sudokuToDLXGrid(matrix);
			grid.solve();
			Stack<Integer> g = grid.solutions.get(0);// donne une solution
			long endTime = System.nanoTime();
			double duration = (endTime - startTime)/1000000.;

			printMatrix(DLXRowsToSudoku(g));
			System.out.println("Execution time : " + duration + "ms");

		} else {
			System.out.println("Erreur d'argument");
		}
	}
	
	public static double mean(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}

	static int[][] parse(String[] args) { // Parse les arguments donn�s en
		// entr�e pour les transformer
		// en une matrice utilisable

		int n = Integer.parseInt(args[1]);
		int[][] grid = new int[n * n][n * n]; // n=2 ou n=3, les cases sont par
		// d�faut a 0

		for (int i = 2; i < args.length; ++i) {
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
		if (matrix[x][y] == 0)
			return false;
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
		int a1 = (numCol % n * n);
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

	static int[] findCell(int n, int row) { // Trouve x, y et val avec le numero
											// de la ligne dans Grid
		int r = row - 1;
		int[] c = new int[3];
		c[1] = r / (n * n * n * n);
		c[0] = (r % (n * n * n * n)) / (n * n);
		c[2] = (r % (n * n)) + 1;

		// System.out.println(row+ " " + c[0]+" "+c[1]+" "+c[2]);
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

		/*
		 * System.out.print("Case " + x + "," + y + " valeur:" + val +
		 * ". Contraintes : "); for (int c : col) { System.out.print(c + ", ");
		 * } System.out.print('\n');
		 */

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

	static int[][] DLXRowsToSudoku(Stack<Integer> g) {

		int n2 = (int) Math.sqrt(g.size());
		int n = (int) Math.sqrt(n2);

		int[][] matrix = new int[n2][n2];

		for (int r : g) {
			int[] c = findCell(n, r);
			matrix[c[1]][c[0]] = c[2];
		}

		return matrix;
	}

	/*
	 * static int[][] generateSudoku(int n){
	 * 
	 * 
	 * Grid g= sudokuToDLXGrid(new int[n*n][n*n]);
	 * 
	 * Stack<Integer> stack = new Stack<Integer>(); for(int
	 * i=0;i<(n*n*n*n);i++){ Cell c= g.randomCell(); stack.add(c.row);
	 * g.coverLine(c); }
	 * 
	 * 
	 * return DLXRowsToSudoku(stack); }
	 */

	static int[][] generateSudoku(int n) {
		//Genere un sudoku minimal
		
		int[][] matrix = new int[n * n][n * n];
		generateSudoku2(matrix, 0, 0); // on genere un sudoku plein
		
		
		ArrayList<XY> cases = new ArrayList<XY>(); 
		for(int x=0;x<n*n;x++){
			for(int y=0;y<n*n;y++){
				cases.add(new XY(x,y)); //on cree une liste des cases de ce sudoku
			}
		}
		
		while(!cases.isEmpty()){ //tant que la liste n'est pas vide
			XY c= cases.remove((int) (Math.random()*cases.size())); //on choisit une case aleatoirement et on la retire de la liste
			int temp=matrix[c.x][c.y];
			matrix[c.x][c.y]=0; //on vide cette case
			
			if(nbSol(matrix)>1) 
				matrix[c.x][c.y]=temp; //S'il existe plusieurs solutions, on remet la valeur
		}
		
		
		return matrix;
	}
	
	static int nbSol(int[][] matrix){
		Grid grid = sudokuToDLXGrid(matrix);
		grid.solveAll();
		return grid.nbsol;
	}
	
	static class XY{
		int x;
		int y;
		XY(int x, int y){
			this.x=x;
			this.y=y;
		}
	}
	
	static int nbDevoile(int[][] matrix){
		int n = 0;
		for(int[] col : matrix)
			for(int c : col)
				if(c!=0)
					n++;
		return n;
	}

	static boolean generateSudoku2(int[][] matrix, int x, int y) { // r�sout
																	// le sudoku
		// pass� en
		// cherchant une
		// solution par un
		// algorithme de
		// backtracking
		// basic
		// => Genere un sudoku plein

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
			return generateSudoku2(matrix, nextX, nextY);
		else {
			ArrayList<Integer> valeurs = new ArrayList<Integer>();
			for(int i =1; i <= n*n; i++)
				valeurs.add(new Integer(i)); // on cree une liste de valeurs possibles
			while (!valeurs.isEmpty()) {
				matrix[x][y] = valeurs.remove((int) (Math.random()*valeurs.size())); //on choisi aleatoirement une des valeurs non testees
				//printMatrix(matrix);
				if (isValid(matrix, x, y))
					if (generateSudoku2(matrix, nextX, nextY))
						return true;
			}
			matrix[x][y] = 0;
			return false;

		}
	}

}
