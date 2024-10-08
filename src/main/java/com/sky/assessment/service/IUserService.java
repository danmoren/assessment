package com.sky.assessment.service;

import com.sky.assessment.controller.request.UserProjectRequest;
import com.sky.assessment.controller.request.UserRequest;
import com.sky.assessment.controller.response.UserProjectResponse;
import com.sky.assessment.controller.response.UserResponse;
import java.util.List;

public interface IUserService {

  UserResponse getUserByEmail(String email);

  UserResponse getUserData(Long id);

  UserResponse createUser(UserRequest userRequest);

  void deleteUser(Long id);

  UserProjectResponse addProject(Long id, UserProjectRequest userProjectRequest);

  List<UserProjectResponse> getProjectsByUser(Long id);

}
