package com.superteam.timeme.db;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.superteam.timeme.objects.Client;
import com.superteam.timeme.objects.Project;
import com.superteam.timeme.objects.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.superteam.timeme.db.InterfaceCallbacks.*;

/**
 * Created by Ronit on 03/15/2018.
 */

public class DBHandler {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String CLIENTS_COLUMN = "clients";
    private static final String PROJECTS_COLUMN = "projects";
    private static final String SESSION_COLUMN = "sessions";


    /* --------- Add items to Firebase database  --------- */

    public static void addClient(Client client) {
        client.setClientUID(mDatabase.child(CLIENTS_COLUMN).push().getKey());
        mDatabase.child(CLIENTS_COLUMN).child(client.getClientUID()).setValue(client);
    }

    // TODO must have clientUID added to it before reaching this method
    public static void addProject(Project project) {
        project.setProjectUID(mDatabase.child(PROJECTS_COLUMN).child(project.getClientUID()).push().getKey());
        mDatabase.child(PROJECTS_COLUMN).child(project.getClientUID()).child(project.getProjectUID()).setValue(project);
    }

    // TODO must have clientUID & projectUid added to it before reaching this method
    public static void addSession(Session session) {
        session.setSessionUID(mDatabase.child(SESSION_COLUMN).child(session.getClientUID()).child(session.getProjectUID()).push().getKey());
        mDatabase.child(SESSION_COLUMN).child(session.getClientUID()).child(session.getProjectUID()).child(session.getSessionUID()).setValue(session);
    }


    /* --------- Update items on Firebase database --------- */

    public static void updateClient(Client client) {
        mDatabase.child(CLIENTS_COLUMN).child(client.getClientUID()).setValue(client);
    }

    public static void updateProject(Project project) {
        mDatabase.child(PROJECTS_COLUMN).child(project.getClientUID()).child(project.getProjectUID()).setValue(project);
    }


    /* --------- Delete items from Firebase database --------- */

    public static void deleteClients(String clientUID) {
        mDatabase.child(CLIENTS_COLUMN).child(clientUID).removeValue();
        mDatabase.child(PROJECTS_COLUMN).child(clientUID).removeValue();
        mDatabase.child(SESSION_COLUMN).child(clientUID).removeValue();
    }

    public static void deleteProject(String clientUID, String projectUID) {
        mDatabase.child(PROJECTS_COLUMN).child(clientUID).child(projectUID).removeValue();
        mDatabase.child(SESSION_COLUMN).child(clientUID).child(projectUID).removeValue();
    }


    /* --------- Retrieve individual items from Firebase database --------- */

