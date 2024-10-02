
package com.kodbook.controllers;

import java.util.ArrayList;
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
		
		//getting specific user data
		@PostMapping("/profileNew")
	    public String profiledetails(@RequestParam Long id,Model model,HttpSession session) {
	        // Logic to handle the user ID, e.g., fetching user details
	        System.out.println("User ID: " + id);
	        String loginuser = (String) session.getAttribute("username");
	        //new
		    User user = userservice.getUserById(id);
		    String actype=user.getAccountType();
		    String na=user.getUsername();
		    List<String> frlist=user.getFriends();
		    System.out.print("Friends list "+frlist);
		    for(Object names:frlist) {
		    	if(names.equals(loginuser)) {
		    		 model.addAttribute("user",user);
		 	        List<Post> userPosts=user.getPosts();
		 	        model.addAttribute("userPosts",userPosts);
		 	        return "specificuserprofile";
		    	}
		    }
		    if(actype.equals("private")) {
		    	User userreq=userservice.getSpecificUser(id);
		    	model.addAttribute("user",userreq);
		    	return "private";
		    }
		    model.addAttribute("user",user);
 	        List<Post> userPosts=user.getPosts();
 	        model.addAttribute("userPosts",userPosts);
 	        return "specificuserprofile";
	    }
		//end
		
		//notifications
		//notifications
		@PostMapping("/addNotification")
		public String acceptRequest(@RequestParam Long id,Model model,HttpSession session)
		{
			System.out.println("User ID: " + id);
	        String loginuser = (String) session.getAttribute("username");
	        //new
		    User user = userservice.getUserById(id);
			
			List<String> notify=user.getNotification();
			if(notify==null) {
				notify = new ArrayList<String>();
			}
			notify.add(loginuser);
			
			user.setNotification(notify);
			userservice.addUser(user);
			model.addAttribute("user",user);
			System.out.println(notify);
			return "confirmform";
		}
		//end
		//displaying notifications
		@GetMapping("/mynotifications")
		public String mynotifications(Model model,HttpSession session) {
			String loginuser = (String) session.getAttribute("username");
			User user=userservice.getUser(loginuser);
			List<String> notify=user.getNotification();
			model.addAttribute("notification",notify);
			
			System.out.print(notify);
			return "mynotifications";
		}
		
		@PostMapping("/addmyfriend")
		public String confirmfriend(@RequestParam String username,HttpSession session) {
			String loginuser = (String) session.getAttribute("username");
		       // You should dynamically get the logged-in user
		    System.out.println("Requested username: " + username);
		    
		    User user=userservice.getUser(loginuser);
		    if (user == null) {
		        System.out.println("User not found: " + loginuser);
		        return "error"; // Handle appropriately
		    }
		    
		    List<String> frds = user.getFriends();
		    if (frds == null) {
		        frds = new ArrayList<>();
		    }
		    
		    if (!frds.contains(username)) { // Avoid adding duplicates
		        frds.add(username);
		    }
		    
		    user.setFriends(frds); // Ensure you have a setter for friends
		    userservice.addUser(user); // Save the user back to the database
		    
		    return "index"; // Redirect or return a view name
		}

		//forgot password
		@GetMapping("/openResetPassword")
		public String forgotPassword() {
			return "fgotpass";
		}
		@PostMapping("/forgotPassword")
		public String processForgotPassword(@RequestParam String email,Model model) {
			User user=userservice.findByEmail(email);
			
			System.out.println(user);
			if(user!=null) {
				model.addAttribute("user",user);
				return "resetpassword";
			}
			return "signup";
		}
		@PostMapping("/resetPassword")
		public String resetPassword(@RequestParam String email,
				@RequestParam("password") String newPassword) {
			System.out.print(email+" "+newPassword);
			User user=userservice.findByEmail(email);
			if(user!=null) {
				userservice.updatePassword(user,newPassword);
				return "index";
			}
			return "signup";
		}
		
		
		
		@GetMapping("/logout")
		public String logout(HttpSession session) {
			session.invalidate();
			return "index";
		}
}
