package com.sky.assessment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_user_external_project")
@Data
public class UserProject {

  @Id
  @SequenceGenerator(name = "user_project_id_seq", sequenceName = "user_project_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_project_id_seq")
  Long id;

  @Column(name = "user_id", nullable = false)
  Long userId;

  @Column(name = "name", nullable = false, length = 120)
  String name;

}
