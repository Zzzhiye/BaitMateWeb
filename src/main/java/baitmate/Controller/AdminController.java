package baitmate.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class AdminController {
	
	
	//@Autowired
	//CustomerRepository adminRepo;
	
	
	@RequestMapping("/home/{id}")
	public String hello(@PathVariable int id) {


		return "Home";
	}

}
