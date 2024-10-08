package com.sky.assessment.mapper;

import com.sky.assessment.controller.request.UserProjectRequest;
import com.sky.assessment.controller.request.UserRequest;
import com.sky.assessment.controller.response.UserProjectResponse;
import com.sky.assessment.controller.response.UserResponse;
import com.sky.assessment.entity.Role;
import com.sky.assessment.entity.User;
import com.sky.assessment.entity.UserProject;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {
  Mapper INSTANCE = Mappers.getMapper(Mapper.class);

  @Mapping(source = "roles", target = "roles", qualifiedByName = "toRolesResponseMapper")
  UserResponse toUserResponse(User user);

  @Mapping(target = "roles", ignore = true)
  User toUserEntity(UserRequest userRequest);

  UserProjectResponse toUserProjectResponse(UserProject userProject);

  UserProject toUserProjectEntity(UserProjectRequest userProjectRequest);

  List<UserProjectResponse> toUserProjectResponseList(List<UserProject> userProjects);

  @Named("toRolesResponseMapper")
  static List<String> toRolesResponseMapper(Set<Role> roles) {
    return roles.stream().map(Role::getName).toList();
  }
}
