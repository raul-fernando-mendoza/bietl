package io.javabrains.springsecurityjpa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.javabrains.springsecurityjpa.models.CurrentUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginCognito {
	static final Logger log = 
	        LoggerFactory.getLogger(LoginCognito.class);	
	@GetMapping("/loginCognito")
    //public String welcome(HttpServletRequest request) throws Exception { 
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		log.info("welcome called jsp {}", name);
		model.addAttribute("userName", CurrentUser.getUserName());
		model.addAttribute("roles", CurrentUser.getRoles());
        return "loginCognito";
    }	
}

