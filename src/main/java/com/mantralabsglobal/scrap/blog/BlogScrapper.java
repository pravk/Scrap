package com.mantralabsglobal.scrap.blog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mantralabsglobal.scrap.dataobject.BlogPost;

public class BlogScrapper extends com.mantralabsglobal.scrap.Scrapper implements Iterable<BlogPost>{

	BlogParser parser;
	List<String> hrefList;
	Queue<String> summaryQueue;
	
	public BlogScrapper(String entryUrl, BlogParser parser) {
		this.parser = parser;
		hrefList = new ArrayList<>();
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
			else if(parser.isPostPage(href) && !hrefList.contains(href))
			{
				hrefList.add(href);
				System.out.println(href);
				postFound = true;
			}
			else if(parser.isListPage(href) && !summaryQueue.contains(href)){
				summaryQueue.add(href);
			}
			
		}
		return postFound;
	}

	@Override
	public Iterator<BlogPost> iterator() {
		final Queue<String> hrefQueue = new PriorityQueue<>();
		hrefQueue.addAll(hrefList);
		
		return new Iterator<BlogPost>() {

			@Override
			public boolean hasNext() {
				return hrefQueue.peek() != null;
			}

			@Override
			public BlogPost next() {
				String href = hrefQueue.poll();
				try {
					return parser.parseBlogPost(Jsoup.connect(href).get());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void remove() {
				//ignore
			}
		};
	}


	
}
