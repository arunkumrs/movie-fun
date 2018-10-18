package org.superbiz.moviefun;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.ClassLoader.getSystemResource;

public class MovieUtils {

    public static byte[] convertInputStreamToBytesArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;

        while ((len = inputStream.read(bytes)) != -1) {
            byteOutputStream.write(bytes, 0, len);
        }
        return byteOutputStream.toByteArray();
    }

    public static Path getExistingCoverPath(String name) throws URISyntaxException {
        File coverFile = new File(name);
        Path coverFilePath;
        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {
            coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
        }

        return coverFilePath;
    }
}