package org.ms.core.reader;

import org.ms.type.Address;

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
    public static final String MARKER_BLOCK_EOF         = "<script src=";
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

    public static final String MSG_ERROR_INV_ADDRESS_1  = "NOT_CLEARLY_DEFINED_ADDRESS: ";
    public static final String MSG_ERROR_INV_ZIPCITY    = "NOT_CLEARLY_DEFINED_ZIP_CITY";


    public List<String> readAddresses(List<String> urls) {
        List<String> entries = new ArrayList<String>();
        for (String url : urls) {
            entries.addAll(readAddresses(url));
        }

        return entries;
    }

    public List<String> readAddresses(String url) {

        String content = readURLContent(url);
        return parse(content);
    }

    private List<String> parse(String content) {

        int startIndex = 0;
        int endIndex = 0;
        boolean hasMoreElements = true;
        String entry = null;
        List<String> list = new ArrayList<String>();

        // parse entry blocks
        while (hasMoreElements) {
            startIndex = content.indexOf(MARKER_BLOCK, startIndex);
            endIndex = content.indexOf(MARKER_BLOCK, startIndex + MARKER_BLOCK.length());
            if (endIndex == -1) {
                //break;
                endIndex = content.indexOf(MARKER_BLOCK_EOF, startIndex + MARKER_BLOCK.length());
                hasMoreElements = false;
            }
            entry = content.substring(startIndex, endIndex).trim();
            formatText(entry);
            list.add(parseEntry(entry));
            startIndex = endIndex;
        }
        //printList(list);
        return list;
    }

    private String parseEntry(String entry) {
        //StringBuffer buffer = new StringBuffer();

        List<String> list = initializeList(39);
        // Parse attributes
        String name = parseAttribute(entry, MARKER_NAME_START, MARKER_NAME_END);
        String addressValue = parseAttribute(entry, MARKER_ADDRESS_START, MARKER_ADDRESS_END);
        Address address = parseAddress(addressValue);
        String phone = parsePhone(entry);
        String url = parseAttribute(entry, MARKER_URL_START, MARKER_URL_END);
        url = url.replaceAll("\"", "");

        setEntry(list, name, 0, true);
        setEntry(list, address.getStreetNumber(), 9, true);
        setEntry(list, address.getStreet(), 10, true);
        setEntry(list, address.getCity(), 11, true);
        setEntry(list, phone, 16, true);
        setEntry(list, address.getZip(), 29, true);
        setEntry(list, url, 38, true);

        StringBuffer buffer = new StringBuffer();
        for (String string : list) {
            buffer.append(string);
        }

        return buffer.toString();
    }

    private List<String> initializeList(int size) {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < size; i++) {
            list.add("");
            if (i == size - 1) {

                setEntry(list, "", i, false);
            } else {
                setEntry(list, "", i, true);
            }
        }
        return list;
    }

    private void setEntry(List<String> list, String value, int pos, boolean appendLimiter) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"");
        buffer.append(value);
        buffer.append("\"");
        if (appendLimiter) {
            buffer.append(";");
        }
        list.set(pos, buffer.toString());
    }

    private String parsePhone(String entry) {
        String phone = "Cannot parse phone.";
        if (isNumeric(parseAttribute(entry, MARKER_PHONE_1_START, MARKER_PHONE_1_END))) {
            phone = parseAttribute(entry, MARKER_PHONE_1_START, MARKER_PHONE_1_END);
        } else if (isNumeric(parseAttribute(entry, MARKER_PHONE_2_START, MARKER_PHONE_2_END))) {
            phone = parseAttribute(entry, MARKER_PHONE_2_START, MARKER_PHONE_2_END);
        }

        phone = phone.replaceFirst("0", "+41 ");

        return phone;
    }


    public Address parseAddress(String value) {

        Address address = new Address();
        int pos1 = 0;
        int pos2 = 0;
        int pos3 = value.lastIndexOf(",");
        int pos4 = 0;
        String street = "";
        String number = "";
        String zip = "";
        String city = "";

        if (pos3 == -1) {
            address.setStreet(MSG_ERROR_INV_ADDRESS_1 + value);
            address.setStreetNumber(MSG_ERROR_INV_ADDRESS_1 + value);
            address.setZip(MSG_ERROR_INV_ADDRESS_1 + value);
            address.setCity(MSG_ERROR_INV_ADDRESS_1 + value);
        } else {
            // Parse street and number
            String streetNumber = value.substring(pos1, pos3);
            pos2 = streetNumber.lastIndexOf(" ");
            if (pos2 > pos1) {
                street = value.substring(pos1, pos2);
                number = value.substring(pos2, pos3);
                number = number.replaceAll(" ", "");
            } else {
                street = streetNumber;
            }

            // parse zip and city
            try {
                String zipCity = value.substring(pos3, value.length());
                pos4 = zipCity.indexOf(" ", 2);
                zip = zipCity.substring(1, pos4);
                zip = zip.replaceAll(" ", "");
                city = zipCity.substring(pos4 + 1, zipCity.length());
            } catch (IndexOutOfBoundsException e) {
                zip = MSG_ERROR_INV_ZIPCITY;
                city = MSG_ERROR_INV_ZIPCITY;
            }

            address.setStreet(street);
            address.setStreetNumber(number);
            address.setZip(zip);
            address.setCity(city);
        }

        return address;
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
        //        int responseCode = 0;

        obj = createURL(url);
        con = openConnection(obj);
        setRequestMethod(con, RequestMethod.GET);
        con.setRequestProperty("User-Agent", USER_AGENT);
        try {
            //            responseCode = con.getResponseCode();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void printList(List<String> list) {
        System.out.println("=== These are your data records, Tommy! ========================================");
        System.out.println("  1. Lege irgendwo auf deiner Disk oder Stick oder sonstwo ein leeres File mit ");
        System.out.println("     dem file type *.csv an.");
        System.out.println("  2. Kopiere die nachfolgenden Daten da rein.");
        System.out.println("  3. Öffne das Ding dann mit Excel zum Sortieren/Korrigieren/Erweitern.");
        System.out.println("--------------------------------------------------------------------------------");
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
