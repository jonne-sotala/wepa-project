package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import projekti.model.Account;
import projekti.model.PostLike;
import projekti.model.Post;
import projekti.repository.AccountRepository;
import projekti.repository.PostLikeRepository;
import projekti.repository.PostRepository;

@Controller
public class LikeController {

    @Autowired
    private PostLikeRepository postLikeRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PostRepository postRepo;

    @PostMapping("posts/{id}/like")
    public String likePost(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName());
        Post post = postRepo.getOne(id);

        if (postLikeRepo.findByAccountAndPost(account, post) == null) {
            PostLike like = new PostLike(account, post);
            postLikeRepo.save(like);
        }
        
        return "redirect:/posts";
    }
    
}