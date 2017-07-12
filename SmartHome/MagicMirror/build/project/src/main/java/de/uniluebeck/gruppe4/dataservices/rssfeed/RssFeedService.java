package de.uniluebeck.gruppe4.dataservices.rssfeed;

import de.uniluebeck.gruppe4.model.RssFeed;
import de.uniluebeck.gruppe4.model.RssFeedMessage;

public class RssFeedService {
	
	private String feedURL;
	
	public RssFeedService(String feedURL){
		this.feedURL = feedURL;
	}
	
	public RssFeedMessage getFeedData(){
		RSSFeedParser parser = new RSSFeedParser(feedURL);
		RssFeed feed = parser.readFeed();
		
		return feed.getMessages().get(0);
	}
}
