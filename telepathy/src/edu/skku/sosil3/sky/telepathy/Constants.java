package edu.skku.sosil3.sky.telepathy;

public class Constants {
	public static final long BUILD_NO = 0;
	
	public static final String URL_SERVER_HOST = "http://telepathy.hyunha.org";
	
	/* Main SNS functionalities. */
	public static final String URI_POST_SIGNUP = "/signup";
	public static final String URI_POST_SIGNIN = "/signin";
	public static final String URI_POST_POST = "/post";
	public static final String URI_POST_COMMENT = "/comment";
	public static final String URI_GET_NEWSFEED = "/newsfeed";
	
	/* Update Checking. */
	public static final String URI_GET_MIN_VERSION = "/app/android/min-version";
	public static final String URI_GET_LATEST_VERSION = "/app/android/latest-version";
}