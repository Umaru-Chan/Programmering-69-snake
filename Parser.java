import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Alexander
 *
 */
public class Parser {
	
	public static void main(String[]args)
	{
		Parser p = new Parser();
		p.findString("http://www.fredrika.se/", "snö");
	}
	
	public Parser() {
	}

	public void findString(String url, String toFind) {
		String html = parseEncodingDifference(loadHTML(url));
		if(html.contains(toFind))
		{
			System.out.println("Strängen hittades i första sidan!\t\t" + url);
			return;
		}
		
		//om man inte hittade strängen på första sidan så får man fortsätta leta
 		boolean found = false;
 		List<String> p = new ArrayList<>();
 		String[] pages = findURLs(html);
 		while(!found)
 		{
 			for(String s : pages)
 			{
 				String str = parseEncodingDifference(loadHTML(s));
 				if(str.contains(toFind))
 				{
 					System.out.println("found [ "+toFind+" ] in url [ "+s+" ]");
 					return;
 				} 				
 			}
 			p = new ArrayList<>();
 			for(String s : pages)
 			{
 				String[] temp = findURLs(s);
 				for(int i = 0; i < temp.length; i++)p.add(temp[i]);
 			}
 			pages = toStringArray(p);
 		}
	}

	/**
	 * 
	 * PROBLEM:
	 * om man laddar ner ett html doc så byts encodingen, 	å blir Ã¥
	 * 														ä blir Ã¤
	 * 														ö blir Ã¶ Eller Ã–
	 * vet inte riktigt vad jag ska göra åt saken förutom att byta ut allt i koden,
	 * jag är även osäker på om problemet är detsamma mellan olika operativsystem.
	 * 
	 * @param url
	 * @return
	 */
	private String loadHTML(String domain) {
		String result = new String();
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new URL(domain).openConnection().getInputStream()));

			String temp;
			while ((temp = in.readLine()) != null)
				result += temp + "\n"; 	// vet inte varför dom har gjort så att man kan addera strings (inte primitiva datatyper),
										// men det kan man tydligen
		} catch (Exception e) {
			System.err.println("ajajaj, ngt gick åt helvete! " + e.getMessage());
			//e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * vet som sagt inte hur jag ska lösa problemet på ett annat sätt.
	 * 	 												
	 * @param in
	 * @return
	 */
	private String parseEncodingDifference(String in)
	{
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < in.length() - 1; i++)
		{
			if(in.charAt(i) == 'Ã' && in.charAt(i + 1) == '¥'){result.append('å'); i++;}
			else if(in.charAt(i) == 'Ã' && in.charAt(i + 1) == '¤'){result.append('ä'); i++;}
			else if(in.charAt(i) == 'Ã' && (in.charAt(i + 1) == '¶' || in.charAt(i + 1) == '–')){result.append('ö'); i++;}
			else result.append(in.charAt(i));
		}
		return result.toString();
	}
	
	/**
	 * misstänker att jag kommer att använda denna funktion ett par gånger på olika ställen så gör bara en funktion av det.
	 * problemet med list.toArray() är att den metoden returnerar en array med java.lang.Object och java gillar tydligen inte att
	 * casta arrays.
	 * 
	 * @param l
	 * @return
	 */
	private String[] toStringArray(List<String> l)
	{
		String[] result = new String[l.size()];
		for(int i = 0; i < l.size(); i++)result[i] = l.get(i);
		return result;
	}
	
	/**
	 * leta igenom ett html dokumment och hitta alla <a ... href = "url" ...
	 * 
	 * @param html
	 * @return en array med alla urls i html
	 */
	private String[] findURLs(String html)
	{
		//vet inte hur många jag kommer att hitta så man kan inte göra en array i början för att lagra alla resultat
		List<String> result = new ArrayList<>();
		String[] lines = html.split("<a");
		
		for(int i = 0; i < lines.length; i++)
		{
			StringBuilder temp = new StringBuilder(lines[i]);
			/*ta bort alla mellanslag så att man inte kan skriva <a ... href = "hejsan.com" > ...
																		href ="hejsan.com"  > ...
																		href= "hejsan.com"  > ...
																		href="hejsan.com"   > ...
			*/
			for(int c = 0; c < temp.length(); c++)if(temp.charAt(c) ==  ' '){temp.deleteCharAt(c); c--;}
											 else if(temp.charAt(c) == '\t'){temp.deleteCharAt(c); c--;}
											 else if(temp.charAt(c) == '\n'){temp.deleteCharAt(c); c--;}
			//System.out.println(temp.toString());
			
			for(int c = 0; c < temp.length() - "href=\"".length(); c++)
			{
				//hitta href="
				if(temp.charAt(c) == 'h' && temp.charAt(c+1) == 'r' && temp.charAt(c+2) == 'e' && temp.charAt(c+3) == 'f')
				{
					StringBuilder url = new StringBuilder();
					//man kan argumentera för att jag bör kolla efter hela karaktärsföljden men jag orkar inte skriva allt
					c += 6; //hoppa till där länken börjar (skippa " href=" ")
					//lägg till länken
					while(temp.charAt(c) != '"')url.append(temp.charAt(c++));
					//det går att ha annat i href, t.ex. mailadresser
					
					if(contains(url.toString(), "https://"))result.add(url.toString());
					continue;
				}
			}
		}
		return toStringArray(result);
	}
	
	/**
	 * 
	 * problemet med contains metoden i string är att den letar efter ord med mellanslag mellan sig. den funkade inte 
	 * för det endamål som jag använder denhär funktionen till iaf.
	 * 
	 * @param str
	 * @param lookFor
	 * @return true om str inehåller lookFor
	 */
	private boolean contains(String str, String lookFor)
	{
		for(int i = 0; i < str.length() - lookFor.length(); i++)
		{
			for(int j = i; j < i + lookFor.length(); j++)
			{
				if(str.charAt(i + j) != lookFor.charAt(j))break;

				return true;
			}
		}
		return false;
	}
}
