package projekti.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import projekti.model.ProfilePicture;
import projekti.repository.ProfilePictureRepository;

@Controller
public class ProfilePictureController {

    @Autowired
    private ProfilePictureRepository profilePictureRepo;

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @PostMapping("/admin/addpicture")
    public String addPicture(@RequestParam String name,
                             @RequestParam("file") MultipartFile file) throws IOException {

        String contentType = file.getContentType();
        if (!contentType.equals("image/png")) {
            return "redirect:/homepage";
        }
        
        ProfilePicture picture = new ProfilePicture();
        picture.setPicture(file.getBytes());
        picture.setName(name);
        
        profilePictureRepo.save(picture);

        return "redirect:/admin";
    }

}