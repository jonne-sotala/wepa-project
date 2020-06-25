package projekti.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePicture extends AbstractPersistable<Long> {

    private String name;

    // @Lob //@Lob didn't work in Heroku
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] picture;

    @OneToMany(mappedBy = "profilePicture")
    private List<Account> accounts;

}