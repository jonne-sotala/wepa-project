package projekti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projekti.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
}