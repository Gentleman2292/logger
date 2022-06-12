package me.esot.logger.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtils {
    private final static String CLIENT_ID = "c44426b4013e677";

    public static String upload(File file) {
        String url = "https://api.imgur.com/3/image";
        HttpURLConnection conn = getHttpConnection(url);
        writeToConnection(conn, "image=" + toBase64(file));
        return getResponse(conn);
    }

    public static String getLink(String jsonResponse)
    {
        Pattern pattern = Pattern.compile("link\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonResponse);
        matcher.find();
        return matcher.group().replace("link\":\"", "").replace("\"", "").replace("\\/", "/");
    }

    private static String toBase64(File file) {
        try {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(b);
            fs.close();
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        }
        catch (IOException e) { throw new WebException(StatusCode.UNKNOWN_ERROR, e); }
    }

    private static HttpURLConnection getHttpConnection(String url)
    {
        HttpURLConnection conn;
        try
        {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        }
        catch (UnknownHostException e)
        {
            throw new WebException(StatusCode.UNKNOWN_HOST, e);
        }
        catch (IOException e)
        {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    private static void writeToConnection(HttpURLConnection conn, String message)
    {
        OutputStreamWriter writer;
        try
        {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    private static String getResponse(HttpURLConnection conn)
    {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try
        {
            if (conn.getResponseCode() != StatusCode.SUCCESS.getHttpCode())
            {
                throw new WebException(conn.getResponseCode());
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                str.append(line);
            }
            reader.close();
        }
        catch (IOException e)
        {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
        if (str.toString().equals(""))
        {
            throw new WebException(StatusCode.UNKNOWN_ERROR);
        }
        return str.toString();
    }

}
