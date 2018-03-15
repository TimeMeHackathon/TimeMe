package com.superteam.timeme.db;

import com.superteam.timeme.objects.Client;
import com.superteam.timeme.objects.Project;
import com.superteam.timeme.objects.Session;

import java.util.List;

/**
 * Created by Ronit on 03/15/2018.
 */

public class InterfaceCallbacks {

    /* This class and its interfaces enable us to get data after Async connections to the Firebase database */

    public interface ListClientCallback {
        void onCallback(List<Client> clients);
    }

    public interface ListProjectCallback {
        void onCallback(List<Project> projects);
    }

    public interface ListSessionCallback {
        void onCallback(List<Session> sessions);
    }

    public interface SingleClientCallback {
        void onCallback(Client client);
    }

    public interface SingleProjectCallback {
        void onCallback(Project project);
    }

    public interface SingleSessionCallback {
        void onCallback(Session session);
    }
}
