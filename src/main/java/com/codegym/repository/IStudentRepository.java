package com.codegym.repository;

import com.codegym.model.Student;

import java.util.List;

public interface IStudentRepository {
    List<Student> findAll();

    Student findById(Long id);

    void save(Student student);

    void remove(Long id);

    List<Student> findByName(String name);
}
