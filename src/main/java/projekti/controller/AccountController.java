package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.model.Account;
import projekti.repository.AccountRepository;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

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

}

