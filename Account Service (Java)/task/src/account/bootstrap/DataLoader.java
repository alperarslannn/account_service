package account.bootstrap;

import account.api.security.Roles;
import account.domain.Group;
import account.domain.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DataLoader implements CommandLineRunner {

    private final GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createRoles();
    }

    private void createRoles() {
        if(Objects.isNull(groupRepository.findByName(Roles.ROLE_ADMINISTRATOR.name()))){
            groupRepository.save(new Group(Roles.ROLE_ADMINISTRATOR.name()));
            groupRepository.save(new Group(Roles.ROLE_USER.name()));
            groupRepository.save(new Group(Roles.ROLE_ACCOUNTANT.name()));
        }
    }
}

