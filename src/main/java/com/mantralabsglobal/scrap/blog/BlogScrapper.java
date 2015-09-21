package com.mantralabsglobal.scrap.blog;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mantralabsglobal.scrap.dataobject.BlogComment;
import com.mantralabsglobal.scrap.dataobject.BlogPost;
import com.mantralabsglobal.scrap.repository.BlogPostRepository;

public class BlogScrapper extends com.mantralabsglobal.scrap.Scrapper{

	BlogParser parser;
	Queue<String> summaryQueue;
	private int threshold = 4;
	BlogPostRepository repository;
	
	public BlogScrapper(String entryUrl, BlogParser parser, BlogPostRepository repository) {
		this.parser = parser;
		summaryQueue = new PriorityQueue<>();
		summaryQueue.add(entryUrl);
		this.repository = repository;
	}

	@Override
	public void scrap() throws IOException{
		int counter = 0;
		while(summaryQueue.peek() != null && counter< threshold)
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
			else if(parser.isPostPage(href))
			{
				try
				{
					List<BlogPost> blogPostList = parser.getRepository().findByUrl(href);
					if(blogPostList == null || blogPostList.size()==0){
						scrapBlogPost(href, null, false);
						postFound = true;
					}
					else
					{
						scrapBlogPost(href, blogPostList.get(0), true);
					}
				}
				catch(Exception exp)
				{
					exp.printStackTrace();
				}
				
			}
			else if(parser.isListPage(href) && !summaryQueue.contains(href)){
				summaryQueue.add(href);
			}
			
		}
		return postFound;
	}
	
	public void scrapBlogPost(String href, BlogPost ourCopy, boolean commentsOnly) throws IOException{
		Document document;
		try {
			document = Jsoup.connect(href).get();
		} catch (IOException e) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			document = Jsoup.connect(href).get();
		}
		if(ourCopy == null)
			ourCopy = new BlogPost();
		BlogPost post = parser.parseBlogPost(document, ourCopy);
		List<BlogComment> comments = parser.parseBlogComments(document);
		if(comments !=null && !comments.isEmpty())
		{
			post.setComments(comments);
		}
		post.setUrl(href);
		
		repository.save(post);
	}

/*	@Override
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
					Document document = Jsoup.connect(href).get();
					BlogPost post = parser.parseBlogPost(document);
					List<BlogComment> comments = parser.parseBlogComments(document);
					if(comments !=null && !comments.isEmpty())
					{
						post.setComments(comments);
					}
					post.setUrl(href);
					return post;
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
*/
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}


	
}
