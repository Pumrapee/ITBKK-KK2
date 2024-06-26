package sit.int221.kanbanapi.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import sit.int221.kanbanapi.configs.StatusConfiguration;
import sit.int221.kanbanapi.entities.Status;
import sit.int221.kanbanapi.entities.Task;
import sit.int221.kanbanapi.exceptions.BadRequestException;
import sit.int221.kanbanapi.exceptions.ItemNotFoundException;
import sit.int221.kanbanapi.exceptions.TaskLimitExceededException;
import sit.int221.kanbanapi.repositories.StatusRepository;
import sit.int221.kanbanapi.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private StatusConfiguration configuration;

    public List<Task> getAllTask() {
        return repository.findAll();
    }

    public List<Task> getAllTaskFilteredSorted(List<String> filterStatuses, String sortBy) {
        String sortProperty = sortBy != null ? sortBy : "createdOn";
        Sort sort = Sort.by(Sort.Order.asc(sortProperty));
        try {
            if (filterStatuses != null && !filterStatuses.isEmpty()) {
                return repository.findByStatusNamesSorted(filterStatuses, sort);
            } else {
                return repository.findAll(sort);
            }
        } catch (PropertyReferenceException e) {
            throw new BadRequestException("Invalid sortBy parameter: " + sortProperty);
        }
    }


    public Task getTaskById(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Task id " + id + " does not exist !!!"));
    }

    @Transactional
    public Task createTask(Task task) {
            if (configuration.getNonLimitedUpdatableDeletableStatuses().contains(task.getTaskStatus().getName())
                || statusLimitCheck(countTasksByStatus(task.getTaskStatus().getId()) + 1)) {
            return repository.save(task);
        } else {
            throw new TaskLimitExceededException("The status " + task.getTaskStatus().getName() + " has reached the task limit.");
        }
    }

    @Transactional
    public Task removeTask(Integer id) {
        Task task = repository.findById(id).orElseThrow(() -> new BadRequestException("Task id " + id + " does not exist !!!"));
        repository.delete(task);
        return task;
    }

    @Transactional
    public Task updateTask(Integer id, Task task) {
        if (configuration.getNonLimitedUpdatableDeletableStatuses().contains(task.getTaskStatus().getName())
                || statusLimitCheck(countTasksByStatus(task.getTaskStatus().getId()) + 1)) {
            Task existingTask = repository.findById(id).orElseThrow(() -> new BadRequestException("Task id " + id + " does not exist !!!"));
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setAssignees(task.getAssignees());
            existingTask.setTaskStatus(task.getTaskStatus());
            return repository.save(existingTask);
        } else {
            throw new TaskLimitExceededException("The status " + task.getTaskStatus().getName() + " has reached the task limit.");
        }
    }

    @Transactional
    public void transferTaskStatus(Integer id, Integer newId) {
        if (id == newId) {
            throw new BadRequestException("destination status for task transfer must be different from current status");
        }
        Status oldStatus = statusRepository.findById(id).orElseThrow(() -> new BadRequestException("The specified status for task transfer does not exist"));
        Status newStatus = statusRepository.findById(newId).orElseThrow(() -> new BadRequestException("The specified status for task transfer does not exist"));
        if (configuration.getNonLimitedUpdatableDeletableStatuses().contains(oldStatus.getName())){
            throw new BadRequestException("The status name '"+ oldStatus.getName() + "' cannot be transfered");
        }
        if (!configuration.getNonLimitedUpdatableDeletableStatuses().contains(oldStatus.getName())
                && configuration.getNonLimitedUpdatableDeletableStatuses().contains(newStatus.getName())
                || statusLimitCheck(countTasksByStatus(id) + countTasksByStatus(newId))){
            repository.transferTaskStatus(id, newId);
        } else {
            throw new TaskLimitExceededException("The destination status cannot be over the limit after transfer");
        }
    }

    public boolean findTaskStatus(Integer id) {
        return (repository.countTasksByStatus(id) != 0);
    }


    public Integer countTasksByStatus(Integer id) { return repository.countTasksByStatus(id); }
    public Boolean statusLimitCheck(Integer count) {
        if (configuration.getTaskLimitEnabled()) {
            return count <= configuration.getMaxTasksPerStatus();
        } else {
            return true;
        }
    }

    public boolean allStatusLimitCheck(StatusConfiguration newConfig) {
        if (newConfig.getTaskLimitEnabled()) {
            return repository.countTasksByStatus().stream().allMatch(tasks -> tasks <= newConfig.getMaxTasksPerStatus());
        } else {
            return true;
        }
    }
}
