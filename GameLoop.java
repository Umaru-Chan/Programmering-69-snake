package programmering1;

import java.awt.Color;
import java.util.Random;

public class GameLoop {

	//fönstret och fönstrets dimentioner
	private SimpleWindowMod window;
	private static final int WIDTH = 25 * 50, HEIGHT = 15 * 50;
	
	private SquareMod apple;			//äpplet
	private TailManager tailManager;	//för att hålla koll på och modifiera svansen
	private HeadManager headManager;	//för att hålla koll på och modifiera huvudet

	private int score, time;			//för att hålla koll på poäng och tid
	private long timer;					//för att hålla koll på tid
	private boolean alive;				//för att hålla koll på om spelaren är vid liv eller inte
	private Random random;				//för att enkelt skapa slumpmässiga tal

	public GameLoop() {
		window = new SimpleWindowMod(WIDTH, HEIGHT, "Snake");
		Menu.wellcomeMessage(window);
		loop();
	}

	/**
	 * metoden återställer alla variabler till vad dom ska vara när spelet börjar.
	 * */
	private void init() {
		random = new Random();
		score = time = 0;
		apple = new SquareMod(random.nextInt(25) * 50, random.nextInt(15) * 50, 50);
		alive = true;
		headManager = new HeadManager();
		tailManager = new TailManager(random);
		//se till så att man inte börjar spelet i äpplet
		while(interSects(headManager.getHead(), apple))
			apple = new SquareMod(random.nextInt(25) * 50, random.nextInt(15) * 50, 50);
		
		timer = System.currentTimeMillis();
	}
	/**
	 * metoden loopar igenom spelet medans spelaren är vid liv, om spelaren dör så får han/hon alternativet
	 * att starta om eller stänga ner spelet
	 * */
	private void loop() {
		//initiera alla variabler (används även för att ränsa alla variabler)
		init();
		while (alive) {
			// vänta för input och delaya 100ms så att spelet inte går för snabbt
			window.waitForEvent();
			
			// kolla om det har gått en sekund
			if (System.currentTimeMillis() - timer >= 1e3) {
				time++;
				timer += 1e3;
			}
			
			//rör på hela ormen
			headManager.moveSnake(tailManager, window, score);
			//om ormen nuddar äpplet
			if(interSects(headManager.getHead(), apple)){
				tailManager.addTailSquare(-50,-50);
				apple = new SquareMod(random.nextInt(25) * 50, random.nextInt(15) * 50, 50);
				score++;
				
				//se till så att äpplet inte är i huvudet eller svansen
				while(interSects(apple, headManager.getHead()) || tailManager.tailIntersects(apple))
					apple = new SquareMod(random.nextInt(25) * 50, random.nextInt(15) * 50, 50);
			}
			//om huvudet nuddar en svansbit
			if(tailManager.tailIntersects(headManager.getHead()))
				alive = false;
			//rensa fönstret
			window.clear();
			//rita huvudet
			headManager.render(window);
			//rendera svansen om det finns en svanns
			if(score > 0)
				tailManager.renderTail(window);
			//rita äpplet
			window.setLineWidth(3);
			window.setLineColor(Color.RED);
			apple.draw(window);
		}
		// om man har förlorat
		Menu.gameOver(window, score, time);
		//om man klickade på den gröna knappen i menyn så kommer man tillbaks hit, då är det bara att börja om
		loop();
	}
	/**
	 * 
	 * @param sq1 den första kvadraten
	 * @param sq2 den andra kvadraten
	 * @return	true om kvadraterna nuddar varandra
	 */
	public boolean interSects(SquareMod sq1, SquareMod sq2){
		//ett par variabler som gör resten av koden enklare att läsa
		int x1 = sq1.getX(), y1 = sq1.getY(), x2 = sq2.getX(), y2 = sq2.getY(), side1 = sq1.getSide(), side2 = sq2.getSide();
		
		if((x1 >= x2 || x1 + side1 >= x2) && (x1 <= x2 + side2)
				&& (y1 >= y2 || y1 + side1 >= y2) && (y1 <= y2 + side2))
			return true;
		
		return false;
	}
	public static void main(String[] args) {
		new GameLoop();
	}
}
