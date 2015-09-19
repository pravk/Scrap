package com.mantralabsglobal.scrap.blog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mantralabsglobal.html.parser.BlogParser;

public class BlogScrapper extends com.mantralabsglobal.scrap.Scrapper{

	BlogParser parser;
	Map<String, Boolean> hrefMap;
	Queue<String> summaryQueue;
	
	public BlogScrapper(String entryUrl, BlogParser parser) {
		this.parser = parser;
		hrefMap = new HashMap<>();
		summaryQueue = new PriorityBlockingQueue<>();
		summaryQueue.add(entryUrl);
	}

	@Override
	public void scrap() throws IOException{
		int counter = 0;
		while(summaryQueue.peek() != null && counter< 10)
		{
			try
			{
				boolean postsFound = scrapInternal(summaryQueue.poll());
			if(postsFound)
				counter = 0;
			else
				counter++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		for(String href: hrefMap.keySet()){
			parser.parseBlogPost(Jsoup.connect(href).get());
			break;
		}
		
	}
	public boolean scrapInternal(String url) throws IOException{
		
		Document document;
		try {
			document = Jsoup.connect(url).get();
		} catch (IOException e) {
			
			//try again
			document = Jsoup.connect(url).get();
		}
		
		Elements hrefElements = document.select("a");
		
		Iterator<Element> eltIterator = hrefElements.iterator();
		
		boolean postFound = false;
		while(eltIterator.hasNext())
		{
			String href =eltIterator.next().attr("href");
			if(StringUtil.isBlank(href))
			{
				continue;
			}
			else if(parser.isPostPage(href) && !hrefMap.containsKey(href))
			{
				hrefMap.put(href, Boolean.FALSE);
				System.out.println(href);
				postFound = true;
			}
			else if(parser.isListPage(href) && !summaryQueue.contains(href)){
				summaryQueue.add(href);
			}
			
		}
		return postFound;
	}
	
}
