package org.superbiz.moviefun.albums;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.MovieUtils;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Controller
@RequestMapping("/albums")
public class AlbumsController {

    private final AlbumsBean albumsBean;

    @Autowired
    private BlobStore blobStore;

    public AlbumsController(AlbumsBean albumsBean) {
        this.albumsBean = albumsBean;
    }


    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        model.put("album", albumsBean.find(albumId));
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {

        // Build blob to contain cover data
        // 1st arg = covers/N
        // 2nd arg = existing file to be saved/written
        // 3rd arg = file type

        Blob blob = new Blob(getCoverFile(albumId), uploadedFile.getInputStream(), uploadedFile.getContentType());

        blobStore.put(blob);

        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException, URISyntaxException {
        Optional<Blob> blobOptional = blobStore.get(getCoverFile(albumId));

        InputStream inputStream =  blobOptional.get().inputStream;
        byte[] imageBytes = MovieUtils.convertInputStreamToBytesArray(inputStream);

        HttpHeaders headers = createImageHttpHeaders(blobOptional.get().contentType, imageBytes.length);
        return new HttpEntity<>(imageBytes, headers);
    }


    private HttpHeaders createImageHttpHeaders(String contentType, int length) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(length);
        return headers;
    }

    private String getCoverFile(@PathVariable long albumId) {
        return format("covers/%d", albumId);
    }
}
