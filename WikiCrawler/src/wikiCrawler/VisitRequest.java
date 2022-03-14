package wikiCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VisitRequest {
	
	
	public static List<String> getLinks(String url, String containingUrl) throws IOException {
		Document doc = Jsoup.connect(url).get();
		
		List<String> linkList = new LinkedList<String>();
		
		Elements links = doc.select("a[href]");
		
		for (Element link : links) {
			
			String newurl = link.attr("abs:href");
			if(newurl.contains(containingUrl))
				linkList.add(newurl);
	    }
		
		return linkList;
	}
	

	public static Deque<String> BFS(String url, String containingURL, int depth) throws IOException{
		
		Deque<String> frontier = new LinkedList<String>();
		
		ArrayList<String> reached = new ArrayList<String>();
		
		
		reached.add(url);
		int stop = 0;
		
		while(depth != stop) {
			for(String link : getLinks(url,containingURL)) {
				frontier.add(link);
				
			}
			stop++;
		}
		
		
		return frontier;
	}
	
}
