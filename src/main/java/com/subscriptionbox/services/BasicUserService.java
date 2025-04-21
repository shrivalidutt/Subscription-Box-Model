package com.subscriptionbox.services;

import com.subscriptionbox.dao.BasicUserDAO;
import com.subscriptionbox.models.BasicUser;

public class BasicUserService {
    private BasicUserDAO basicUserDAO = new BasicUserDAO();

    public BasicUser getBasicUserByUserId(int userId) {
        return basicUserDAO.getBasicUserByUserId(userId);
    }

    public void createBasicUser(BasicUser basicUser) {
        basicUserDAO.addBasicUser(basicUser);
    }
}


