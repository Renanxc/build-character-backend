package com.rakuten.buildcharacterbackend.infrastructure.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class HomeController {
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:swagger-ui.html";
	}
}