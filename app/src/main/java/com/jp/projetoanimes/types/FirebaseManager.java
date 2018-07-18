package com.jp.projetoanimes.types;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    private static FirebaseDatabase database;
    private static FirebaseAuth auth;

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }

    public static FirebaseAuth getAuth() {
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
