package com.mantralabsglobal.scrap.blog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mantralabsglobal.html.parser.BlogParser;
import com.mantralabsglobal.scrap.HyperlinkValidator;

public class BlogScrapper extends com.mantralabsglobal.scrap.Scrapper{

	BlogParser parser;
	HyperlinkValidator validator;
	Map<String, Boolean> hrefMap;
	
	public BlogScrapper( BlogParser parser, HyperlinkValidator validator) {
		this.parser = parser;
		this.validator = validator;
		hrefMap = new HashMap<>();
	}

	@Override
	public void scrap(String url) throws IOException{
		Document document = Jsoup.connect(url).get();
		
		Elements hrefElements = document.select("a");
		
		Iterator<Element> eltIterator = hrefElements.iterator();
		
		while(eltIterator.hasNext())
		{
			String href =eltIterator.next().attr("href");
			if(StringUtil.isBlank(href))
			{
				continue;
			}
			else if(validator.isValidLink(href) && !hrefMap.containsKey(href))
			{
				hrefMap.put(href, Boolean.FALSE);
				System.out.println(href);
			}
			
		}
	}
	
}
