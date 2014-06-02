public class sudoku {
 
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int[][] matrix = parse(args);
		printMatrix(matrix);
		System.out.println("\nSolving...\n");
		solve(matrix, 0, 0);
		printMatrix(matrix);
	}

	static int[][] parse(String[] args) { // Parse les arguments donnï¿½s en
											// entrï¿½e pour les transformer
											// en une matrice utilisable

		int n = Integer.parseInt(args[0]);
		int[][] grid = new int[n * n][n * n]; // n=2 ou n=3, les cases sont par
												// dï¿½faut a 0

		for (int i = 1; i < args.length; ++i) {
			int l = Integer.parseInt(args[i].substring(0, 1));
			int c = Integer.parseInt(args[i].substring(1, 2));
			int val = Integer.parseInt(args[i].substring(2, 3));
			grid[l][c] = val;
		}
		// TODO : eventuellement changer le parsing pour accepter des lettres
		// pour rendre compatible
		// avec des tailles de grille supï¿½rieures
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

		int xi = (x / n) * n; // coordonnï¿½es de la case en haut a gauche du bloc
								// auquel appartient la case traitï¿½e
		int yi = (y / n) * n;

		for (int i = 0; i < n; i++) { // vï¿½rifie dans le bloc
			for (int j = 0; j < n; j++) {
				if (matrix[xi + i][yi + j] == v && (xi + i != x || yi + j != y))
					return false;
			}
		}

		return true; // si aucun des testes prï¿½cï¿½dents n'a retournï¿½ false, la
						// case est valide
	}

	static boolean solve(int[][] matrix, int x, int y) { // rï¿½soud le sudoku
															// passï¿½ en
															// cherchant une
															// solution par un
															// algorithme de
															// backtracking
															// basic

		int n = (int) Math.sqrt(matrix.length);

		if (y == n * n)
			return true;

		int nextX = x + 1; // calcule les coordonnï¿½es de la prochaine case
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

	static class contrainte{
		/* Classe reprŽsentant les diffŽrentes contraintes possibles
		 * 
		 * Si type = 1, on cherche la contrainte correspondant au fait qu'une case soit remplie.
		 * On a alors a1=x, a2=y
		 * 
		 * Si type = 2, on cherche la contrainte correspondant au fait qu'une valeur existe dans une ligne.
		 * On a alors a1 le numŽro de ligne, a2 la valeur
		 * 
		 * Si type = 3, on cherche la contrainte correspondant au fait qu'une valeur existe dans une colonne.
		 * On a alors a1 le numŽro de colonne, a2 la valeur
		 * 
		 * Si type = 4, on cherche la contrainte correspondant au fait qu'une valeur existe dans un bloc.
		 * On a alors a1 le numŽro du bloc, a2 la valeur
		 */
		int type,a1,a2;
		
		public contrainte(int type, int a1, int a2){
			this.type=type;this.a1=a1;this.a2=a2;
		}
	}
	
	static int findCol(int n, contrainte c){ //Cette fonction trouve le numŽro de colonne correspondant ˆ une contrainte.
		return (c.type-1)*(n*n*n*n)+c.a2*n*n+c.a1+1;
	}
	
	static contrainte findContrainte(int n, int numCol){ // Cette fonction trouve la contrainte correspondante ˆ un numŽro de colonne
		numCol--;
		int type = numCol/(n*n*n*n)+1;
		int a2 = (numCol%type)/(n*n);
		int a1= (numCol%type)%a2;
		return new contrainte(type, a1, a2);
		
	}
	
	static int findBloc(int n, int x, int y){	// Cette fonction trouve le numŽro du bloc correspondant ˆ une case
		return (y/n)*n+(x/n);
	}
	
	static Grid sudokuToDLXGrid(int[][] matrix){
		
		int n = (int) Math.sqrt(matrix.length);
		
		Grid g = new Grid(4*n*n*n*n);
		
		for(int x=0;x<n*n;x++){
			for (int y=0;y<n*n;y++){
				if(matrix[x][y]==0){
					
				}
			}
		}
		
		
		return null;
	}
}
