package projekti.model;

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
public class Account extends AbstractPersistable<Long> {
    
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    
    @ManyToOne
    private ProfilePicture profilePicture;

    @OneToMany(mappedBy = "account")
    private List<Skill> skills;

    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    @OneToMany(mappedBy = "account")
    private List<Comment> comments;

    @OneToMany(mappedBy = "account")
    private List<Praise> praises;

    @OneToMany(mappedBy = "account")
    private List<PostLike> postLikes;

    @OneToMany(mappedBy = "to")
    private List<Connection> connectionsIn;
    // Accounts that have a connection to this account (or pending request)

    @OneToMany(mappedBy = "from")
    private List<Connection> connectionsOut; 
    // Accounts that this account is connected to (or trying to connect)
}