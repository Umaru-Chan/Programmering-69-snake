package programmering1;

import java.awt.Color;

public class HeadManager {

	private SquareMod head;		//kvadraten som representerar ormens huvud
	private Color headColor;	//huvudets färg
	private int dir;			//en variabel för att hålla koll på ormens riktning
	
	public HeadManager() {
		init();
	}
	/**
	 * återställer alla variabler
	 */
	private void init(){
		head = new SquareMod(13 * 50 + 2, 8 * 50 + 2, 46);
		headColor = new Color(0xaaaa44);
		dir = -1;
	}
	/**
	 * rendera ormens huvud
	 * @param window fönstret som huvudet ska ritas i
	 */
	public void render(SimpleWindowMod window){
		window.setLineWidth(4);
		window.setLineColor(headColor);
		head.draw(window);
	}
	/**
	 * metoden rör på huvudet och sista svansbiten
	 * 
	 * @param tailManager
	 * @param window
	 * @param score
	 */
	public void moveSnake(TailManager tailManager, SimpleWindowMod window, int score){
		switch (window.getKey()) {
		case 'w'://upp
			if(dir != 2){
				if (score > 0) {
					tailManager.removeTailSquare(score - 1);
					tailManager.addTailSquare(0, new SquareMod(head.getX() + 5, head.getY() + 5, 36));
				}
				if (head.getY() - 50 <= 0)
					head.move(0, window.getHeight() - 50);
				else
					head.move(0, -50);
				dir = 0;
			}
			break;
		case 'a':// vänster
			if(dir != 3){
				if (score > 0) {
					tailManager.removeTailSquare(score - 1);
					tailManager.addTailSquare(0, new SquareMod(head.getX() + 5, head.getY() + 5, 36));
				}
				if (head.getX() - 50 < 0)
					head.move(window.getWidth() - 50, 0);
				else
					head.move(-50, 0);
				dir = 1;
			}
			break;
		case 's':// ner
			if(dir != 0){
				if (score > 0) {
					tailManager.removeTailSquare(score - 1);
					tailManager.addTailSquare(0, new SquareMod(head.getX() + 5, head.getY() + 5, 36));
				}
				if (head.getY() + 50 >= window.getHeight())
					head.move(0, -window.getHeight() + 50);
				else
					head.move(0, 50);
				dir = 2;
			}
			break;
		case 'd':// höger   (alla andra riktningar har samma mönster i koden)
			if (dir != 1) {//om man inte åker vänster
				if (score > 0) {//om man har en svans så rör svansen först
					tailManager.removeTailSquare(score - 1);//ta bort den sista svansbiten
					tailManager.addTailSquare(0, new SquareMod(head.getX() + 5, head.getY() + 5, 36));//skapa en svansbit på huvudet
				}
				if (head.getX() + 50 >= window.getWidth())//om man skulle åka utanför fönstret flytta huvudet till andra sidan
					head.move(-window.getWidth() + 50, 0);
				else//om man inte skulle åka utanför fönstret så rör bara huvudet som vanligt
					head.move(50, 0);
				dir = 3;//för att hålla koll på riktningen som ormen färdas i så att man senare inte kan svänga in i ormen
			}
			break;
		}
	}
	/**
	 * @return kvadraten som representerar ormens huvud
	 */
	public SquareMod getHead(){
		return head;
	}
	/**
	 * @return huvudets x koordinat
	 */
	public int getX(){
		return head.getX();
	}
	/**
	 * @return huvudets y koordinat
	 */
	public int getY(){
		return head.getY();
	}
}
