package com.Shashwat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Shashwat.model.Post;
import com.Shashwat.model.User;
import com.Shashwat.service.PostService;
import com.Shashwat.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(value = "/newPost", method = RequestMethod.GET)
    public String newPost(Model model) {
        // Public site: creating posts is disabled for now — redirect to home
        return "redirect:/home";
    }

    @RequestMapping(value = "/newPost", method = RequestMethod.POST)
    public String createNewPost(@Valid Post post,
                                BindingResult bindingResult) {
        // Post creation disabled for public site
        return "redirect:/home";
    }

    @RequestMapping(value = "/editPost/{id}", method = RequestMethod.GET)
    public String editPostWithId(@PathVariable Long id,
                                 Model model) {
        // Editing posts disabled for public site
        return "redirect:/home";
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
    public String getPostWithId(@PathVariable Long id,
                                Model model) {

        Optional<Post> optionalPost = postService.findForId(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "/post";
        } else {
            return "/error";
        }
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.DELETE)
    public String deletePostWithId(@PathVariable Long id) {
        // Deleting posts disabled for public site
        return "redirect:/home";
    }

    private Optional<User> getDefaultAuthor() {
        Optional<User> author = userService.findByUsername("admin");
        if (author.isPresent()) {
            return author;
        }

        author = userService.findByUsername("user");
        if (author.isPresent()) {
            return author;
        }

        User fallback = new User();
        fallback.setUsername("admin");
        fallback.setPassword("admin");
        fallback.setEmail("admin@localhost");
        fallback.setName("Admin");
        fallback.setLastName("User");
        fallback.setActive(1);

        try {
            return Optional.of(userService.save(fallback));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
