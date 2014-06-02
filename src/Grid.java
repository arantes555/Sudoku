
public class Grid {

	Cell head;
	Cell[] collumns;
	
	public Grid(int nbCol){
		collumns=new Cell[nbCol+1];
		head=Cell.head();
		collumns[0]=head;
		Cell.collumn(head, nbCol);
		for(Cell i=head.R;i!=head;i=i.R)
			collumns[i.col]=i;
		
	}
	
	public void addRow(int l, int[] col){	//ajoute une ligne a la Grid déja formée, avec le numero de lignes l, avec des 1 dans les collonnes de col
											// /!\ Entrer les lignes dans le desordre peut donner un tableau incorrect
		if (col.length==0) return;
		
		Cell c= new Cell(null,collumns[col[0]], l, col[0]);
		collumns[col[0]].row++;
		
		for(int i=1;i<col.length;i++){
			new Cell(c,collumns[col[i]],l,col[i]);
			collumns[col[i]].row++;
		}
	}
	

	public void coverCol(int c){
		Cell C= collumns[c];
		C.hideH();
		for(Cell i=C.D;i!=C;i=i.D){
			for(Cell j=i;j!=i;j=j.R){
				collumns[j.col].row--;
				j.hideH();
			}
		}
	}
	
	public void uncoverCol(int c){
		Cell C= collumns[c];
		C.unHideH();
		for(Cell i=C.D;i!=C;i=i.D){
			for(Cell j=i;j!=i;j=j.R){
				collumns[j.col].row++;
				j.unHideH();
			}
		}
	}
	
}
