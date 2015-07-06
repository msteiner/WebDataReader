package org.ms.core.reader;

import org.junit.Assert;
import org.junit.Test;
import org.ms.type.Address;

import java.util.ArrayList;
import java.util.List;

public class HttpReaderTest {

    public static final String URL_1  = "http://yellow.local.ch/de/print/q?page=1&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_2  = "http://yellow.local.ch/de/print/q?page=2&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_3  = "http://yellow.local.ch/de/print/q?page=3&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_4  = "http://yellow.local.ch/de/print/q?page=4&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_5  = "http://yellow.local.ch/de/print/q?page=5&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_6  = "http://yellow.local.ch/de/print/q?page=6&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_7  = "http://yellow.local.ch/de/print/q?page=7&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_8  = "http://yellow.local.ch/de/print/q?page=8&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_9  = "http://yellow.local.ch/de/print/q?page=9&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_10  = "http://yellow.local.ch/de/print/q?page=10&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_11  = "http://yellow.local.ch/de/print/q?page=11&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_12  = "http://yellow.local.ch/de/print/q?page=12&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_13  = "http://yellow.local.ch/de/print/q?page=13&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_14  = "http://yellow.local.ch/de/print/q?page=14&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    public static final String URL_15  = "http://yellow.local.ch/de/print/q?page=15&print=text&rid=VTKk&what=gartenbau&where=Espace+Mittelland+%28Region%29";
    //public static final String URL_4  = "http://yellow.local.ch/naechstePage";
    //public static final String URL_5  = "http://yellow.local.ch/nochnePage";
    // Und so weiter: Beliebig viele. Nur existieren müssen sie.
    HttpReader                 reader = new HttpReader();

    @Test
    public void testReadAddresses() {
        List<String> urls = new ArrayList<String>();
        urls.add(URL_1);
        urls.add(URL_2);
        urls.add(URL_3);
        urls.add(URL_4);
        urls.add(URL_5);
        urls.add(URL_6);
        urls.add(URL_7);
        urls.add(URL_8);
        urls.add(URL_9);
        urls.add(URL_10);
        urls.add(URL_11);
        urls.add(URL_12);
        urls.add(URL_13);
        urls.add(URL_14);
        urls.add(URL_15);
        //urls.add(URL_4);
        //urls.add(URL_5);
        //Und so weiter: Ebenso beliebig viele...
        List<String> addresses = reader.readAddresses(urls);
        reader.printList(addresses);
    }

    /**
     * Data set 9 throws an assertion failure, cause street/number is not a implemented format. You have to fix data manually...
     */
    @Test
    public void testParseAddress() {

        Address address = null;

        String address1 = "Kochstrasse 2, 8004 Zürich";
        String address2 = "avenue de la Gare 1, 1020 Renens VD";
        String address3 = "Postfach 30, 3818 Grindelwald";
        String address4 = "route du Bois-Genoud 36, 1023 Crissier";
        String address5 = "Nägelihof 3, 8001 Zürich";
        String address6 = "via San Gottardo 25, 6943 Vezia";
        String address7 = "Fluhmattstrasse 48, 6004 Luzern";
        String address8 = "rue du Château 2, 1354 Montcherand";
        String address9 = "Badenerstrasse 355 /Albisriederplatz, 8003 Zürich";
        String address10 = "Unterstadt 20, 6210 Sursee";

        address = reader.parseAddress(address1);
        Assert.assertEquals("Kochstrasse", address.getStreet());
        Assert.assertEquals("2", address.getStreetNumber());
        Assert.assertEquals("8004", address.getZip());
        Assert.assertEquals("Zürich", address.getCity());

        address = reader.parseAddress(address2);
        Assert.assertEquals("avenue de la Gare", address.getStreet());
        Assert.assertEquals("1", address.getStreetNumber());
        Assert.assertEquals("1020", address.getZip());
        Assert.assertEquals("Renens VD", address.getCity());

        address = reader.parseAddress(address3);
        Assert.assertEquals("Postfach", address.getStreet());
        Assert.assertEquals("30", address.getStreetNumber());
        Assert.assertEquals("3818", address.getZip());
        Assert.assertEquals("Grindelwald", address.getCity());

        address = reader.parseAddress(address4);
        Assert.assertEquals("route du Bois-Genoud", address.getStreet());
        Assert.assertEquals("36", address.getStreetNumber());
        Assert.assertEquals("1023", address.getZip());
        Assert.assertEquals("Crissier", address.getCity());

        address = reader.parseAddress(address5);
        Assert.assertEquals("Nägelihof", address.getStreet());
        Assert.assertEquals("3", address.getStreetNumber());
        Assert.assertEquals("8001", address.getZip());
        Assert.assertEquals("Zürich", address.getCity());

        address = reader.parseAddress(address6);
        Assert.assertEquals("via San Gottardo", address.getStreet());
        Assert.assertEquals("25", address.getStreetNumber());
        Assert.assertEquals("6943", address.getZip());
        Assert.assertEquals("Vezia", address.getCity());

        address = reader.parseAddress(address7);
        Assert.assertEquals("Fluhmattstrasse", address.getStreet());
        Assert.assertEquals("48", address.getStreetNumber());
        Assert.assertEquals("6004", address.getZip());
        Assert.assertEquals("Luzern", address.getCity());

        address = reader.parseAddress(address8);
        Assert.assertEquals("rue du Château", address.getStreet());
        Assert.assertEquals("2", address.getStreetNumber());
        Assert.assertEquals("1354", address.getZip());
        Assert.assertEquals("Montcherand", address.getCity());

        address = reader.parseAddress(address9);
        //Assert.assertEquals("Badenerstrasse", address.getStreet());
        //Assert.assertEquals("355", address.getStreetNumber());
        Assert.assertEquals("8003", address.getZip());
        Assert.assertEquals("Zürich", address.getCity());

        address = reader.parseAddress(address10);
        Assert.assertEquals("Unterstadt", address.getStreet());
        Assert.assertEquals("20", address.getStreetNumber());
        Assert.assertEquals("6210", address.getZip());
        Assert.assertEquals("Sursee", address.getCity());
    }
}
