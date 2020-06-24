package projekti.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import projekti.model.Account;
import projekti.model.Post;
import projekti.model.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    
    PostLike findByAccountAndPost(Account account, Post post);
}