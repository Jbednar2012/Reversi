import java.util.Random;
import java.util.Scanner;

enum Stan {EMPTY, BLACK, WHITE}
enum Winner {NOT_YET, DRAW, BLACK, WHITE}

class Field {
	Stan[][] plansza = new Stan[8][8];
	Field(){
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++){
				if(i==3&&j==3||i==4&&j==4)
					plansza[i][j] = Stan.BLACK;
				else if(i==3&&j==4||i==4&&j==3)
					plansza[i][j] = Stan.WHITE;
				else plansza[i][j] = Stan.EMPTY;
			}
	}
	
	void printField()
	{
		String row = "";
		System.out.println(" X  1 2 3 4 5 6 7 8\nY -------------------");
		for(int i=0;i<8;i++){
			char id = (char)(i+'0'+1);
			row = id+" | ";
			for(int j=0;j<8;j++)
				switch(plansza[i][j]){
					case EMPTY: row+="  "; break;
					case BLACK: row+="X "; break;
					case WHITE: row+="O "; break;
					default: row+="E "; break;
				}
			row+="|";
			System.out.println(row);
		}
		System.out.println("  -------------------");
	}
}

class Game{
	Winner win;
	Stan kolejka;
	int immobilityBlocker;
	Game(){
		win = Winner.NOT_YET;
		immobilityBlocker = 0;
	}
	
	boolean replacePossible(Field field, Stan turn, Stan opponent, int x, int y, boolean[] directions)
	{
		if(field.plansza[x][y]!=Stan.EMPTY) return false;
		int i, j, possibility=0;
		check_right:	for(i=x+1;i<8;i++){
							j=y;
							if(field.plansza[i][j]==opponent)
								continue check_right;
							else if(field.plansza[i][j]==turn&&field.plansza[i-1][j]==opponent){
								possibility+=1;
								directions[0] = true;
								break check_right;
							}
							else break check_right;
		}
		check_right_down:	for(i=x+1;i<8;i++){
								j=y+(i-x);
								if(j<8)
									if(field.plansza[i][j]==opponent){
										continue check_right_down;
									}
									else if(field.plansza[i][j]==turn&&field.plansza[i-1][j-1]==opponent){
										possibility+=1;
										directions[1] = true;
										break check_right_down;
									}
							else break check_right_down;
		}
		check_down:	for(j=y+1;j<8;j++){
						i=x;
						if(field.plansza[i][j]==opponent)
							continue check_down;
						else if(field.plansza[i][j]==turn&&field.plansza[i][j-1]==opponent){
							possibility+=1;
							directions[2] = true;
							break check_down;
						}
							else break check_down;
		}
		check_left_down:	for(i=x-1;i>=0;i--){
								j=y+(x-i);
								if(j<8)
									if(field.plansza[i][j]==opponent){
										continue check_left_down;
									}
									else if(field.plansza[i][j]==turn&&field.plansza[i+1][j-1]==opponent){
										possibility+=1;
										directions[3] = true;
										break check_left_down;
									}
							else break check_left_down;
		}
		check_left: for(i=x-1;i>=0;i--){
						j=y;
						if(field.plansza[i][j]==opponent)
							continue check_left;
						else if(field.plansza[i][j]==turn&&field.plansza[i+1][j]==opponent){
							possibility+=1;
							directions[4] = true;
							break check_left;
						}
						else break check_left;
		}
		check_left_up: 	for(i=x-1;i>=0;i--){
							j=y-(x-i);
							if(j>=0)
								if(field.plansza[i][j]==opponent){
									continue check_left_up;
								}
							else if(field.plansza[i][j]==turn&&field.plansza[i+1][j+1]==opponent){
								possibility+=1;
								directions[5] = true;
								break check_left_up;
							}
							else break check_left_up;
		}
		check_up: 	for(j=y-1;j>=0;j--){
						i=x;
						if(field.plansza[i][j]==opponent)
							continue check_up;
						else if(field.plansza[i][j]==turn&&field.plansza[i][j+1]==opponent){
							possibility+=1;
							directions[6] = true;
							break check_up;
						}
						else break check_up;
		}
		check_right_up: for(i=x+1;i<8;i++){
							j=y-(i-x);
							if(j>=0)
							if(field.plansza[i][j]==opponent){
								continue check_right_up;
							}
							else if(field.plansza[i][j]==turn&&field.plansza[i-1][j+1]==opponent){
								possibility+=1;
								directions[7] = true;
								break check_right_up;
							}
							else break check_right_up;
		}
		//System.out.println("Liczba mo¿liwosci dla punktu <"+(x+1)+", "+(y+1)+">: "+possibility);
		if(possibility>0) return true;
		else return false;
	}
	
