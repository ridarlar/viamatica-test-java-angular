package com.consulti.api.roles;

import com.consulti.api.model.Privilege;
import com.consulti.api.model.Role;
import com.consulti.api.model.User;
import com.consulti.api.repository.PrivilegeRepository;
import com.consulti.api.repository.RoleRepository;
import com.consulti.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup= false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    public SetupDataLoader(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PrivilegeRepository privilegeRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event){

        User adminUser=this.userRepository.findUserByUserName("admin");

        if (adminUser!=null) {
            return;
        }
        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findOneByName("ROLE_ADMIN");
        User user = new User();
        user.setUserName("admin");
        user.setFirstName("Richard");
        user.setLastName("Aguilar");
        user.setIdentityCard(125361346);
        user.setDob(LocalDate.of(2000,3,2));
        user.setPassword("admin");
        user.setIsAdmin(true);
        user.setPasswordEncripted(user.getPassword());
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        alreadySetup = true;
    }


    @Transactional
    public Privilege createPrivilegeIfNotFound(String name){
        Privilege privilege = this.privilegeRepository.findByName(name);
        if(privilege == null){
            privilege = new Privilege(name);
            this.privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public void createRoleIfNotFound(
            String name,
            Collection<Privilege> privileges
    ){
        Role role = this.roleRepository.findByName(name);
        if(role == null){
            role = new Role(name);
            role.setPrivileges(privileges);
            this.roleRepository.save(role);
        }
    }
}
