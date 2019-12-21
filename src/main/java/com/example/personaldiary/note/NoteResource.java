package com.example.personaldiary.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteResource {

    @Autowired
    private NoteRepository noteRepository;

}
