package com.consulti.api.controller;


import com.consulti.api.dto.CreatePersonDto;
import com.consulti.api.dto.EditPersonDto;
import com.consulti.api.model.Person;
import com.consulti.api.service.PesonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/person")
@CrossOrigin("*")
public class PersonController {
    private final PesonService personService;

    @Autowired
    public PersonController(PesonService personService) {
        this.personService = personService;
    }

    @CrossOrigin("*")
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Person createPerson(
            @RequestBody CreatePersonDto body){
        return this.personService.addNewPerson(body);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Person> findAllPersonsPaged(Pageable pageable){
        return this.personService.findAllPersonsPaged(pageable);
    }

    @GetMapping
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Person updatePerson(@RequestBody EditPersonDto body){
        return this.personService.updatePersonByBoddy(body);
    }
}
