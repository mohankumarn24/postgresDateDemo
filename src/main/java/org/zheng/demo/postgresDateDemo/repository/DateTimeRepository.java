package org.zheng.demo.postgresDateDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zheng.demo.postgresDateDemo.entity.DateTime;

@Repository
public interface DateTimeRepository extends JpaRepository<DateTime, Integer> {

}
