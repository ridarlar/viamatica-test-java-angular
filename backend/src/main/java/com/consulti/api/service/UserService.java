package com.consulti.api.service;

import com.consulti.api.dto.CreateUserDto;
import com.consulti.api.dto.EditUserDto;
import com.consulti.api.model.Role;
import com.consulti.api.model.User;
import com.consulti.api.repository.AttendanceRecordRepository;
import com.consulti.api.repository.RoleRepository;
import com.consulti.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
    private UserRepository userRepository;
    private AttendanceRecordRepository attendanceRecordRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AttendanceRecordRepository attendanceRecordRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.attendanceRecordRepository=attendanceRecordRepository;
    }

    public UserService() {
    }

    public User addNewUser(CreateUserDto body){
        User user=null;
        try {
            User userObject=new User(
                    body.userName,
//                    body.firstName,
//                    body.lastName,
//                    Integer.parseInt(body.identityCard),
//                    LocalDate.parse(body.dateOfBirth),
                    body.password,
                    false
            );
            Role userRole= this.roleRepository.findOneByName("ROLE_USER");

            userObject.setRoles(Arrays.asList(userRole));

            userObject.setPasswordEncripted(userObject.getPassword());
            user=this.userRepository.save(userObject);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }

    public User findUserByUserName(String userName) {
        return (User) userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found with userName: " + userName));
    }

    public List<User> findUsersByDateOfBirth(String dateOfBirth) {
        try {
            LocalDate parsedDateOfBirth = LocalDate.parse(dateOfBirth);
            return userRepository.findByDateOfBirth(parsedDateOfBirth);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<User> findAllUsers() {
        List<User> users= userRepository.findAll();
        return users.stream()
                .filter(user-> !user.getIsAdmin())
                .filter(user-> !user.getIsDeleted())
                .collect(Collectors.toList());
    }

    public User updateUserByBody(EditUserDto body) {
        try{
            User optionalUser = userRepository.findByUserName(body.oldUserName)
                    .orElseThrow(() -> new UsernameNotFoundException("El usuario con username" + body.oldUserName + "no existe"));

            if (optionalUser!=null) {

                if(body.userName != null){
                    optionalUser.setUserName(body.userName);
                }

                if (body.firstName != null) {
                    optionalUser.setFirstName(body.firstName);
                }

                if (body.lastName != null) {
                    optionalUser.setLastName(body.lastName);
                }

                if (body.identityCard != null) {
                    optionalUser.setIdentityCard(Integer.parseInt(body.identityCard));
                }

                if (body.dateOfBirth != null) {
                    optionalUser.setDob(LocalDate.parse(body.dateOfBirth));
                }

                return userRepository.save(optionalUser);
            } else {
                throw new RuntimeException("User not found with ID: " + body.userName);
            }
        }catch (Exception error){
            System.out.println(error.getMessage());
        }
        return null;
    }

    public String removeUser(String userName) {
        try {
            User currentUser = this.userRepository.findUserByUserName(userName);

            if (currentUser != null) {
                currentUser.setIsDelete();
                this.userRepository.save(currentUser);
            } else {
                return "User not found.";
            }
        } catch (Exception e) {
            // Handle specific exceptions if needed
            System.out.println("Error removing user: " + e.getMessage());
            return "Error removing user.";
        }
        return null;
    }


    public Page<User> findAllUsersPaged(Pageable pageable) {
        Page<User> content = this.userRepository.findAll(pageable);
        List<User> list = content.getContent();

        List<User> filteredList = list.stream()
                .filter(user -> !user.getIsAdmin())
                .collect(Collectors.toList());

        return new PageImpl<>(filteredList, pageable, content.getTotalElements());
    }
}
