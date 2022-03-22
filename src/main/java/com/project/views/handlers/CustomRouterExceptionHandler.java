package com.project.views.handlers;


import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;

import javax.servlet.http.HttpServletResponse;

public class CustomRouterExceptionHandler extends RouteNotFoundError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {
        getElement().setText(
                "This is custom router exception handler. Such page does not exist");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
