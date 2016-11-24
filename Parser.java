import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	
	public static void main(String[]args)
	{
		Parser p = new Parser();
		p.findString("http://www.fredrika.se/", "hej");
	}
	
	public Parser() {
	}

	public void findString(String url, String toFind) {
		String html = parseEncodingDifference(loadHTML(url));
		if(html.contains(toFind))
		{
			System.out.println("Str�ngen hittades i f�rsta sidan!\t\t" + url);
			return;
		}
		
		//om man inte hittade str�ngen p� f�rsta sidan s� f�r man forts�tta leta
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
 					System.out.println("found ["+toFind+"] in url ["+s+"]");
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
	 * om man laddar ner ett html doc s� byts encodingen, 	� blir å
	 * 														� blir ä
	 * 														� blir ö Eller Ö
	 * vet inte riktigt vad jag ska g�ra �t saken f�rutom att byta ut allt i koden
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
				result += temp + "\n"; 	// vet inte varf�r dom har gjort s� att man kan addera strings (inte primitiva datatyper),
										// men det kan man tydligen
		} catch (Exception e) {
			System.err.println("ajajaj, ngt gick �t helvete! " + e.getMessage());
			//e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * vet som sagt inte hur jag ska l�sa problemet p� ett annat s�tt.
	 * 	 												
	 * @param in
	 * @return
	 */
	private String parseEncodingDifference(String in)
	{
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < in.length() - 1; i++)
		{
			if(in.charAt(i) == '�' && in.charAt(i + 1) == '�'){result.append('�'); i++;}
			else if(in.charAt(i) == '�' && in.charAt(i + 1) == '�'){result.append('�'); i++;}
			else if(in.charAt(i) == '�' && (in.charAt(i + 1) == '�' || in.charAt(i + 1) == '�')){result.append('�'); i++;}
			else result.append(in.charAt(i));
		}
		return result.toString();
	}
	
	/**
	 * misst�nker att jag kommer att anv�nda denna funktion ett par g�nger p� olika st�llen s� g�r bara en funktion av det.
	 * problemet med list.toArray() �r att den metoden returnerar en array med java.lang.Object och java gillar tydligen inte att
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
	 * 
	 * @param html
	 * @return
	 */
	private String[] findURLs(String html)
	{
		//vet inte hur m�nga jag kommer att hitta s� man kan inte g�ra en array i b�rjan f�r att lagra alla resultat
		List<String> result = new ArrayList<>();
		String[] lines = html.split("<a");
		
		for(int i = 0; i < lines.length; i++)
		{
			StringBuilder temp = new StringBuilder(lines[i]);
			/*ta bort alla mellanslag s� att man inte kan skriva <a ... href = "hejsan.com" > ...
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
					//man kan argumentera f�r att jag b�r kolla efter hela karakt�rsf�ljden men jag orkar inte skriva allt
					c += 6; //hoppa till d�r l�nken b�rjar (skippa " href=" ")
					//l�gg till l�nken
					while(temp.charAt(c) != '"')url.append(temp.charAt(c++));
					//det g�r att ha annat i href, t.ex. mailadresser
					if(!result.contains("http://www"))result.add(url.toString());
					continue;
				}
			}
		}
		return toStringArray(result);
	}


}
