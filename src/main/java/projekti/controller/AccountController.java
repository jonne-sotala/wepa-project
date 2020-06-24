package projekti.controller;

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

import projekti.model.Account;
import projekti.model.Skill;
import projekti.repository.AccountRepository;
import projekti.repository.SkillRepository;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private SkillRepository skillRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(@RequestParam String username, 
                         @RequestParam String password, 
                         @RequestParam String firstname, 
                         @RequestParam String lastname) {

        if (accountRepo.findByUsername(username) != null) {
            return "redirect:/signup";
        }

        Account a = new Account();
        a.setUsername(username);
        a.setPassword(passwordEncoder.encode(password));
        a.setFirstName(firstname);
        a.setLastName(lastname);
        accountRepo.save(a);
        
        return "redirect:/login";
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


}

