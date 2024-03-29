package com.tuespotsolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

}
