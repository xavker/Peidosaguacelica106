package com.example.peidosaguacelica106.Provider;

import com.google.firebase.auth.FirebaseAuth;

public class AuthProvider {
    FirebaseAuth auth;
    public AuthProvider(){
        auth=FirebaseAuth.getInstance();
    }

    public String getId(){
        return auth.getUid();
    }

    public void logout(){
        auth.signOut();
    }
}
