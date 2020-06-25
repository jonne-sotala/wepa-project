package projekti.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import projekti.model.Account;
import projekti.model.ProfilePicture;
import projekti.model.Skill;
import projekti.repository.AccountRepository;
import projekti.repository.ProfilePictureRepository;
import projekti.repository.SkillRepository;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private SkillRepository skillRepo;

    @Autowired
    private ProfilePictureRepository profilePictureRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login/successful")
    public String successfulSignup() {
        return "successful";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, 
                         @RequestParam String password, 
                         @RequestParam String firstname, 
                         @RequestParam String lastname,
                         @RequestParam String gender) {

        if (accountRepo.findByUsername(username) != null) {
            return "redirect:/login";
        }

        Account a = new Account();
        a.setUsername(username.trim());
        a.setPassword(passwordEncoder.encode(password));
        a.setFirstName(firstname.trim());
        a.setLastName(lastname.trim());
        a.setProfilePicture(profilePictureRepo.findByName(gender));
        accountRepo.save(a);
        
        return "redirect:/login/successful";
    }

    @GetMapping("/homepage")
    public String getHomepage(Model model) {
        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
        String username = auth.getName();
        Account a = accountRepo.findByUsername(username);
        if (auth.getName().equals(username)) {
            model.addAttribute("authorize", true);
        } else {
            model.addAttribute("authorize", false);
        }

        model.addAttribute("title", "Homepage of " + a.getFirstName() + " " + a.getLastName());
        model.addAttribute("account", a);

        List<Skill> skills = skillRepo.findByAccount(a);
        model.addAttribute("skills", skills);

        Pageable pageable = PageRequest.of(0, 3, Sort.by("praiseCount").descending());
        model.addAttribute("top3", skillRepo.findByAccount(a, pageable));

        return "homepage";
    }

    @GetMapping("/homepage/user/{username}")
    public String getUserHomepage(@PathVariable String username,
                                   Model model) {
        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
                                
        Account a = accountRepo.findByUsername(username);
        if (auth.getName().equals(username)) {
            model.addAttribute("authorize", true);
        } else {
            model.addAttribute("authorize", false);
        }

        model.addAttribute("title", "Homepage of " + a.getFirstName() + " " + a.getLastName());
        model.addAttribute("account", a);

        List<Skill> skills = skillRepo.findByAccount(a);
        model.addAttribute("skills", skills);

        Pageable pageable = PageRequest.of(0, 3, Sort.by("praiseCount").descending());
        model.addAttribute("top3", skillRepo.findByAccount(a, pageable));

        return "homepage";
    }

    @GetMapping("/homepage/profilepictures/{id}")
    @ResponseBody
    public byte[] getProfilePicture(@PathVariable Long id) {
        return profilePictureRepo.getOne(id).getPicture();
    }
    
    @PostMapping("/homepage/profilepictures")
    public String saveProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (!contentType.equals("image/png") && !contentType.equals("image/jpg")) {
            return "redirect:/homepage";
        }
        
        ProfilePicture picture = new ProfilePicture();
        picture.setPicture(file.getBytes());
        
        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
                                
        Account a = accountRepo.findByUsername(auth.getName());
        a.setProfilePicture(picture);

        profilePictureRepo.save(picture);

        return "redirect:/homepage";
    }

}

