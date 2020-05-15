package ticTacToe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicTacToe extends Application {
	private ImageView[][] tiles = new ImageView[3][3];
	private int[][] a = new int[3][3];
	private int p;	//player's token (X or O)
	private int c;	//computer's token(O or X)
	private int count;
	@Override
	public void init() {
		p = new Random().nextInt(2);
		//p=1;	
		if (p==0) p=-1;
		c = p==1 ? -1 : 1;
		//System.out.println(p+" "+c);
	}
	
	private Image playerImage() {
		try {
			if (p==1) return new Image(new FileInputStream("src/1.png"));
			else return new Image(new FileInputStream("src/2.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private Image computerImage() {
		try {
			if (p==-1) return new Image(new FileInputStream("src/1.png"));
			else return new Image(new FileInputStream("src/2.png"));
		} catch (FileNotFoundException e) {
			//System.out.println("File not found!");
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void start(Stage stage){
		HBox h = new HBox();
		for (int i=0;i<3;i++) {
			int x=i;
			VBox v = new VBox();
			for(int j=0;j<3;j++) {
				int y=j;
				try {
					ImageView iv = new ImageView(new Image(new FileInputStream("src/0.png")));
					iv.setFitHeight(100);
					iv.setFitWidth(100);
					tiles[i][j] = iv;
					iv.setOnMouseClicked((MouseEvent me)->{
						if (a[x][y]!=0) return;
						putPlayersTokenOn(iv,x,y);
						computerMoves();
					});
					v.getChildren().add(iv);
					v.setMaxHeight(305);
				} catch (FileNotFoundException e) {
					//System.out.println("File not found!");
					e.printStackTrace();
				}
				if (j<2) v.getChildren().add(new Separator());			
			}
			h.getChildren().add(v);
			h.setMaxHeight(305);
			Separator vertSep = new Separator();
			vertSep.setOrientation(Orientation.VERTICAL);
			if (i<2) h.getChildren().add(vertSep);	
		}
		if (p==1) putComputersTokenOn(1,1);	//if the computer starts, it starts from center
		stage.setScene(new Scene(h,305,305));
		stage.show();
	}
	
	private void putPlayersTokenOn(ImageView iv, int x,int y) {//event handler when a new tile has been clicked on
		count++;
		//System.out.println(x+" "+y+" clicked ");
		iv.setImage(playerImage());
		a[x][y]=p;
	}
	
	private boolean putComputersTokenOn(int x, int y) {
		//System.out.println("Computer moves on "+x+" "+y);
		if(a[x][y]!=0) return false;	//if computer can't move here
		count++;
		tiles[x][y].setImage(computerImage());
		a[x][y] = c;
		return true;
	}
	
	private boolean findWinner() {
		int sumD=0;
		int sumS=0;
		sumS=a[0][2]+a[1][1]+a[2][0];
		for (int i=0;i<3;i++) {
			int sumH=0;
			int sumV=0;
			for (int j=0;j<3;j++) {
				//System.out.print(a[j][i]+" ");
				sumV+=a[i][j];
				sumH+=a[j][i];
				if (j==0) sumD+=a[i][i];
				if (sumH==3 || sumV==3 || sumD==3 || sumS==3) {
					//TODO make an alert
					//System.out.print(sumH+""+sumV+""+sumD);
					System.out.println(p==1 ? " Player has won" : " Computer has won");
					return true;
				}
				if (sumH==-3 || sumV==-3 || sumD==-3 || sumS==-3) {
					//TODO make an alert
					System.out.println(p==-1 ? "Player has won" : "Computer has won");
					return true;
				}
			}
			//System.out.println();
		}
		return false;
	}
	
	private boolean forcedMove() {	//returns true if the computer is forced to make its move
		int count=0;
		int sumD = 0;
		int sumS = 0;
		int freeXY=2;
		for (int i=0;i<3;i++) {
			int freeX=2,freeY=2,sumH=0,sumV=0;
			//forced move on the diagonal
			sumD+=a[i][i];
			if (a[i][i]==0) freeXY=i;
			if (sumD==2||sumD==-2) {
				//System.out.println("	SumD = "+sumD);

				if (putComputersTokenOn(freeXY,freeXY)) {
					sumD+=c;
					return true;
				}
			}		
			freeXY=0;
			
			for(int j=0;j<3;j++) {				
				if (count==8 && c==-1) putComputersTokenOn(freeX,freeY);
				////System.out.print(a[i][j]+" ");	//vert
				////System.out.print(a[j][i]+" ");	//horiz
				
				//forced move on the secondary diagonal
				if (i==0) {
					sumS+=a[j][2-j];
					if (a[j][2-j]==0) freeXY=j;
					if (sumS==2||sumS==-2) {
						//System.out.println("	SumS = "+sumS);

						if (putComputersTokenOn(freeXY,2-freeXY)) {
							sumS+=c;
							return true;
						}
					}
				}
				//forced move on a column
				sumV+=a[i][j];
				if (a[i][j]==0) freeY=j;
				if (sumV==2||sumV==-2) {
					//System.out.println("	SumH = "+sumH+" SumV = "+sumV);
					
					if (putComputersTokenOn(i,freeY)) {
						sumV+=c;
						return true;
					}
				}
				
				//forced move on a row
				sumH+=a[j][i];
				if (a[j][i]==0) freeX=j;
				if (sumH==2||sumH==-2) {
					//System.out.println("	SumH = "+sumH+" SumV = "+sumV);

					if (putComputersTokenOn(freeX,i)) {
						sumH+=c;
						return true;
					}
				}
			}
			//System.out.println("	SumH = "+sumH+" SumV = "+sumV);
		}
		
		
		
		return false;
	}
	
	private void computerMoves() {
		//System.out.println("COUNT="+count);
		if (findWinner())return;
		
		if (!forcedMove()) {
			//strategy to actually win
			//System.out.println("Not a forced move:");
			if (count==1) {	//computer's first move if playing with O's
							//is putting its token in a random corner
				if (!putComputersTokenOn(1,1)) {
					putComputersTokenOn(new Random().nextInt(2)*2,new Random().nextInt(2)*2);
					return;
				}
			}
			if (count==2) {	//computer's second move if playing with X's
							//is putting an X into an unoccupied corner
				if (a[0][0]!=0) {putComputersTokenOn(2,2);return;}
				if (a[0][2]!=0) {putComputersTokenOn(2,0);return;}
				if (a[2][0]!=0) {putComputersTokenOn(0,2);return;}
				if (a[2][2]!=0) {putComputersTokenOn(0,0);return;}
			}
			if (count==4) {	//computer's third move if playing with X's
							//is forming a fork: X	 X
							//					   X
				int x=0;
				int y=0;
				for (int i=0;i<3;i++) {
					for (int j=0;j<3;j++) {
						if (a[i][j]==1) {
							if (i<1) x=2; else x=0;
							if (j<2) y=0; else y=2;
						}
					}
				}
				if (putComputersTokenOn(x,y)) return;				
			}
			//a randomized move with preference for corners
			boolean couldOccupyCorner=false;
			for (int i=0;i<3 && !couldOccupyCorner;i+=2) {
				for (int j=0;j<3 && !couldOccupyCorner;j+=2) {
					if (putComputersTokenOn(i,j)) {
						couldOccupyCorner = true;
					}
				}
			}
			//System.out.println("COUNT="+count);
			if (!couldOccupyCorner && !(count==9 && c==1)) {
				int i=0;
				int j=0;
				while(!putComputersTokenOn(i,j)) {
					i = new Random().nextInt(3);
					j = new Random().nextInt(3);
//					try {
//					Thread.sleep(100);
//					}catch (Exception e) {};
				}
			}
			if (count==9) {
				if (!findWinner()) System.out.println("Draw");
				//TODO make an alert
			}
		}	
		findWinner();
	}
	
	public static void main(String[] args) {
		launch();
	}
}
