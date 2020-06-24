package projekti.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.model.Account;
import projekti.model.Comment;
import projekti.model.Post;
import projekti.repository.AccountRepository;
import projekti.repository.CommentRepository;
import projekti.repository.PostRepository;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("/posts/{id}")
    public String showComments(Model model, @PathVariable Long id) {
        Post post = postRepo.getOne(id);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("time").descending());
        model.addAttribute("comments", commentRepo.findByPost(post, pageable));
        model.addAttribute("post", post);

        return "comments";
    }

    @PostMapping("/posts/{id}/comment")
    public String postComment(@PathVariable Long id, 
                              @RequestParam String content) {

        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName());
        Post post = postRepo.getOne(id);
        
        Comment comment = new Comment();
        comment.setAccount(account);
        comment.setPost(post);
        comment.setContent(content);
        comment.setTime(LocalDateTime.now());
        commentRepo.save(comment);

        return "redirect:/posts/" + id;
    }
    
}