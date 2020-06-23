package projekti.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePicture extends AbstractPersistable<Long> {

    @Lob
    private byte[] picture;

    @OneToMany(mappedBy = "profilePicture")
    private List<Account> accounts;

}