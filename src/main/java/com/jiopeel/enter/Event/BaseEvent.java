package com.jiopeel.enter.event;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiopeel.enter.bean.Base;
import com.jiopeel.enter.logic.BaseLogic;

@Controller
public class BaseEvent {
	
	@Resource
	private BaseLogic logic;
	
	@RequestMapping(value ={"/","/home"},method = RequestMethod.GET)
	public String home(HttpServletRequest request,Model model) {
		return "home";
	}
	
	@RequestMapping(value ="/homeinfo",method = RequestMethod.GET)
	public String homeinfo(HttpServletRequest request,Model model) throws Exception {
		Base bean = logic.syncqueryInfo();
		model.addAttribute("bean", bean);
		return "homeinfo";
	}
	
	@RequestMapping(value ="/about",method = RequestMethod.GET)
	public String about(HttpServletRequest request,Model model) {
		System.out.println("about");
		return "about";
	}

	@RequestMapping(value ="/contact",method = RequestMethod.GET)
	public String contact(HttpServletRequest request,Model model) {
		System.out.println("contact");
		return "contact";
	}
}
