package com.consulti.api.service;

import com.consulti.api.dto.CreatePersonDto;
import com.consulti.api.dto.EditPersonDto;
import com.consulti.api.model.Person;
import com.consulti.api.repository.PersonRespository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PesonService {
    private final PersonRespository personRespository;

    public PesonService(PersonRespository personRespository) {
        this.personRespository = personRespository;
    }

    public Person addNewPerson(CreatePersonDto body) throws RuntimeException {
        Person person=null;
        try{
            Person personObject=new Person(
                    body.firstName,
                    body.lastName,
                    Integer.parseInt(body.identityCard),
                    LocalDate.parse(body.dateOfBirth)
            );
            person = this.personRespository.save(personObject);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return person;
    }

    public Page<Person> findAllPersonsPaged(Pageable pageable) {
        Page<Person> content = this.personRespository.findAll(pageable);
        List<Person> list =content.getContent();

        return new PageImpl<>(list, pageable, content.getTotalElements());
    }

    public Person updatePersonByBoddy(EditPersonDto body) throws RuntimeException{
        Person person;
        try{
            person= this.personRespository.findByIdentityCard(body.identityCard);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return person;
    }
}