	void replace(Field field, Stan turn, Stan opponent, int x, int y, boolean[] directions)
	{
		int i, j;
		if(directions[0]){
			i=x+1;
			j=y;
			do{
				field.plansza[i][j]=turn;
				i++;
			}while(field.plansza[i][j]==opponent);
		}
		if(directions[1]){
			i=x+1;
			j=y+1;
			do{
				field.plansza[i][j]=turn;
				i++; j++;
			}while(field.plansza[i][j]==opponent);
		}
		if(directions[2]){
			i=x;
			j=y+1;
			do{
				field.plansza[i][j]=turn;
				j++;
			}while(field.plansza[i][j]==opponent);
		}
		if(directions[3]){
			i=x-1;
			j=y+1;
			do{
				field.plansza[i][j]=turn;
				i--; j++;
			}while(field.plansza[i][j]==opponent);
		}
		if(directions[4]){
			i=x-1;
			j=y;
			do{
				field.plansza[i][j]=turn;
				i--;
			}while(field.plansza[i][j]==opponent);
		}
		if(directions[5]){
			i=x-1;
			j=y-1;
			do{
				field.plansza[i][j]=turn;
				i--; j--;
			}while(field.plansza[i][j]==opponent);
		}
		if(directions[6]){
			i=x;
			j=y-1;
			do{
				field.plansza[i][j]=turn;
				j--;
			}while(field.plansza[i][j]==opponent);
		}
		if(directions[7])
		{
			i=x+1;
			j=y-1;
			do{
				field.plansza[i][j]=turn;
				i++; j--;
			}while(field.plansza[i][j]==opponent);
		}
	}
	
	boolean canIMove(Field field, Stan turn, Stan opponent)
	{
		boolean[] checker = new boolean[8];
		int possibilities = 0;
		for(int i=0;i<8;i++)
			for(int j=0; j<8; j++)
				if(replacePossible(field,turn,opponent,i,j,checker))
					possibilities++;
		if(possibilities>0) return true;
		else return false;
	}
	
	boolean immobility(){
		if(immobilityBlocker == 2)
			return true;
		else return false;
	}
	
	void myTurn(Field field, Stan turn, Stan opponent)
	{
		if(turn==opponent) System.out.println("Nie mo¿esz sam siebie zaatakowaæ!");
		if(canIMove(field,turn,opponent)){
			int x, y;
			immobilityBlocker = 0;
			boolean move_possible = false;
			while(!move_possible){
				Scanner wspolrzedne = new Scanner(System.in);
				System.out.println("Gdzie chcesz zamiescic swoj pionek?");
				System.out.print("X: ");
				y = wspolrzedne.nextInt();
				while(y<1||y>8){
					System.out.println("Liczba musi miescic sie w przedziale od 1 do 10!");
					y = wspolrzedne.nextInt();
				}
				System.out.print("Y: ");
				x = wspolrzedne.nextInt();
				while(x<1||x>8){
					System.out.println("Liczba musi miescic sie w przedziale od 1 do 10!");
					x = wspolrzedne.nextInt();
				}
				x--; y--;
				if(field.plansza[x][y]==Stan.EMPTY){
					boolean[] directions = new boolean[8];
					if(replacePossible(field,turn,opponent,x,y,directions)){
						move_possible = true;
						field.plansza[x][y] = turn;
						replace(field,turn,opponent,x,y,directions);;
					}
					else System.out.println("Ten ruch nie jest mo¿liwy!");
				}
				else
					System.out.println("Miejsce zajête!");
				}
			}
			else
			{
				System.out.println("Brak mo¿liwych ruchów!");
				immobilityBlocker++;
			}
		System.out.print(System.lineSeparator());
	}
	
