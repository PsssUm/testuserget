package com.psssum.docs.utils;

public class DecoderHelper {
    public static String decode(String myString){
        String str = myString.split(" ")[0];
        str = str.replace("\\","");
        String[] arr = str.split("u");
        String text = "";
        for(int i = 1; i < arr.length; i++){
            int hexVal = Integer.parseInt(arr[i], 16);
            text += (char)hexVal;
        }
        return text;
    }
}
