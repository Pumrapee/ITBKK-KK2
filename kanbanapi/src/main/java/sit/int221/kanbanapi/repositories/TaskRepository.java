package sit.int221.kanbanapi.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sit.int221.kanbanapi.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.status.id = :newId WHERE t.status.id = :id")
    void transferTaskStatus(Integer id, Integer newId);
}
