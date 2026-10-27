
package in.viven.CRUDSpringBootDemo.repositories;

import in.viven.CRUDSpringBootDemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepositories
        extends JpaRepository<Student, Long> {

}
