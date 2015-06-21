package edu.skku.sosil3.sky.telepathy;

public class Constants {
	static final long BUILD_NO = 0;
	
	static final String URL_SERVER_HOST = "http://telepathy.hyunha.org";
	
	/* Main SNS functionalities. */
	static final String URI_POST_SIGNUP = "/signup";
	static final String URI_POST_SIGNIN = "/signin";
	static final String URI_POST_POST = "/post";
	static final String URI_POST_COMMENT = "/comment";
	static final String URI_GET_NEWSFEED = "/newsfeed";
	
	/* Update Checking. */
	static final String URI_GET_MIN_VERSION = "/app/android/min-version";
	static final String URI_GET_LATEST_VERSION = "/app/android/latest-version";
}
