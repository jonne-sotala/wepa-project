package projekti.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Praise extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Skill skill;

    @ManyToOne
    private Account account;

    private Integer count;

    public void praiseMore() {
        count++;
    }

}