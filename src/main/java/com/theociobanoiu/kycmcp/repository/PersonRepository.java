package com.theociobanoiu.kycmcp.repository;

import com.theociobanoiu.kycmcp.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

interface PersonRepository extends JpaRepository<Person, Long> {

}
