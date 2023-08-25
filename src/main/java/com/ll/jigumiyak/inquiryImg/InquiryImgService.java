package com.ll.jigumiyak.inquiryImg;

import com.ll.jigumiyak.file.FileService;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.inquiry.Inquiry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class InquiryImgService {
    private final InquiryImgRepository inquiryImgRepository;
    private final FileService fileService;

    public InquiryImg create (MultipartFile file, Inquiry inquiry) throws IOException {
        GenFile img = this.fileService.upload(file, "inquiry", "img", "inquiry");

        InquiryImg inquiryImg = InquiryImg.builder()
                .img(img)
                .inquiry(inquiry)
                .build();
        this.inquiryImgRepository.save(inquiryImg);

        return inquiryImg;
    }
}
