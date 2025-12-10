package com.pos.grad_project.config;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
@RestController
public class BunnySignedUrlGenerator {
    private static final String BUNNY_CDN_HOST = "https://dola-cdn.b-cdn.net";
    private static final String BUNNY_SIGNING_KEY = "67dc16a9-4a53-4fcd-8b14-4a560d2827ae";
    @GetMapping("/encode")
    public String generateSignedUrl(
            @RequestParam String storedPath,
            @RequestParam int expireInSeconds
    ){
        try {
            // 1. تأكد إن عندنا path بس مش URL كامل
            String path = storedPath;

            if (path.startsWith("https://")) {
                path = path.replace(BUNNY_CDN_HOST, "");
            }
            // 2. وقت الانتهاء (Unix timestamp)
            long expiryTimestamp = (System.currentTimeMillis() / 1000) + expireInSeconds;
            // 3. النص اللي هنوقّعه
            String dataToSign = path + expiryTimestamp;
            // 4. توليد HMAC SHA256
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    BUNNY_SIGNING_KEY.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            sha256_HMAC.init(secretKey);
            byte[] hash = sha256_HMAC.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            // 5. بناء اللينك النهائي
            return BUNNY_CDN_HOST
                    + path
                    + "?token="
                    + token
                    + "&expires="
                    + expiryTimestamp;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signed URL", e);
        }
    }
}