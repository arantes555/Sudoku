
public class Cell { //Classe qui implŽmente une cellule de matrice de l'agorithme des liens dansants

	public Cell U,D,L,R;
	public int row,col;
	
	public Cell(Cell R, Cell D,int row, int col){ // crŽe une nouvelle cellule ˆ gauche de R et en haut de D
		if (R==null)
			this.R=this.L=this;
		else{
			this.R=R;
			this.L=R.L;
		}
		
		if (D==null)
			this.D=this.U=this;
		else{
			this.D=D;
			this.U=D.U;
		}
		
		this.row=row;
		this.col=col;
	}
	
	public static Cell head(){ // crŽe une nouvelle tete de matrice
		return new Cell(null,null,0,0);
	}
	
	public static void collumn(Cell R,int n){ // crŽe n nouvelles colonnes, placŽes a gauche de R
		Cell c = new Cell(R,null,0,n);
		if(n>0) collumn(c,n-1);	
	}
	
	public void hideV(){
		U.D=D;
		D.U=U;
	}
	
	public void hideH(){
		L.R=R;
		R.L=L;
	}
	
	public void unHideV(){
		U.D=this;
		D.U=this;
	}
	
	public void unHideH(){
		L.R=this;
		R.L=this;
	}
	
	
	//public static boolean solve(){}
}
