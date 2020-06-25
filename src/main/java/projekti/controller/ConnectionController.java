package projekti.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import projekti.model.Account;
import projekti.model.Connection;
import projekti.repository.AccountRepository;
import projekti.repository.ConnectionRepository;

@Controller
public class ConnectionController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ConnectionRepository connectionRepo;
    
    @GetMapping("/connections")
    public String getAccounts(Model model) {
        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
        Account a = accountRepo.findByUsername(auth.getName());
        model.addAttribute("current", 
                connectionRepo.findByFromAndStatus(a, "Connected"));
        model.addAttribute("incoming", 
                connectionRepo.findByToAndStatus(a, "Pending"));
        model.addAttribute("outgoing", 
                connectionRepo.findByFromAndStatus(a, "Pending"));

        List<Account> accounts = accountRepo.findAll();
        List<Account> others = new ArrayList<>();
        String username = auth.getName();
        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                continue;
            } else if (!account.getConnectionsOut().stream()
                        .filter(c -> c.getTo().getUsername().equals(username))
                        .collect(Collectors.toList()).isEmpty()) {
                continue;
            } else if (!account.getConnectionsIn().stream()
                        .filter(c -> c.getFrom().getUsername().equals(username))
                        .collect(Collectors.toList()).isEmpty()) {
                continue;
            }
            
            others.add(account);

        }
        model.addAttribute("others", others);
        return "connections";
    }

    @PostMapping("/connections/send/{id}")
    public String requestConnection(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();
        Account from = accountRepo.findByUsername(auth.getName());
        Account to = accountRepo.getOne(id);
        if (from.getId() == id) {
            return "redirect:/connections";
        }
        for (Connection c : from.getConnectionsIn()) {
            if (c.getFrom().getUsername().equals(to.getUsername())) {
                return "redirect:/connections";
            }
        }
        for (Connection c : from.getConnectionsOut()) {
            if (c.getTo().getUsername().equals(to.getUsername())) {
                return "redirect:/connections";
            }
        }

        Connection connection = new Connection();
        connection.setFrom(from);
        connection.setTo(to);
        connection.setStatus("Pending");
        connectionRepo.save(connection);
        
        return "redirect:/connections";
    }

    @Transactional
    @PostMapping("/connections/{id}/accept")
    public String acceptConnection(@PathVariable Long id) {
        Connection connection = connectionRepo.getOne(id);
        if (connection == null) {
            return "redirect:/connections";
        }

        connection.setStatus("Connected");
        
        Connection oppositeConnection = new Connection();
        oppositeConnection.setFrom(connection.getTo());
        oppositeConnection.setTo(connection.getFrom());
        oppositeConnection.setStatus("Connected");
        connectionRepo.save(oppositeConnection);

        return "redirect:/connections";
    }

    @PostMapping("/connections/{id}/cancel")
    public String cancelConnection(@PathVariable Long id) {
        Connection connection = connectionRepo.getOne(id);
        if (connection.getStatus().equals("Pending")) {
            connectionRepo.delete(connection);
        }

        return "redirect:/connections";
    }
    
    @Transactional
    @PostMapping("/connections/{id}/remove")
    public String removeConnection(@PathVariable Long id) {
        Connection connection = connectionRepo.getOne(id);
        connectionRepo.delete(connection);
        Connection oppositeConnection = connectionRepo.findByFromAndTo(
                                connection.getTo(), connection.getFrom());
        connectionRepo.delete(oppositeConnection);
        return "redirect:/connections";
    }
}