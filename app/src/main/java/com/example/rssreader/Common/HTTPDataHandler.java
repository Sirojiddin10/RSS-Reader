package com.example.rssreader.Common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPDataHandler {
    static String STREAM = "";
    public HTTPDataHandler(){}
    public String GetHTTPDataHandler(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urLConnection = (HttpURLConnection) url.openConnection();
            if(urLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = new BufferedInputStream(urLConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = r.readLine()) != null){
                    sb.append(line);
                    STREAM = sb.toString();
                    urLConnection.disconnect();
                }
            }
        } catch (Exception ex){
            return null;
        }
        return STREAM;
    }
}
