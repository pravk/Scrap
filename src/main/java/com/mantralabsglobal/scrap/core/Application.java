package com.mantralabsglobal.scrap;

import java.io.IOException;

import com.mantralabsglobal.html.parser.ValuePickParser;
import com.mantralabsglobal.scrap.blog.BlogScrapper;

public class Main {

	public static void main(String [] args) throws IOException{
		ValuePickParser parser = new ValuePickParser();
		BlogScrapper scrapper = new BlogScrapper("http://value-picks.blogspot.in/", parser);
		scrapper.scrap();
	}
}
