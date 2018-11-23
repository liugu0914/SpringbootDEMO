package com.jiopeel.enter.Event;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiopeel.enter.Logic.BaseLogic;
import com.jiopeel.enter.bean.Base;

@Controller
public class BaseEvent {
	
	@Autowired
	private BaseLogic logic;
	
	@RequestMapping(value ={"/","/home"},method = RequestMethod.GET)
	public String home(HttpServletRequest request,Model model) {
		
		return "home";
	}
	
	@RequestMapping(value ="/homeinfo",method = RequestMethod.GET)
	public String homeinfo(HttpServletRequest request,Model model) {
		List<Base> baseList = logic.getBaseList();
		System.out.println("homeinfo");
		model.addAttribute("beans",baseList);
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
