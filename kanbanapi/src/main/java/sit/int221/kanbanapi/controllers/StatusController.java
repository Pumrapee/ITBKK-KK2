package sit.int221.kanbanapi.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.kanbanapi.dtos.SimpleTaskDTO;
import sit.int221.kanbanapi.dtos.StatusDTO;
import sit.int221.kanbanapi.dtos.TaskCreateUpdateDTO;
import sit.int221.kanbanapi.entities.Status;
import sit.int221.kanbanapi.entities.Task;
import sit.int221.kanbanapi.services.StatusService;
import sit.int221.kanbanapi.services.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statuses")
public class StatusController {
    @Autowired
    private StatusService statusService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<Object> getAllStatus() {
        List<Status> statuses = statusService.getStatus();
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> addNewStatus(@RequestBody Status status) {
        Status createdStatus = statusService.createStatus(status);
        return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStatus(@RequestBody Status status, @PathVariable Integer id) {
        Status updatedStatus = statusService.updateStatus(id, status);
        return new ResponseEntity<>(updatedStatus, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void removeTask(@PathVariable Integer id) {
        statusService.removeStatus(id);
    }
    @DeleteMapping("/{id}/{newId}")
    public void transferStatus(@PathVariable Integer id, @PathVariable Integer newId) {
        taskService.transferTaskStatus(id, newId);
        statusService.removeStatus(id);
    }
}