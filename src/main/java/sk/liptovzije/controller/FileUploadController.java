package sk.liptovzije.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.liptovzije.service.IStorageService;
import sk.liptovzije.utils.exception.StorageFileNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final IStorageService storageService;

    @Autowired
    public FileUploadController(IStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files/all")
    public ResponseEntity<List<String>> listAllFiles() throws IOException {
// prepares download links for files
//        List<String> fileNames = storageService.loadAll().map(
//                fileName -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "getFile", fileName.normalize().toString()).build().toString())
//                .collect(Collectors.toList());

        /* prepares links for images to display*/
        List<String> fileNames = storageService.loadAll().map(
                fileName -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "showFile", fileName.normalize().toString()).build().toString())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(fileNames);
    }

    /**
     * Prepares required image for download
     * @param filename
     * @return
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * Build link for image to be displayed in browser
     * @param filename
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/files/show/{filename:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> showFile(@PathVariable String filename) throws IOException {
        Resource imgFile = storageService.loadAsResource(filename);
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }

    @PostMapping("/files/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        storageService.store(file);
        System.out.println("Filename :" + file.getOriginalFilename());

        String serverFileLocation = "/files/show/" + file.getOriginalFilename();
        // might as well be /img/ + filename since Spring serves files from resource/ folder where images are stored
        return new ResponseEntity<>(serverFileLocation , HttpStatus.OK);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}