    public static void retrieveSingleClient(String clientUID, final SingleClientCallback myCallback) {
        mDatabase.child(CLIENTS_COLUMN).child(clientUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Client client = dataSnapshot.getValue(Client.class);
                myCallback.onCallback(client);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void retrieveSingleProject(Project project, final SingleProjectCallback myCallback) {
        mDatabase.child(PROJECTS_COLUMN).child(project.getClientUID()).child(project.getProjectUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                myCallback.onCallback(project);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void retrieveSingleSession(Session session, final SingleSessionCallback myCallback) {
        mDatabase.child(SESSION_COLUMN).child(session.getClientUID()).child(session.getProjectUID()).child(session.getSessionUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Session session = dataSnapshot.getValue(Session.class);
                myCallback.onCallback(session);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /* --------- Retrieve lists of items from Firebase database --------- */

    public static void retrieveListOfClients(final ListClientCallback myCallback) {
        mDatabase.child(CLIENTS_COLUMN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Client> clients = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Client client = noteDataSnapshot.getValue(Client.class);
                    clients.add(client);
                }
                myCallback.onCallback(clients);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public static void retrieveListOfProjects(final ListProjectCallback myCallback) {
        mDatabase.child(PROJECTS_COLUMN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Project> projects = new ArrayList<>();
                for (DataSnapshot clientSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot projectSnapshot: clientSnapshot.getChildren()) {
                        Project project = projectSnapshot.getValue(Project.class);
                        projects.add(project);
                    }
                }
                myCallback.onCallback(projects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void retrieveListOfAllProjectsOrderedByLastUse(final ListProjectCallback myCallback) {
        mDatabase.child(SESSION_COLUMN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Session> sessions = new ArrayList<>();
                for (DataSnapshot clientSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot projectSnapshot: clientSnapshot.getChildren()) {
                        for(DataSnapshot sessionSnapshot: projectSnapshot.getChildren()) {
                            Session session = sessionSnapshot.getValue(Session.class);
                            sessions.add(session);
                        }
                    }
                }

                // Sort sessions according to endTime
                Collections.sort(sessions, new Sortbyroll());

                // Retrieve unique projectUIDs in descending order of use
                final List<String> uniqueProjectIDSInOrder = new ArrayList<>();
                int sessionsListSize = sessions.size();
                for( int i = sessionsListSize; i > 0; i-- ) {
                    String projectUID = sessions.get(i-1).getProjectUID();
                    if (!uniqueProjectIDSInOrder.contains(projectUID)) {
                        uniqueProjectIDSInOrder.add(projectUID);
                    }
                }


                //Grab project objects
                retrieveListOfProjects(new ListProjectCallback() {
                    @Override
                    public void onCallback(List<Project> projects) {

                        List<Project> projectsOrderedList = new ArrayList<>();
                        int sizeUniqueProjectIDSInOrder = uniqueProjectIDSInOrder.size();
                        int sizeTotalProjects = projects.size();

                        // Place projects with timestamps to be in order
                        for( int i = 0; i < sizeUniqueProjectIDSInOrder; i++ ) {
                            String currentUniqueID = uniqueProjectIDSInOrder.get(i);

                            for( int j = 0; j < sizeTotalProjects; j++ ) {
                                if(projects.get(j).getProjectUID().equals(currentUniqueID)) {
                                    projectsOrderedList.add(projects.get(j));
                                }
                            }
                        }

                        // Add remaining projects (those without timestamps) to the list
                        for(Project project : projects) {
                            String projectUID = project.getProjectUID();
                            if (!uniqueProjectIDSInOrder.contains(projectUID)) {
                                projectsOrderedList.add(project);
                            }
                        }


                        myCallback.onCallback(projectsOrderedList);

                    }
                });




//                // Fetch all project objects
//                retrieveListOfProjects(new ListProjectCallback() {
//                    @Override
//                    public void onCallback(List<Project> projects) {
//
//                        int sizeUniqueProjectIDSInOrder = uniqueProjectIDSInOrder.size();
//                        int sizeTotalProjects = projects.size();
//
//                        // Place projects with timestamps to be in order
//                        for( int i = 0; i < sizeUniqueProjectIDSInOrder; i++ ) {
//                            String currentUniqueID = uniqueProjectIDSInOrder.get(i);
//
//                            for( int j = 0; j < sizeTotalProjects; j++ ) {
//                                if(projects.get(j).getProjectUID().equals(currentUniqueID)) {
//                                    projects.add(projects.get(j));
//                                }
//                            }
//                        }
//
//                        // Add remaining projects (those without timestamps) to the list
//                        for(Project project : projects) {
//                            String projectUID = project.getProjectUID();
//                            if (!uniqueProjectIDSInOrder.contains(projectUID)) {
//                                projects.add(project);
//                            }
//                        }
//
//                        myCallback.onCallback(projects);
//                    }
//                });





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//
//
//        Query query = mDatabase.child(SESSION_COLUMN).orderByChild("endTime");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//                    // Get all sessions
//                    List<Session> sessions = new ArrayList<>();
//                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                        Session session = issue.getValue(Session.class);
//                        sessions.add(session);
//                    }
//
//                    // Retrieve unique projectUIDs in descending order of use
//                    final List<String> uniqueProjectIDSInOrder = new ArrayList<>();
//                    int sessionsListSize = sessions.size();
//                    for( int i = sessionsListSize; i > 0; i-- ) {
//                        String projectUID = sessions.get(i-1).getProjectUID();
//                        if (!uniqueProjectIDSInOrder.contains(projectUID)) {
//                            uniqueProjectIDSInOrder.add(projectUID);
//                        }
//                    }
//
//                    // Organize all projects in desired order
//                    retrieveListOfProjects(new ListProjectCallback() {
//                        @Override
//                        public void onCallback(List<Project> projects) {
//
//                            List<Project> listOrderedProjects = new ArrayList<>();
//                            int sizeUniqueProjectIDSInOrder = uniqueProjectIDSInOrder.size();
//                            int sizeTotalProjects = projects.size();
//
//                            // Reorganize projects with timestamps to be on top
//                            for( int i = 0; i < sizeUniqueProjectIDSInOrder; i++ ) {
//                                String currentUniqueID = uniqueProjectIDSInOrder.get(i);
//
//                                for( int j = 0; j < sizeTotalProjects; j++ ) {
//                                    if(projects.get(j).getProjectUID().equals(currentUniqueID)) {
//                                        listOrderedProjects.add(projects.get(j));
//                                    }
//                                }
//                            }
//
//                            // Add remaining projects (those without timestamps) to the list
//                            for(Project project : projects) {
//                                String projectUID = project.getProjectUID();
//                                if (!uniqueProjectIDSInOrder.contains(projectUID)) {
//                                    listOrderedProjects.add(project);
//                                }
//                            }
//
//                            myCallback.onCallback(listOrderedProjects);
//                        }
//                    });
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    public static void retrieveProjectsOfSingleClient(String clientUID, final ListProjectCallback myCallback) {

        mDatabase.child(PROJECTS_COLUMN).child(clientUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Project> projects = new ArrayList<>();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Project project = issue.getValue(Project.class);
                        projects.add(project);
                    }
                    myCallback.onCallback(projects);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void retrieveSessionsOfSingleProject(String clientUID, String projectUID, final ListSessionCallback myCallback) {

        mDatabase.child(SESSION_COLUMN).child(clientUID).child(projectUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Session> sessions = new ArrayList<>();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Session session = issue.getValue(Session.class);
                        sessions.add(session);
                    }

                    myCallback.onCallback(sessions);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void retrieveSessionsOfSingleClient(String clientUID, final ListSessionCallback myCallback) {

        Query query = mDatabase.child(SESSION_COLUMN).child(clientUID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Session> sessions = new ArrayList<>();
                for(DataSnapshot project : dataSnapshot.getChildren()) {
                    for(DataSnapshot sessionsSnapshot : project.getChildren()) {
                        Session session = sessionsSnapshot.getValue(Session.class);
                        sessions.add(session);
                    }
                }

                myCallback.onCallback(sessions);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Used to sort sessions by endTime
    static class Sortbyroll implements Comparator<Session>
    {
        @Override
        public int compare(Session a, Session b) {
            return a.getEndTime() - b.getEndTime();
        }
    }
}
