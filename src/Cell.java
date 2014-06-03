public class Cell { // Classe qui impl�mente une cellule de matrice de
					// l'agorithme des liens dansants

	public Cell U, D, L, R;
	public int row, col;

	public Cell(Cell R, Cell D, int row, int col) { // cr�e une nouvelle
													// cellule � gauche de R
													// et en haut de D
		if (R == null)
			this.R = this.L = this;
		else {
			this.R = R;
			this.L = R.L;
			R.L = this;
			L.R = this;
		}

		if (D == null)
			this.D = this.U = this;
		else {
			this.D = D;
			this.U = D.U;
			U.D = this;
			D.U = this;
		}

		this.row = row;
		this.col = col;

		System.out.println("Creating cell " + row + ", " + col);
	}

	public static Cell head() { // cr�e une nouvelle tete de matrice
		return new Cell(null, null, 0, 0);
	}

	public static void column(Cell R, int n, Cell[] col) { // cr�e n nouvelles
															// colonnes,
															// plac�es a
															// gauche de R
		for (int c = 0; c < n; c++)
			col[c + 1] = new Cell(R, null, 0, c + 1);
	}

	public void hideV() {
		U.D = D;
		D.U = U;
	}

	public void hideH() {
		L.R = R;
		R.L = L;
	}

	public void unHideV() {
		U.D = this;
		D.U = this;
	}

	public void unHideH() {
		L.R = this;
		R.L = this;
	}
}
