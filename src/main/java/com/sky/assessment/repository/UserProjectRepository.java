package com.sky.assessment.repository;

import com.sky.assessment.entity.UserProject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
  List<UserProject> findByUserId(Long userId);
}
