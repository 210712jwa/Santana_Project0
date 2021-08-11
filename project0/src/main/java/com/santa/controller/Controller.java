package com.santa.controller;

import io.javalin.Javalin;

public interface Controller {

	public abstract void mapEndpoints(Javalin app);
}
