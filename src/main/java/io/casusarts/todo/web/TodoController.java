package io.casusarts.todo.web;


import io.casusarts.todo.data.TodoRepository;
import io.casusarts.todo.entity.Todo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private TodoRepository todoRepository;

    @ModelAttribute(name = "todo")
    public Todo todo() {
        return new Todo();
    }

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public String showAllTodos(Model model) {
        List<Todo> todos = new ArrayList<>();
        todoRepository.findAll().forEach(t -> todos.add(t));

        model.addAttribute("allTodos", todos);
        model.addAttribute("localDateTime", LocalDateTime.now());

        return "allTodos";
    }

    @GetMapping("/new")
    public String newTodo(Model model) {
        return "newTodo";
    }

    @PostMapping
    public String createTodo(@ModelAttribute Todo todo, Errors errors) {
        if (errors.hasErrors()) {
            return "allTodos";
        }
        todo.setCreatedAt(new Date());
        todoRepository.save(todo);

        return "redirect:/todo";
    }

    @GetMapping("/edit/{id}")
    public String editTodo(@PathVariable("id") Long id, Model model) {
        Optional<Todo> todo = todoRepository.findById(id);
        model.addAttribute("todo", todo);
        return "updateTodo";
    }

    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable Long id, @Valid Todo todo,
                             Errors errors, Model model) {
        if (errors.hasErrors()) {
            todo.setId(id);
            return "redirect:/todo";
        }

        todoRepository.save(todo);
        model.addAttribute("todo", todoRepository.findAll());
        return "redirect:/todo";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id, Model model) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID:" + id));
        todoRepository.delete(todo);
        model.addAttribute("todo", todo);

        return "redirect:/todo";
    }
}
