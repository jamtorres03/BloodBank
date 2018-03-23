package com.bloodbank.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bloodbank.model.Notification;
import com.bloodbank.model.Request;
import com.bloodbank.model.RequestModel;
import com.bloodbank.model.User;
import com.bloodbank.model.UserModel;
import com.bloodbank.service.NotificationService;
import com.bloodbank.service.RequestService;
import com.bloodbank.service.UserService;
import com.google.gson.Gson;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private NotificationService notificationService;

	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/profile", method = RequestMethod.GET)
	public ModelAndView profile() {
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
			userService.updateUser(user);
			modelAndView.addObject("successMessage", "Successfully updated.");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("admin/profile");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/admin/map", method = RequestMethod.GET)
	public ModelAndView map() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/map");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/request", method = RequestMethod.GET)
	public ModelAndView request() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("request", new Request());
		modelAndView.setViewName("admin/request");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/request", method = RequestMethod.POST)
	public ModelAndView addRequest(@Valid Request request, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("admin/request");
		} else {
			modelAndView.addObject("successMessage", "Request sent!");
			User u = userService.findUserByEmail(auth.getName());
			request.setRequestedBy(u);

			// set to 1 = NEW, and save to DB
			request.setStatus(1);
			request.setRequestDate(new Date());
			request.setUuid(UUID.randomUUID().toString());
			requestService.saveRequest(request);

			// notify users
			List<User> list = userService.findByAvailable();
			Gson gson = new Gson();
			for (User user : list) {
				RequestModel model = new RequestModel();
				BeanUtils.copyProperties(request, model);
				notificationService.notify(new Notification(gson.toJson(model)), user.getEmail());
			}
			modelAndView.addObject("requests", requestService.findByStatus(1));
			modelAndView.setViewName("admin/donate");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/admin/users", method = RequestMethod.POST)
	@ResponseBody
	public List<UserModel> generateJSONPosts() {
		List<UserModel> list = new ArrayList<UserModel>();
		for (User user : userService.findByAvailable()) {
			UserModel model = new UserModel();
			BeanUtils.copyProperties(user, model);
			list.add(model);
		}
		return list;
	}

	@RequestMapping(value = "/admin/donate", method = RequestMethod.GET)
	public ModelAndView donate() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/donate");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/requests", method = RequestMethod.POST)
	@ResponseBody
	public List<RequestModel> generateJSONPostsRequest() {
		List<RequestModel> list = new ArrayList<RequestModel>();
		for (Request request : requestService.findByStatus(1)) {
			RequestModel requestModel = new RequestModel();
			BeanUtils.copyProperties(request, requestModel);

			UserModel userR = new UserModel();
			BeanUtils.copyProperties(request.getRequestedBy(), userR);
			requestModel.setRequestedBy(userR);

			if (null != request.getAcceptedBy()) {
				UserModel user = new UserModel();
				BeanUtils.copyProperties(request.getAcceptedBy(), user);
				requestModel.setAcceptedBy(user);
			}
			
			list.add(requestModel);
		}

		return list;
	}
	
	@RequestMapping(value = "/admin/donate-now", method = RequestMethod.GET)
	@ResponseBody
	public RequestModel donateTo(@RequestParam String uuid) {
		Request request = requestService.findByUuid(uuid);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		if (request.getStatus() == 1) {
			request.setStatus(2);
			request.setAcceptedBy(user);
			request.setAcceptDate(new Date());
			requestService.saveRequest(request);
			
			RequestModel requestModel = new RequestModel();
			BeanUtils.copyProperties(request, requestModel);
			
			UserModel userR = new UserModel();
			BeanUtils.copyProperties(request.getRequestedBy(), userR);
			requestModel.setRequestedBy(userR);
			
			UserModel userA = new UserModel();
			BeanUtils.copyProperties(request.getAcceptedBy(), userA);
			requestModel.setAcceptedBy(userA);
			
			Gson gson = new Gson();
			notificationService.notify(new Notification(gson.toJson(requestModel)), request.getRequestedBy().getEmail());
			return requestModel;
		} else {
			return null;
		}
	}
	
	@RequestMapping(value = "/admin/mydonations", method = RequestMethod.GET)
	public ModelAndView mydonations() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		List<Request> donations = requestService.findByAcceptedBy(user);
		modelAndView.addObject("donations", donations);
		modelAndView.setViewName("admin/mydonations");
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin/myrequests", method = RequestMethod.GET)
	public ModelAndView myrequests() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		List<Request> requests = requestService.findByRequestedBy(user);
		modelAndView.addObject("requests", requests);
		modelAndView.setViewName("admin/myrequests");
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin/request/status", method = RequestMethod.GET)
	@ResponseBody
	public String updateStatus(@RequestParam String uuid, @RequestParam String status) {
		Request request = requestService.findByUuid(uuid);
		request.setStatus(Integer.parseInt(status));
		requestService.saveRequest(request);
		return "success";
	}

}
