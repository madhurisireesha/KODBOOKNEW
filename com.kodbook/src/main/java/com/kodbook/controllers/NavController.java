
package com.kodbook.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kodbook.entities.Post;
import com.kodbook.entities.User;
import com.kodbook.services.PostService;
import com.kodbook.services.UserService;

import jakarta.servlet.http.HttpSession;
@Controller
public class NavController {
		@Autowired
		PostService service;
		@Autowired 
		UserService userservice;
		@GetMapping("/")
		public String index()
		{
			return "index";
		}
		@GetMapping("/openSignUp")
		public String openSignUp() {
			return "signUp";
		}
		@GetMapping("/openCreatePost")
		public String openCreatePost() {
			return "createPost";
		}
		@GetMapping("/goHome")
		public String login(Model model)
		{
			List<Post> allPosts=service.fetchAllPosts();
			model.addAttribute("allPosts",allPosts);
			return "home";
		}
		@GetMapping("/openMyProfile")
		public String openMyProfile(Model model, HttpSession session) {
			String username = (String) session.getAttribute("username");
			User user = userservice.getUser(username);
			model.addAttribute("user", user);
			
			//getting particular user post
			List<Post> userPosts=user.getPosts();
			
			model.addAttribute("userPosts",userPosts);
			
			return "myprofile";
		}
		
		
		@GetMapping("/openEditProfile")
		public String openEditProfile(HttpSession session) {
			if(session.getAttribute("username")!=null) {
				return "editprofile";
		}
		return "index";
			
		}
		@PostMapping("/profileNew")
	    public String profiledetails(@RequestParam Long id,Model model) {
	        // Logic to handle the user ID, e.g., fetching user details
	        System.out.println("User ID: " + id);
	        User user = userservice.getUserById(id);
	        model.addAttribute("user",user);
	        List<Post> userPosts=user.getPosts();
			
			model.addAttribute("userPosts",userPosts);
	        return "specificuserprofile"; // Adjust as needed
	    }
		@GetMapping("/logout")
		public String logout(HttpSession session) {
			session.invalidate();
			return "index";
		}
}
