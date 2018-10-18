package org.superbiz.moviefun.blobstore;

import org.apache.tika.io.TikaInputStream;
import org.superbiz.moviefun.MovieUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class FileStore implements BlobStore {
    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.name);
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();


        byte[] bytes = MovieUtils.convertInputStreamToBytesArray(blob.inputStream);


        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(bytes);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        Blob blob = null;
       try {
           Path coverFilePath = MovieUtils.getExistingCoverPath(name);
           TikaInputStream tikaInputStream = TikaInputStream.get(coverFilePath);
           blob = new Blob(name, tikaInputStream, "jpeg");
       }catch (Exception ex){
           System.out.println(ex.getStackTrace());
       }

      return Optional.ofNullable(blob);

    }

    @Override
    public void deleteAll() {
        // ...
    }
}
