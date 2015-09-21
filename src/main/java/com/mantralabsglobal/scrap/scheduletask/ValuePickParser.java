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

import com.mantralabsglobal.scrap.HtmlUtil;
import com.mantralabsglobal.scrap.blog.BlogParser;
import com.mantralabsglobal.scrap.dataobject.BlogComment;
import com.mantralabsglobal.scrap.dataobject.BlogPost;

@Component
public class ValuePickParser extends BlogParser{
	private final static String BLOG="value-picks-blogspot";
	
	public BlogPost parseBlogPost(Document document,  BlogPost post){
		post.setTitle(document.select("h3.post-title").select("a").text());
		post.setContent(document.select("div.post-body").html());
		post.setSummary(HtmlUtil.abbreviateHtmlString(post.getContent(), 300, true));
		post.setAuthor(document.select("span.post-author").select("span").text());
		post.setBlog(BLOG);
		SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd, yyyy");
		
		try {
			post.setLastModified( format.parse(document.select("h2.date-header").select("span").text()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
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