	boolean fullyFilled(Field field)
	{
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				if(field.plansza[i][j]==Stan.EMPTY) return false;
		}
		return true;
	}
	
	Winner whoIsEliminated(Field field)
	{
		int black_counter = 0, white_counter = 0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				switch(field.plansza[i][j]){
					case BLACK: black_counter++; break;
					case WHITE: white_counter++; break;
				}
		}
		if(black_counter==0) return Winner.BLACK;
		if(white_counter==0) return Winner.WHITE;
		return Winner.NOT_YET;
	}
	
	Winner checkWhoPrevailed(Field field){
		int black_counter = 0, white_counter = 0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				switch(field.plansza[i][j]){
					case BLACK: black_counter++; break;
					case WHITE: white_counter++;
				}
		}
		System.out.println("Czarne: "+black_counter+"\t\tBia³e: "+white_counter);
		if(black_counter>white_counter) return Winner.BLACK;
		else if(black_counter<white_counter) return Winner.WHITE;
		else return Winner.DRAW;
	}

	boolean Settled(Field field)
	{
		String victor = "";
		if(fullyFilled(field)||immobilityBlocker>1){
			win = checkWhoPrevailed(field);
			switch(win){
				case BLACK: victor = "Czarny wygra³ przez przewagê liczebn¹ na wype³nionej planszy!"; break;
				case WHITE: victor = "Bia³y wygra³ przez przewagê liczebn¹ na wype³nionej planszy!"; break;
				case DRAW: victor = "Mamy remis!"; break;
			}
		}
		else{
			win = whoIsEliminated(field);
			switch(win){
				case BLACK: victor = "Bia³y wygra³ przez eliminacjê przeciwnika!"; break;
				case WHITE: victor = "Czarny wygra³ przez eliminacjê przeciwnika!"; break;
				case NOT_YET: return false;
			}
		}
		System.out.println(victor);
		return true;
	}
}

class Gameplay
{
	void hotSeat(Field field, Game game)
	{
		field.printField();
		game.kolejka = Stan.BLACK;
		do{
			if(game.kolejka==Stan.BLACK){
				System.out.println("\nCZARNY\n");
				game.myTurn(field,game.kolejka,Stan.WHITE);
				game.kolejka = Stan.WHITE;
			}
			else if(game.kolejka==Stan.WHITE){
				System.out.println("\nBIA£Y\n");
				game.myTurn(field,game.kolejka,Stan.BLACK);
				game.kolejka = Stan.BLACK;
			}
			field.printField();
		}while(!game.Settled(field));
		System.gc();
	}
	
	void serverPlay(Field field, Game game)
	{
		System.out.println("Coming soon");
		System.gc();
	}
}

public class Reversi{
	static Field pole = new Field();
	static Game gra = new Game();
	static Gameplay rozgrywka = new Gameplay();
	
	public static void main(String[] args){
		int choice;
		Scanner begin = new Scanner(System.in);
		System.out.println("\nW którym trybie chcesz graæ?\n1 - \"Gor¹ce krzes³o\"\n2 - Gra przez serwer");
		choice = begin.nextInt();
		while(choice<1||choice>2){
			System.out.println("Nie ma takiej opcji\nSpróbuj ponownie");
			choice = begin.nextInt();
		}
		if(choice == 1)
			rozgrywka.hotSeat(pole, gra);
		else
			rozgrywka.serverPlay(pole, gra);
	}
}