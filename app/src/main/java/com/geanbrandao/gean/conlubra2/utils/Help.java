package com.geanbrandao.gean.conlubra2.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.geanbrandao.gean.conlubra2.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Help {

    private static final String TAG = "help";

    public static boolean buttonLike(Button b, Context c){
        if(b.getBackground().getConstantState().equals(c.getDrawable(R.drawable.ic_favorite_black_24dp).getConstantState())) {
            // tirou o like
            return false;
        } else {
            return true;
        }
    }

    public static List<String> addInList(List<String> l, String s){
        if(l != null) {
            l.add(s);
            Log.i("likes", "nao nullo - add like");
        } else {
            l = new ArrayList<>();
            l.add(s);
            Log.i("likes", "nullo - add like");
        }
        return l;
    }

    public static List<String> removeInList(List<String> l, String s){
        if(l != null) {
            l.remove(s);
            Log.i("likes", "nao nullo - removeu like");
        } else {
            Log.i("likes", "Não pode remover pq a lista está vazia");
        }
        return l;
    }

    public static boolean primeiroLogin(FirebaseAuth mAuth) {
        return (mAuth.getCurrentUser().getMetadata().getCreationTimestamp() + 60000 >= mAuth.getCurrentUser().getMetadata().getLastSignInTimestamp());
    }
}
