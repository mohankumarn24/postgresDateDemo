package net.projectsync.postgresDateDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.projectsync.postgresDateDemo.entity.DateTime;

@Repository
public interface DateTimeRepository extends JpaRepository<DateTime, Integer> {

}
