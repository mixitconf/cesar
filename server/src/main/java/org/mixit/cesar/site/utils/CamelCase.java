package org.mixit.cesar.site.utils;

public class CamelCase {


    public static String camelCase(String string) {
        if(string==null){
            return null;
        }
        if(string.length()==1){
            return string.substring(0,0).toUpperCase();
        }
        return string.substring(0,0).toUpperCase() + string.substring(1, string.length());
    }

}
