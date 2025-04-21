package com.subscriptionbox.services;

import com.subscriptionbox.dao.PreferenceDAO;
import com.subscriptionbox.models.Preference;

import java.util.List;

public class PreferenceService {
    private PreferenceDAO preferenceDAO = new PreferenceDAO();

    public List<Preference> getPreferencesByUserId(int userId) {
        return preferenceDAO.getPreferencesByUser(userId);
    }

    public void createPreference(Preference preference) {
        preferenceDAO.addPreference(preference);
    }
}

