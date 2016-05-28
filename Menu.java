package programmering1;

import java.awt.Color;

public class Menu {
	//för att hålla koll på spelarens bästa poäng
	private static int highScore = 0;
	/**
	 * rita upp ett välkomstmeddelande
	 * @param w fönstret att rita i
	 */
	public static void wellcomeMessage(SimpleWindowMod w){
		//måla upp bakgrunden
		w.setLineColor(new Color(0x006600));
		w.setLineWidth(w.getHeight());
		w.moveTo(0, w.getHeight() / 2);
		w.lineTo(w.getWidth(), w.getHeight() / 2);
		//skriv upp ett meddelande med instruktioner
		w.setLineColor(new Color(0xaabbff));
		w.moveTo(150, 150);
		w.writeText("Välkommen till snake !");
		w.moveTo(150, 170);
		w.writeText("Ditt mål är att styra ormen så att du äter upp äpplet utan att äta din svans.");
		w.moveTo(150, 190);
		w.writeText("Du styr med W (upp), A (vänster), S (ner) och D (höger)");
		w.moveTo(150, 210);
		w.writeText("Oroa dig inte för att krocka med väggar, men om du krockar med din svans så förlorar du.");
		w.moveTo(150, 230);
		w.writeText("Klicka på den gröna cirkeln för att starta eller den röda för att stänga ner spelet.");
		
		waitForInput(w);
	}
	/**
	 * rita upp en meny efter att spelaren har förlorat
	 * @param w fönstret att rita i
	 * @param score hur många poäng som spelaren fick
	 * @param time hur lång tid spelaren tog på sig
	 */
	public static void gameOver(SimpleWindowMod w, int score, int time){
		//måla upp bakgrunden
		w.setLineColor(new Color(0x006600));
		w.setLineWidth(w.getHeight());
		w.moveTo(0, w.getHeight() / 2);
		w.lineTo(w.getWidth(), w.getHeight() / 2);
		//skriv upp ett meddelande med instruktioner
		if(score > highScore) highScore = score;
		w.setLineColor(Color.WHITE);
		w.moveTo(550, 150);
		w.writeText(score >= highScore ? ("Du slog ditt förra rekord !! ditt nya rekord är : " + highScore+ " poäng.") : 
			("Du slog inte ditt förra rekord :( , för att slå ditt förra rekord så måste du få över "+highScore+" poäng."));
		w.setLineColor(Color.CYAN);
		w.moveTo(250, 150);
		w.writeText("Du fick : "+score+" poäng på "+time+" sekunder, snyggt jobbat!");
		w.setLineColor(Color.RED);
		w.moveTo(150, 190);
		w.writeText("Du bör klicka på en knapp (inte w, a, s eller d) innan du börjar om, annars så kommer ormen inte att stå still när du börjar.");
		w.setLineColor(new Color(0xaabbff));
		w.moveTo(150, 150);
		w.writeText("Du dog :(");
		w.moveTo(150, 170);
		w.writeText("Men oroa dig inte, du kan alltid försöka igen igenom att klicka på den gröna knappen.");
		w.moveTo(150, 210);
		w.writeText("Du styr med W (upp), A (vänster), S (ner) och D (höger).");
		w.moveTo(150, 230);
		w.writeText("Om du inte vill fösöka igen så klickar du på den röda knappen.");
		
		waitForInput(w);
	}	
	
	/**
	 * metoden ritar upp 2 knappar och väntar på att användaren ska klicka på en av knapparna.
	 * @param w fönstret att rita i
	 */
	private static void waitForInput(SimpleWindowMod w){
		//rita upp startknappen
		w.setLineColor(new Color(0x88ff88));
		w.setLineWidth(4);
		int startX = 400, startY = 400, rad = 100;
		w.moveTo(startX, startY);
		for(int i = 0; i < 360; i++){
			w.lineTo((int)(Math.cos(i) * rad + startX), (int)(Math.sin(i) * rad + startY));
			w.moveTo(startX, startY);
		}

		//rita upp avsluta-knappen
		w.setLineColor(new Color(0xff5555));
		w.setLineWidth(4);
		int exitX = 600, exitY = 600; //jag har redan definerat 'rad'
		w.moveTo(exitX, exitY);
		for(int i = 0; i < 360; i++){
			w.lineTo((int)(Math.cos(i) * rad + exitX), (int)(Math.sin(i) * rad + exitY));
			w.moveTo(exitX, exitY);
		}
		
		
		//vänta på att användaren klickar på en av knapparna
		while(true){
			w.waitForMouseClick();
			int mx = w.getMouseX(), my = w.getMouseY();
			
			//om man klickar på startKnappen (pythagoras sats)
			if(Math.sqrt(Math.abs((startX - mx) * (startX - mx) + Math.abs((startY - my) * (startY - my)))) <= rad){
				return;
			}
			//om man klickar på avsluta-knappen (pythagoras sats)
			if(Math.sqrt(Math.abs((exitX - mx) * (exitX - mx) + Math.abs((exitY - my) * (exitY - my)))) <= rad){
				System.exit(0);
			}		
		}
	}
}
