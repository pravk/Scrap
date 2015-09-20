package com.mantralabsglobal.scrap.core;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mantralabsglobal.html.parser.ValuePickParser;
import com.mantralabsglobal.scrap.blog.BlogScrapper;

@SpringBootApplication
@ComponentScan("com.mantralabsglobal.scrap")
@EnableAutoConfiguration
public class Application {

	public static void main(String [] args) throws IOException{
		
		SpringApplication.run(Application.class, args);
		
		//ValuePickParser parser = new ValuePickParser();
		//BlogScrapper scrapper = new BlogScrapper("http://value-picks.blogspot.in/", parser);
		//scrapper.scrap();
	}
}
