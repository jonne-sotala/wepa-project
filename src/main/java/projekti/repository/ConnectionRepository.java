package projekti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projekti.model.Account;
import projekti.model.Connection;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    List<Connection> findByFromAndStatus(Account account, String state);
    List<Connection> findByToAndStatus(Account account, String state);
    Connection findByFromAndTo(Account from, Account to);
    
}
