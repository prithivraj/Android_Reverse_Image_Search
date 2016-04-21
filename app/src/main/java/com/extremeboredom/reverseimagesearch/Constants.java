package com.extremeboredom.reverseimagesearch;

/**
 * Created by AKiniyalocts on 2/23/15.
 */
public class Constants {
    /*
      Logging flag
     */
    public static final boolean LOGGING = false;

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    public static final String MY_IMGUR_CLIENT_ID = "068ba06ac8349e6";
    public static final String MY_IMGUR_CLIENT_SECRET = "8ea18f4a3f8ec12722034483b679e16ae3be5792";

    /*
      Redirect URL for android.
     */
    public static final String MY_IMGUR_REDIRECT_URL = "http://google.com";

    /*
      Client Auth
     */
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }

}
