package com.subscriptionbox.services;

import com.subscriptionbox.dao.PremiumUserDAO;
import com.subscriptionbox.models.PremiumUser;

public class PremiumUserService {
    private PremiumUserDAO premiumUserDAO = new PremiumUserDAO();

    public PremiumUser getPremiumUserByUserId(int userId) {
        return premiumUserDAO.getPremiumUserByUserId(userId);
    }

    public void createPremiumUser(PremiumUser premiumUser) {
        premiumUserDAO.addPremiumUser(premiumUser);
    }
}
