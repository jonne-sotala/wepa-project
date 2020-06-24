package projekti.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractPersistable<Long> 
                  implements Comparable<Post> {

    @ManyToOne
    private Account account;

    private String content;
    private LocalDateTime time;
    
    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    public int compareTo(Post post) {
        if (this.getTime().isBefore(post.getTime())) {
            return 1;
        }

        if (this.getTime().isAfter(post.getTime())) {
            return -1;
        }

        return 0;
    }
    
}
