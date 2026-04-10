package com.example.job_portal.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) {
        try {

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "folder", "resumes",
                            "type", "upload", // ✅ VERY IMPORTANT
                            "access_mode", "public" // ✅ force public
                    ));

            String url = uploadResult.get("secure_url").toString();

            // 🔥 replace image → raw
            url = url.replace("/image/upload/", "/raw/upload/");

            return url;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }
}
