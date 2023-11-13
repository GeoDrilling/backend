package ru.nsu.fit.geodrilling.services.file;

import grillid9.laslib.LasReader;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class LasFileService {

    public ResponseEntity<?> upload(MultipartFile file) throws IOException {
        File fileDest = new File("C:\\GeoDrillingData\\temp\\" + file.getOriginalFilename());
        try {
            file.transferTo(fileDest);
            LasReader lasReader = new LasReader(fileDest.getAbsolutePath());
            lasReader.read();

        } catch (IOException e) {
            throw new IOException("Can't read file");
        } catch (IllegalStateException i) {
            throw new IllegalStateException("File has already transfered");
        }
        return null;
    }

}
