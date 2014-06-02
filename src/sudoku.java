public class sudoku {
 
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int[][] matrix = parse(args);
		printMatrix(matrix);
		System.out.println("\nSolving...\n");
		solve(matrix, 0, 0);
		printMatrix(matrix);
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

		int xi = (x / n) * n; // coordonn�es de la case en haut a gauche du bloc
								// auquel appartient la case trait�e
		int yi = (y / n) * n;

		for (int i = 0; i < n; i++) { // v�rifie dans le bloc
			for (int j = 0; j < n; j++) {
				if (matrix[xi + i][yi + j] == v && (xi + i != x || yi + j != y))
					return false;
			}
		}

		return true; // si aucun des testes pr�c�dents n'a retourn� false, la
						// case est valide
	}

	static boolean solve(int[][] matrix, int x, int y) { // r�soud le sudoku
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

	static Grid sudokuToDLXGrid(int[][] matrix){
		
		
		
		return null;
	}
}
