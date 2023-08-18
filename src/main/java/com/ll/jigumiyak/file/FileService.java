package com.ll.jigumiyak.file;

import com.ll.jigumiyak.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.root.path}")
    private String fileRootPath;

    @Value("${file.origin.path}")
    private String fileOriginPath;

    private final FileRepository fileRepository;

    public GenFile upload(MultipartFile file, String primaryPath, String secondaryPath, String uploader) throws IOException {

        String[] fileBits = file.getContentType().split("/");
        String type = fileBits[0];
        String ext;

        switch (fileBits[1]) {
            case "jpg", "jpeg" -> ext = "jpg";
            case "png" -> ext ="png";
            default -> ext = "etc";
        }

        // file type == image
        // file ext == jpg, jpeg, png

        if (!type.equals("image") || ext.equals("etc")) {
            throw new IOException("file type not allowed");
        }

        // file path == root/primaryPath/secondaryPath
        // file name == uploader_date.ext;

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_n"));

        String saveDirPath = fileRootPath
                + File.separator + primaryPath
                + File.separator + secondaryPath;

        String saveFilePath = saveDirPath
                + File.separator + uploader
                + "_" + date
                + "." + ext;

        File target = new File(saveDirPath);
        if (!target.exists()) {
            log.info(saveDirPath + " 경로가 존재하지 않습니다.");
            target.mkdir();
            log.info(saveDirPath + " 경로를 생성했습니다.");
        } else {
            log.info(saveDirPath + " 경로가 존재합니다.");
        }

        file.transferTo(new File(saveFilePath));
        log.info(saveFilePath + " 파일 저장을 완료했습니다.");

        GenFile genFile = new GenFile();

        genFile.setPrimaryPath(primaryPath);
        genFile.setSecondaryPath(secondaryPath);
        genFile.setUploader(uploader);
        genFile.setDate(date);
        genFile.setExt(ext);
        genFile.setCreateDate(LocalDateTime.now());

        this.fileRepository.save(genFile);

        return genFile;
    }

    public GenFile getFile(Long id) {
        Optional<GenFile> ou = this.fileRepository.findById(id);
        if (ou.isPresent()) {
            return ou.get();
        } else {
            throw new DataNotFoundException("file not found");
        }
    }

    public String getFilePath(GenFile genFile) {
        return fileOriginPath + genFile.getPrimaryPath()
                + File.separator + genFile.getSecondaryPath()
                + File.separator + genFile.getUploader()
                + "_" + genFile.getDate()
                + "." + genFile.getExt();
    }
}