package projekti.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import projekti.model.Comment;
import projekti.model.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"account"})
    Page<Comment> findByPost(Post post, Pageable pageable);
    
}
