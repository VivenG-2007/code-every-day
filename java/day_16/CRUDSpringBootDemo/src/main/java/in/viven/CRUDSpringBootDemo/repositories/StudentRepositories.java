
package in.viven.CRUDSpringBootDemo.repositories;

import in.viven.CRUDSpringBootDemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepositories extends JpaRepository<Student, Long> {

    Optional<Student> findByIdAndIsDeletedFalse(Long id);

    List<Student> findByIsDeletedFalse();
}
