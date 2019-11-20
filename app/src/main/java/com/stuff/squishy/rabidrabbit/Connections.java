package com.stuff.squishy.rabidrabbit;

//_________Connection class to provide references to Firebase: Auth, storage and realtime db________________
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Connections
{
    private FirebaseAuth fb_authInstance;
    private FirebaseDatabase fb_database;



    private StorageReference fb_StorageRef;

    public Connections()
    {
        fb_authInstance = FirebaseAuth.getInstance();
        fb_database = FirebaseDatabase.getInstance();
        fb_StorageRef = FirebaseStorage.getInstance().getReference();

    }

    public FirebaseAuth getFb_authInstance()
    {
        return fb_authInstance;
    }

    public FirebaseDatabase getFb_database(){ return fb_database; }

    public StorageReference getFb_StorageRef()
    {
        return fb_StorageRef;
    }

}
