package com.recipe.cookmaster.repository;

import com.recipe.cookmaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameAndPassword(String name, String password);

    User findByName(String name);

}
