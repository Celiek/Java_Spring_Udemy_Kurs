package com.example.controller;

import com.example.model.Task;
import com.example.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
class TaskController {
    private final TaskRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private Task toCreate;

    TaskController(final TaskRepository repository){
        this.repository = repository;
    }

//    @GetMapping
//    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
//        Task result = repository.save(toCreate);
//        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
//    }

    @GetMapping( value = "/tasks" , params = {"!sort" , "!page" , "!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable("id") int id , @RequestBody @Valid Task toUpdate){
       if( repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        toUpdate.setId(id);
        repository.save(toUpdate);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create(   "/" + result.getId())).body(result);

    }


    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> updateTask(@PathVariable("id") int id ){
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

}
