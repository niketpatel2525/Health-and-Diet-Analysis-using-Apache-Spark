package com.pkg.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterDataMining {
	private static final String OAUTH_CONSUMER_KEY = "lpIaruS2IccQSGj69DzlF85sS";
	private static final String OAUTH_CONSUMER_KEY_SECRET = "n2s5fYN0BWlyP81wZuPeH0Jt6eynm5ekKDlQ9TBWYwaXZR0BMH";
	private static final String OAUTH_ACCESS_TOKEN = "2443450466-NKIbR2J78Nhbo6SCAfb5AACsrVBgQseypcIdY8b";
	private static final String OAUTH_ACcESS_TOKEN_SECRET = "EOfSsgxYfYu5MCnV8Vo4DLnMhJGljlhDZOGvGN24IfdzT";

	private static BufferedWriter bw = null;
	private static final String FILENAME = "twitter.json";
	private static String[] keywords = { "food", "health", "gym", "workout", "fit", "lifestyle", "diet", "diet plan",
			"dietPlan", "pizza", "sandwich", "pasta", "salad", "vegetarian", "vegan", "non vegetarian", "ill", "sick",
			"tierd", "campaign", "awareness campaign", "ngo", "health", "society", "mankind", "disease", "flu", "fever",
			"weight loss", "fat", "nutrition", "fitness", "calorie", "fruit", "yoga", "exercise", "nonveg", "non veg",
			"foodlover", "foodholic", "Chicken", "Mutton", "beef", "fish", "salads", "juices", "street", "streetfood",
			"street food", "zumba", "Indian food", "Italian food", "Mexican food", "Chinese food", "Thai food",
			"American food", "Japanese food", "aloo chat", "aloochat", "momos", "samosa", "vadapav", "vada pav", "dosa",
			"pani puri", "panipuri", "bhelpuri", "bhel puri", "gymdiet", "gym diet", "protein" };

	static StatusListener statusListener = new StatusListener() {
		long count = 0;

		@Override
		public void onException(Exception arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTrackLimitationNotice(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatus(Status status) {
			// TODO Auto-generated method stub
			try {
				if (status.getGeoLocation() != null) {
					String tweetData = TwitterObjectFactory.getRawJSON(status);
					bw.append(tweetData + "\n");
					System.out.println("Tweet Count:" + count++);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onStallWarning(StallWarning arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrubGeo(long arg0, long arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDeletionNotice(StatusDeletionNotice arg0) {
			// TODO Auto-generated method stub

		}
	};

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// setting configuration with twitter api using OAUTH key
		ConfigurationBuilder cb = getConfigurationWithTwitter();

		// Create File and prepare it for writing data.
		bw = getWriterInstance();

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		FilterQuery fq = new FilterQuery();

		fq.track(keywords);
		twitterStream.addListener(statusListener);
		twitterStream.filter(fq);
	}

	private static BufferedWriter getWriterInstance() {
		// TODO Auto-generated method stub

		File f = new File(FILENAME);
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		BufferedWriter b = null;
		try {
			b = new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	private static ConfigurationBuilder getConfigurationWithTwitter() {
		// TODO Auto-generated method stub
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setJSONStoreEnabled(true);
		cb.setOAuthConsumerKey(OAUTH_CONSUMER_KEY);
		cb.setOAuthConsumerSecret(OAUTH_CONSUMER_KEY_SECRET);
		cb.setOAuthAccessToken(OAUTH_ACCESS_TOKEN);
		cb.setOAuthAccessTokenSecret(OAUTH_ACcESS_TOKEN_SECRET);
		return cb;
	}

}
