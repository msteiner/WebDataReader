package org.ms.core.reader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public List<String> getURLs(String path) {

        List<String> urls = new ArrayList<String>();
        StringBuffer url = null;

        try {
            RandomAccessFile aFile = new RandomAccessFile(path, "r");
            FileChannel inChannel = aFile.getChannel();
            long fileSize = inChannel.size();
            ByteBuffer buffer = ByteBuffer.allocate((int)fileSize);
            inChannel.read(buffer);
            //buffer.rewind();
            buffer.flip();
            url = new StringBuffer();
            for (int i = 0; i < fileSize; i++) {
                char ch = (char)buffer.get();
                url.append(ch);
                if (ch == '\n') {
                    urls.add(url.toString());
                    url = new StringBuffer();
                }
            }
          urls.add(url.toString());
            inChannel.close();
            aFile.close();
        } catch (IOException exc) {
            System.out.println(exc);
            System.exit(1);
        }
        return urls;
    }
}
