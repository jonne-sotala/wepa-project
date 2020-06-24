package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.model.Account;
import projekti.model.Praise;
import projekti.model.Skill;
import projekti.repository.AccountRepository;
import projekti.repository.PraiseRepository;
import projekti.repository.SkillRepository;

@Controller
public class SkillController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private SkillRepository skillRepo;

    @Autowired
    private PraiseRepository praiseRepo;

    @PostMapping("/homepage/user/{username}/skills/add")
    public String addSkill(@PathVariable String username, 
                           @RequestParam String content) {
        
        Skill skill = new Skill();
        skill.setAccount(accountRepo.findByUsername(username));
        skill.setContent(content);
        skill.setPraiseCount(0);
        skillRepo.save(skill);

        return "redirect:/homepage/user/" + username;
    }

    @Transactional
    @PostMapping("/homepage/user/{username}/skills/{id}")
    public String praise(@PathVariable String username,
                         @PathVariable Long id) {
                            
        Account a = accountRepo.findByUsername(username);
        Skill skill = skillRepo.getOne(id);
        Praise praise = praiseRepo.findBySkillAndAccount(skill, a);
        if (praise != null) {
            praise.praiseMore();
            skill.praise();
            return "redirect:/homepage/user/" + username;
        }

        praise = new Praise();
        praise.setSkill(skill);
        praise.setAccount(a);
        praise.setCount(1);
        praiseRepo.save(praise);
        
        skill.praise();

        return "redirect:/homepage/user/" + username;
    }
}

