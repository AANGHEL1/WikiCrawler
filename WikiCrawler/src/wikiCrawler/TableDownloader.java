package wikiCrawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableDownloader {
	
	
	
	public static void printTable(String page) throws IOException {
		Document doc = Jsoup.connect(page).get();
		
		Elements links = doc.getElementById("content").getElementsByTag("caption");
		Elements title = doc.getElementById("content").getElementsByTag("h1");
		String filename = title.text()+"_"+links.text().replace(" ", "")+".txt";
		//creating the file
		//FileWriter file = new FileWriter(filename);
		//file.write("\"URL\",\""+page+"\"\n");
		
		
		String tableCaption = "";
		List<String> headers = new ArrayList<String>();
		List<String> rows = new ArrayList<String>();
		
		
		
		Elements pageTables = doc.select("table");
		for(Element table : pageTables) {
			int isCaption = 0;
			Elements tableChildren = table.children();
			for(Element tableChild : tableChildren) {
				if(tableChild.is("caption")) {
					tableCaption = tableChild.text();
					//file.write("\"Table\",\"" + tableChild.text()+"\"\n");
					isCaption = 1;
					//file.write("\n");
				}
				if(isCaption ==1) {
					if(tableChild.is("tbody")) {
						Elements tableBodyChildren = tableChild.children();
						for(Element tableBodyChild : tableBodyChildren) {
							Elements subItems = tableBodyChild.children();
							for(Element sub : subItems) {
								if(sub.tagName().equals("th")) {
									headers.add(sub.text());
								}
								if(sub.tagName().equals("td")) {
									rows.add(sub.text());
									
								}
							}
						}
					}
				}
			}
			
		}
		
		if(headers.size()>0 && tableCaption.trim() != "") {
		
		FileWriter file = new FileWriter(filename);
		
		file.write("\"URL\",\""+page+"\"\n");
		
		
		file.write("\"Table\",\"" + tableCaption+"\"\n");
		file.write("\n");
		
		
		file.write("\"Headings\",");
		
		for( int i = 0; i < headers.size(); i++) {
			if(i == headers.size()-1)
				file.write("\""+headers.get(i)+"\"\n");
			else
				file.write("\""+headers.get(i)+"\",");
		}
		int count = 0;
		file.write("\"\",");
		for (int i = 0; i < rows.size(); i++) {
			if(i == rows.size()-1) {
				file.write("\""+rows.get(i)+"\"\n");
			}
			else if(count == headers.size()-1) {
				file.write("\""+rows.get(i)+"\"\n");
				file.write("\"\",");
				count = 0;
			}
			else {
				file.write("\""+rows.get(i)+"\",");
				count++;
			}
		}
		
		
		file.write("\n");
		
		file.close();
		System.out.println(filename + " sucessfully created");
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		
		Deque<String> pages = VisitRequest.BFS(args[0], args[1], Integer.parseInt(args[2]));
		
		for(String page:pages) {
				printTable(page);
			}
		}
		
	}
//}
