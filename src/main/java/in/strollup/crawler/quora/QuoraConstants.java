package in.strollup.crawler.quora;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class QuoraConstants {

	private static String google = "https://www.google.com/search?q=";
	public static String charset = "UTF-8";
	private static String quora = "\"quora\" ";
	private static int numResultToScrape = 10;
	public static int numReLatedQuestion = 5;

	public static Map<Integer, String> cityIntegerMap = new HashMap<>();
	private static String delhi = " in \"delhi\"";
	private static String bangalore = " in \"bangalore\"";
	public static List<String> searches = Arrays.asList(
			"NightClubs", "places to spent quality time", "hangout places", "couple places", 
			"best place to drink", "Go karting", "romantic places", "quiet places", "fun places",
			"best app to explore", "adventure places", "Paintball", "things to do");

	static {
		cityIntegerMap.put(1, delhi);
		cityIntegerMap.put(2, bangalore);
	}

	public static String getUrl(String url, int cityId) {
		String city = cityIntegerMap.get(cityId);
		String search = google + StringUtils.replace(quora + url + city, " ", "+") + "&num="
				+ String.valueOf(numResultToScrape);
		return search;
	}

}
