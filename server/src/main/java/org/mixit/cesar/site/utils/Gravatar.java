package org.mixit.cesar.site.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * Help to call Gravatar
 */
public class Gravatar {

    private final static String GRAVATAR_URL = "http://www.gravatar.com/avatar/";

    /**
     * Downloads the gravatar for the given URL using Java {@link java.net.URL} and
     * returns a byte array containing the gravatar jpg, returns null if no
     * gravatar was found.
     */
    public static boolean imageExist(String email) {
        Objects.requireNonNull(email, "email required");
        URL url;
        try {
            String emailHash = HashUtil.md5Hex(email);
            url = new URL(GRAVATAR_URL + emailHash + ".jpg?d=404");
        }
        catch (MalformedURLException e) {
            return false;
        }

        try (InputStream stream = url.openStream()) {
            return stream.available()>0;
        }
        catch (IOException e) {
            return false;
        }
    }

}
