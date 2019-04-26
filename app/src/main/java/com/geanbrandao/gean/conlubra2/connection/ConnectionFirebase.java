package com.geanbrandao.gean.conlubra2.connection;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConnectionFirebase {

    private static FirebaseFirestore firestore;
    private static FirebaseAuth auth;
    private static StorageReference storage;

    //retorna a instancia do FirebaseDatabase
    public static FirebaseFirestore getFirebaseFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
            Log.i("Instancia", " aqui " + firestore);
        }
        return firestore;
    }

    //retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static StorageReference getFirebaseStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }


}
