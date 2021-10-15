package com.codegym.controller;

import com.codegym.model.Student;
import com.codegym.model.StudentForm;
import com.codegym.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/student")

public class StudentController {
    @Autowired
    private IStudentService studentService;

    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping("")
    public ModelAndView showAll(@RequestParam(name = "q" ,required = false) String name){
        ModelAndView modelAndView = new ModelAndView("/list");
        List<Student> students;
        if (name == null) {
            students = studentService.findAll();
        } else {
            students=studentService.findByName(name);
        }
        modelAndView.addObject("students",students);
        return modelAndView;
    }
    @GetMapping("/create")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("studentForm",new StudentForm());
        return modelAndView;
    }
    @PostMapping("/save")
    public String createStudent(@ModelAttribute StudentForm studentForm){
        MultipartFile multipartFile = studentForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(studentForm.getImage().getBytes(),new File(fileUpload +fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Student student = new Student(studentForm.getId(),studentForm.getName(),studentForm.getEmail(),studentForm.getAddress(),fileName);
        studentService.save(student);
        return "redirect:/student";
    }
    @GetMapping("/{id}/edit")
    public ModelAndView showEditForm(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/edit");
        StudentForm studentForm = new StudentForm();
        studentForm.setId(id);
        modelAndView.addObject("studentForm",studentForm);
        return modelAndView;
    }
    @PostMapping("/update")
    public String update(@ModelAttribute StudentForm studentForm){
        MultipartFile multipartFile = studentForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(studentForm.getImage().getBytes(),new File(fileUpload +fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Student student = new Student(studentForm.getId(),studentForm.getName(),studentForm.getAddress(),studentForm.getEmail(),fileName);
        studentService.save(student);
        return "redirect:/student";
    }
    @GetMapping("/{id}/delete")
    public String remove(@PathVariable Long id){
        studentService.remove(id);
        return "redirect:/student";
    }
    @GetMapping("/{id}/view")
    public String view(@PathVariable Long id,Model model){
        model.addAttribute("student",studentService.findById(id));
        return "/view";
    }
}
