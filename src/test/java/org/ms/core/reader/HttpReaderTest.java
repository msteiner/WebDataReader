package org.ms.core.reader;

import org.junit.Test;

public class HttpReaderTest {
  
  //public static final String URL = "http://yellow.local.ch/de/q/Restaurant.html?page=1";
  public static final String URL = "http://yellow.local.ch/de/print/q?page=3&print=text&what=Restaurant";
  
  @Test
  public void readFullPage() {
    HttpReader reader = new HttpReader();
    
    String content = reader.readAddresses(URL);
    System.out.println(content);
  }
}
