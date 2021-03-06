package com.epam.study.snet.controller.filter;

import com.epam.study.snet.model.dao.*;
import com.epam.study.snet.model.entity.User;
import com.epam.study.snet.view.SessionInfo;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class SetSessionInfo implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getSession().getAttribute("loggedUser") != null) {
            User user = (User) req.getSession().getAttribute("loggedUser");
            try {
                StatusMessageDao statusMessageDao=DaoFactory.getFactory().getStatusMessageDao();
                UserDao userDao = DaoFactory.getFactory().getUserDao(statusMessageDao);
                MessageDao messageDao = DaoFactory.getFactory().getMessageDao(userDao);
                long registeredUsers = userDao.getNumber();
                int unreadMessages = messageDao.getNumberUnread(user);
                SessionInfo sessionInfo = new SessionInfo(unreadMessages, registeredUsers);
                req.getSession().setAttribute("sessionInfo", sessionInfo);
            } catch (DaoException e) {
                e.printStackTrace();
                req.getRequestDispatcher("/WEB-INF/pages/errorpage.jsp").forward(req, resp);
            }
        }
        if (req.getSession().getAttribute("locale") == null) {
            String defaultLocale = req.getLocale().getLanguage() + "_" + req.getLocale().getCountry();
            req.getSession().setAttribute("locale", req.getLocale());
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
