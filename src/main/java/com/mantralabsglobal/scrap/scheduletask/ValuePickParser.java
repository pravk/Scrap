package com.mantralabsglobal.scrap.scheduletask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.mantralabsglobal.scrap.blog.BlogParser;
import com.mantralabsglobal.scrap.dataobject.BlogComment;
import com.mantralabsglobal.scrap.dataobject.BlogPost;

@Component
public class ValuePickParser extends BlogParser{
	private final static String BLOG="value-picks-blogspot";
	
	public BlogPost parseBlogPost(Document document,  BlogPost post){
		post.setTitle(document.select("h3.post-title").select("a").text());
		Elements contentElts = document.select("div.post-body");
		removeStyleAttributes(contentElts);
		post.setContent(contentElts.html());
		post.setSummary20(truncateAfteWords(20, document.select("div.post-body").text()));
		post.setSummary50(truncateAfteWords(50, document.select("div.post-body").text()));
		post.setSummary100(truncateAfteWords(100, document.select("div.post-body").text()));
		post.setSummary200(truncateAfteWords(200, document.select("div.post-body").text()));
		post.setSummary300(truncateAfteWords(300, document.select("div.post-body").text()));
		
		post.setAuthor(document.select("span.post-author").select("span.fn").text());
		Elements images = document.select("div.post-body").select("img");
		if(images!= null && images.size()>0)
		{
			post.setImageUrlList(new ArrayList<String>());
			Iterator<Element> elt = images.iterator();
			while(elt.hasNext()){
				post.getImageUrlList().add(elt.next().attr("src"));
			}
		}
		
		post.setBlog(BLOG);
		SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd, yyyy");
		
		try {
			post.setLastModified( format.parse(document.select("h2.date-header").select("span").text()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Parsing complete for " + post.getTitle());
		return post;
		
	}
		
	public boolean isPostPage(String href) {
		return href.matches("http:\\/\\/value-picks.blogspot.in\\/(\\d{4}\\/\\d{2})\\/(\\w+(-\\w+)*).html");
	}

	public boolean isListPage(String href) {
		return href.startsWith("http://value-picks.blogspot.in/search?updated-max=");
	}

	@Override
	public List<BlogComment> parseBlogComments(Document document) {
		List<BlogComment> commentList = new ArrayList<>();
		Elements commentElements = document.select("div.comment-thread").select("li.comment");
		Iterator<Element> eltIterator= commentElements.iterator();
		while(eltIterator.hasNext()){
			Element elt = eltIterator.next();
			BlogComment comment = parseComment(elt);
			commentList.add(comment);
		}
		return commentList;
	}
		
	protected BlogComment parseComment(Element elt){
		BlogComment comment = new BlogComment();
		comment.setAvatarUrl(elt.select("div.avatar-image-container").select("img").attr("src"));
		comment.setProfileUrl(elt.select("cite.user").select("a").attr("href"));
		comment.setUser(elt.select("cite.user").select("a").text());
		comment.setComment(elt.select("p.comment-content").text());
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
		try
		{
		  long time = format.parse(elt.select("div.comment-header").select("span.datetime").select("a").text()).getTime();
		  comment.setCommentDate(time);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}	
		
		if(elt.select("div.comment-replies").size()>0)
		{
			comment.setReplies(new ArrayList<BlogComment>());
			//Process comment replies
			Iterator<Element> replies = elt.select("div.comment-replies").select("li.comment").iterator();
			while(replies.hasNext()){
				Element replyElt = replies.next();
				BlogComment reply = parseComment(replyElt);
				comment.getReplies().add(reply);
			}
		}
		
		return comment;
	}
}