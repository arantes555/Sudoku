public class sudoku {

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		printMatrix(parse(args));
	}

	static int[][] parse(String[] args) {	// Parse les arguments donnés en entrée pour les transformer
											// en une matrice utilisable
		
		int n = Integer.parseInt(args[0]);
		int[][] grid = new int[n * n][n * n]; 	// n=2 ou n=3, les cases sont par
												// défaut a 0

		for (int i = 1; i < args.length; ++i) {
			int l = Integer.parseInt(args[i].substring(0, 1));
			int c = Integer.parseInt(args[i].substring(1, 2));
			int val = Integer.parseInt(args[i].substring(2, 3));
			grid[l][c] = val;
		}
		// TODO : eventuellement changer le parsing pour accepter des lettres
		// pour rendre compatible
		// avec des tailles de grille supérieures
		return grid;
	}

	static void printMatrix(int[][] matrix) {
		int n = (int) Math.sqrt(matrix.length);
		for (int i = 0; i < n * n; ++i) {
			if (i % n == 0)
				System.out
						.println(" "
								+ String.format("%0" + (n * 2 + (n - 1) + 1)
										+ "c", '-'));
			for (int j = 0; j < n * n; ++j) {
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
				+ String.format("%0" + (n * 2 + (n - 1) + 1) + "c", '-'));
	}

}
