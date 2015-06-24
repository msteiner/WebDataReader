package org.ms.core.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HttpReader {

    public static final String USER_AGENT               = "Google Chrome 39.0.2171.71";
    public static final String MARKER_BLOCK             = "<h3 class='marker index'>";
    public static final String MARKER_NAME_START        = "<h4>";
    public static final String MARKER_NAME_END          = "</h4>";
    public static final String MARKER_CATEGORY_START    = "<div class='edge'></div>";
    public static final String MARKER_CATEGORY_END      = "</div>";
    public static final String MARKER_ADDRESS_START     = "<p class='organization'></p><p>";
    public static final String MARKER_ADDRESS_END       = "</p>";

    public static final String MARKER_PHONE_LABEL_START = "<label>";
    public static final String MARKER_PHONE_LABEL_END   = "</label>";
    public static final String MARKER_PHONE_1_START     = "<span class='value'><span class='star'>*</span>";
    public static final String MARKER_PHONE_1_END       = "</span>";
    public static final String MARKER_PHONE_2_START     = "<span class='value'>";
    public static final String MARKER_PHONE_2_END       = "</span>";

    public static final String MARKER_URL_START         = "<span class='url'><a href=";
    public static final String MARKER_URL_END           = " class=";

    public static final String SEPERATOR                = ";";


    public String readAddresses(String url) {

        String content = readURLContent(url);
        parse(content);

        return "";
    }

    private void parse(String content) {

        int startIndex = 0;
        int endIndex = 0;
        String entry = null;
        List<String> list = new ArrayList<String>();

        // parse entry blocks
        while (true) {
            startIndex = content.indexOf(MARKER_BLOCK, startIndex);
            endIndex = content.indexOf(MARKER_BLOCK, startIndex + MARKER_BLOCK.length());
            if (endIndex == -1) {
                break;
            }
            entry = content.substring(startIndex, endIndex).trim();
            formatText(entry);
            list.add(parseEntry(entry));
            startIndex = endIndex;
        }
        printList(list);
    }

    private String parseEntry(String entry) {
        StringBuffer buffer = new StringBuffer();
        // Parse attributes
        String name = parseAttribute(entry, MARKER_NAME_START, MARKER_NAME_END);
        String category = parseAttribute(entry, MARKER_CATEGORY_START, MARKER_CATEGORY_END);
        String address = parseAttribute(entry, MARKER_ADDRESS_START, MARKER_ADDRESS_END);
        String phoneLabel = parseAttribute(entry, MARKER_PHONE_LABEL_START, MARKER_PHONE_LABEL_END);
        String phone = parsePhone(entry);
        String url = parseAttribute(entry, MARKER_URL_START, MARKER_URL_END);
        url = url.replaceAll("\"", "");

        buffer.append(name);
        buffer.append(SEPERATOR);
        buffer.append(category);
        buffer.append(SEPERATOR);
        buffer.append(address);
        buffer.append(SEPERATOR);
        buffer.append(phoneLabel);
        buffer.append(SEPERATOR);
        buffer.append(phone);
        buffer.append(SEPERATOR);
        buffer.append(url);
        buffer.append(SEPERATOR);

        return buffer.toString();
    }

    private String parsePhone(String entry) {
        String phone = "Cannot parse phone.";
        if (isNumeric(parseAttribute(entry, MARKER_PHONE_1_START, MARKER_PHONE_1_END))) {
            return parseAttribute(entry, MARKER_PHONE_1_START, MARKER_PHONE_1_END);
        } else if (isNumeric(parseAttribute(entry, MARKER_PHONE_2_START, MARKER_PHONE_2_END))) {
            return parseAttribute(entry, MARKER_PHONE_2_START, MARKER_PHONE_2_END);
        }
        
        return phone;
    }

    private boolean isNumeric(String value) {
        value.replaceAll(" ", "");
        try {
            NumberFormat.getInstance().parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private String parseAttribute(String entry, String MARKER_START, String MARKER_END) {
        // Parse name
        int pos1, pos2;
        pos1 = entry.indexOf(MARKER_START) + MARKER_START.length();
        pos2 = entry.indexOf(MARKER_END, pos1 - 1);
        String attribute = entry.substring(pos1, pos2);
        return attribute;
    }

    public String readURLContent(String url) {

        URL obj = null;
        HttpURLConnection con = null;
        String inputLine = null;
        StringBuffer response = new StringBuffer();
        BufferedReader in = null;
        int responseCode = 0;

        obj = createURL(url);
        con = openConnection(obj);
        setRequestMethod(con, RequestMethod.GET);
        con.setRequestProperty("User-Agent", USER_AGENT);
        try {
            responseCode = con.getResponseCode();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //print result
        //System.out.println(response.toString());

        return response.toString();
    }

    private URL createURL(String url) {
        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void setRequestMethod(HttpURLConnection con, RequestMethod requestMethod) {
        // optional default is GET
        try {
            con.setRequestMethod(requestMethod.getName());
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection openConnection(URL obj) {

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void formatText(String str) {
        str.replaceAll("\\n", "");
        str.replaceAll("\\s", "");
        str.replaceAll("\n", "");
        str.replaceAll("\r", "");
        str.replaceAll("\\r", "");

    }

    private void printList(List<String> list) {
        System.out.println("================================================================================");
        for (String row : list) {
            System.out.println(row);
        }
        System.out.println("================================================================================");
    }

    public enum RequestMethod {
        GET("GET"),
        POST("POST");

        private String name = null;

        RequestMethod(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
