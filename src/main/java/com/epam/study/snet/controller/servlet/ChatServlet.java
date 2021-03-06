package com.epam.study.snet.controller.servlet;

import com.epam.study.snet.model.dao.*;
import com.epam.study.snet.model.entity.Message;
import com.epam.study.snet.model.entity.User;
import com.epam.study.snet.view.Enums.FormErrors;
import com.epam.study.snet.view.Chat;
import com.epam.study.snet.controller.validators.MessageValidator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/main/chat")
public class ChatServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(ChatServlet.class.getCanonicalName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = (User) req.getSession().getAttribute("loggedUser");
        try {
            Chat chat = new Chat(loggedUser, req.getParameter("companionId"), req.getParameter("page"));
            req.setAttribute("chat", chat);
            req.getRequestDispatcher("/WEB-INF/pages/chat.jsp").forward(req, resp);
        } catch (DaoException e) {
            log.error("[" + loggedUser.getUsername() + "](id:[" + loggedUser.getId() + "]) try to getById chat", e);
            req.getRequestDispatcher("/WEB-INF/pages/errorpage.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sender = (User) req.getSession().getAttribute("loggedUser");
        try {
            StatusMessageDao statusMessageDao=DaoFactory.getFactory().getStatusMessageDao();
            UserDao userDao = DaoFactory.getFactory().getUserDao(statusMessageDao);
            MessageDao messageDao = DaoFactory.getFactory().getMessageDao(userDao);
            User receiver = userDao.getById(Long.valueOf(req.getParameter("companionId")));

            String action = req.getParameter("action");

            if (action != null && action.equals("remove")) {
                //remove message
                long messageId = Long.valueOf(req.getParameter("messageId"));
                Message message = messageDao.getById(messageId);
                if (message.isUnread()) messageDao.removeById(messageId);
                String contextPath = req.getContextPath();
                resp.sendRedirect(contextPath + "/main/chat?companionId=" + receiver.getId());
            } else {
                //create message
                String body = req.getParameter("body");
                MessageValidator message = MessageValidator.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .body(body).build();
                Map<String, FormErrors> errors = message.validate();

                if(errors.isEmpty()) {
                    messageDao.create(message);
                    int unreadMessages = messageDao.getNumberUnread(sender);//todo ????
                    req.getSession().setAttribute("unreadMessages", unreadMessages);
                    String contextPath = req.getContextPath();
                    resp.sendRedirect(contextPath + "/main/chat?companionId=" + receiver.getId());
                } else{
                    //todo think about this
                   // req.setAttribute("errors", errors);
                    String contextPath = req.getContextPath();
                    resp.sendRedirect(contextPath + "/main/chat?companionId=" + receiver.getId());

                }
            }
        } catch (DaoException e) {
            log.error("[" + sender.getUsername() + "](id:[" + sender.getId() + "]) try to write/remove message", e);
            req.getRequestDispatcher("/WEB-INF/pages/errorpage.jsp").forward(req, resp);
        }
    }
}
