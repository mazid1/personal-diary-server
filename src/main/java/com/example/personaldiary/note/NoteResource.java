package com.example.personaldiary.note;

import com.example.personaldiary.category.Category;
import com.example.personaldiary.category.CategoryRepository;
import com.example.personaldiary.user.User;
import com.example.personaldiary.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class NoteResource {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/api/notes")
    public List<Note> getAllNotes() {
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Note> userNotes = noteRepository.getNotesByUser(ud.getUsername());
        return userNotes;
    }

    @GetMapping("/api/note/{id}")
    public Note getNoteById(@PathVariable long id) {
        Optional<Note> note = noteRepository.findById(id);

        if (!note.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found (id-" + id + ")");
        }

        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!note.get().getUser().getUsername().equals(ud.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to get others data");
        }

        return note.get();
    }

    @DeleteMapping("/api/note/{id}")
    public void deleteNoteById(@PathVariable long id) {
        Optional<Note> note = noteRepository.findById(id);

        if (!note.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found (id-" + id + ")");
        }

        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!note.get().getUser().getUsername().equals(ud.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not delete others data");
        }

        noteRepository.delete(note.get());
    }

    @PostMapping("/api/note")
    public ResponseEntity<Object> createNote(@RequestBody Note note) {
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByUsername(ud.getUsername());
        note.setUser(currentUser);
        note.setDate(new Date());
        Optional<Category> categoryOptional = categoryRepository.findById(note.getCategory().getId());
        if (!categoryOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        note.setCategory(categoryOptional.get());

        Note savedNote = noteRepository.save(note);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedNote.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/api/note/{id}")
    public ResponseEntity<Object> updateNote(@RequestBody Note note, @PathVariable long id) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if (!noteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if(noteOptional.get().getUser() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!noteOptional.get().getUser().getUsername().equals(ud.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        note.setId(id);
        note.setUser(noteOptional.get().getUser());
        if(note.getTitle() == null) note.setTitle(noteOptional.get().getTitle());
        if(note.getDescription() == null) note.setDescription(noteOptional.get().getDescription());
        if(note.getCategory() == null) note.setCategory(noteOptional.get().getCategory());
        if(note.getDate() == null) note.setDate(new Date());

        noteRepository.save(note);

        return ResponseEntity.noContent().build();
    }
}
