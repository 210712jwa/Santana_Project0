package com.santa.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.santa.app.Application;
import com.santa.controller.Controller;
import com.santa.controller.ExceptionController;
import com.santa.controller.AccountController;
//import com.santa.controller.AccountController;
import com.santa.controller.ClientController;

import io.javalin.Javalin;

public class Application {

	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		app = Javalin.create();
		
		mapControllers(new ClientController(), new ExceptionController(), new AccountController());
		
		app.before((ctx) -> {
			logger.info(ctx.method() + " request received to the " + ctx.path() + " endpoint");
		});
		
		app.start(7000);
	}
	
	public static void mapControllers(Controller... controllers) {
		for (Controller c : controllers) {
			c.mapEndpoints(app); 
		}
	}

}
