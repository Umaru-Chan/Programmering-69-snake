package programmering1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TailManager {
	//listor för att hålla information om svansen
	private List<SquareMod> tailSquare;
	private List<Color> tailColor;
	private List<Boolean> tailFill;
	//en Random variabel som används för att skaffa slumpmässiga tal och booleans
	private Random random;
	
	public TailManager(Random random){
		this.random = random;
		init();
	}
	/**
	 * återställer alla variabler
	 */
	private void init(){
		tailSquare = new ArrayList<>();
		tailFill = new ArrayList<>();
		tailColor = new ArrayList<>();
	} 
	/**
	 * rendera svansen
	 * @param window fönstret att rita i
	 */
	public void renderTail(SimpleWindowMod window){
		for (int i = 0; i < tailSquare.size(); i++){
			window.setLineWidth(3);
			//rita upp svansbiten
			window.setLineColor(tailColor.get(i));
			tailSquare.get(i).draw(window);
			//fyll i svansbiten om det så behövs
			if(tailFill.get(i)){
				window.moveTo(tailSquare.get(i).getX() + tailSquare.get(i).getSide() / 2, tailSquare.get(i).getY());
				window.setLineWidth(tailSquare.get(i).getSide());
				window.lineTo(tailSquare.get(i).getX() + tailSquare.get(i).getSide() / 2,
						tailSquare.get(i).getY() + tailSquare.get(i).getSide());
			}
		}
	}
	/**
	 * 
	 * @param sq1 kvadraten att kolla svansen mot
	 * @return true om sq1 nuddar svansen
	 */
	public boolean tailIntersects(SquareMod sq1){
		for(SquareMod sq2 : tailSquare){
			int x1 = sq1.getX(), y1 = sq1.getY(), x2 = sq2.getX(), y2 = sq2.getY(), side1 = sq1.getSide(), side2 = sq2.getSide();
			if((x1 >= x2 || x1 + side1 >= x2) && (x1 <= x2 + side2)
					&& (y1 >= y2 || y1 + side1 >= y2) && (y1 <= y2 + side2))
				return true;
		}
		return false;
	}
	/**
	 * lägger till en svansbit, en färg och en boolean för att hålla koll på om svansbiten ska vara helmålad eller inte.
	 * */
	public void addTailSquare(int x, int y){
		tailSquare.add(new SquareMod(x + 5, y + 5, 36));
		tailColor.add(new Color(random.nextInt(0x888888)));
		tailFill.add(random.nextBoolean());
	}
	/**
	 * tar bort svansbiten i indexet index
	 * @param index indexet som svansbiten ligger i
	 */
	public void removeTailSquare(int index){
		tailSquare.remove(index);
	}
	/**
	 * lägg till en svansbit i indexet index
	 * @param index indexet som man vill ha den nya svansbiten i
	 * @param newSquare den nya svansbiten
	 */
	public void addTailSquare(int index, SquareMod newSquare){
		tailSquare.add(0, newSquare);
	}
}
