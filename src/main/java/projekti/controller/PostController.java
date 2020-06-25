package projekti.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.model.Account;
import projekti.model.Post;
import projekti.repository.AccountRepository;
import projekti.repository.ConnectionRepository;
import projekti.repository.PostRepository;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ConnectionRepository connectionRepo;

    @GetMapping("/posts")
    public String getPosts(Model model) {
        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
        Account a = accountRepo.findByUsername(auth.getName());
        List<Account> connectedAccounts = 
            connectionRepo.findByFromAndStatus(a, "Connected").stream()
                          .map(c -> c.getTo()).collect(Collectors.toList());

        connectedAccounts.add(a);

        List<Post> posts = postRepo.findAll().stream()
                    .filter(p -> connectedAccounts.contains(p.getAccount()))
                    .sorted().collect((Collectors.toList()));

        if (posts.size() > 25) {
            posts = posts.subList(0, 25);
        }
        
        model.addAttribute("posts", posts);
        model.addAttribute("test", new ArrayList<>());

        return "posts";
    }

    @PostMapping("/posts/create")
    public String createPost(@RequestParam String content) {
        if (content.trim().isEmpty()) {
            return "redirect:/posts";
        }

        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
        Account a = accountRepo.findByUsername(auth.getName());
        Post post = new Post();
        post.setAccount(a);
        post.setTime(LocalDateTime.now());
        post.setContent(content.trim());
        postRepo.save(post);

        return "redirect:/posts";
    }
    
}