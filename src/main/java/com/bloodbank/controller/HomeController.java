package com.bloodbank.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bloodbank.model.User;
import com.bloodbank.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}
	
	@RequestMapping(value="/admin/profile", method = RequestMethod.GET)
	public ModelAndView profile(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", user);
		modelAndView.setViewName("admin/profile");
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin/profile", method = RequestMethod.POST)
	public ModelAndView updateUserDetails(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("admin/profile");
		} else {
			modelAndView.addObject("successMessage", "Successfully updated.");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("admin/profile");
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/admin/request", method = RequestMethod.GET)
	public ModelAndView request(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/request");
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin/request", method = RequestMethod.POST)
	public ModelAndView addRequest(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("admin/profile");
		} else {
			modelAndView.addObject("successMessage", "Successfully updated.");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("admin/profile");
		}
		return modelAndView;
	}
}
