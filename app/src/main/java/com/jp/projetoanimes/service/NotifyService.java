package com.jp.projetoanimes.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jp.projetoanimes.processes.CriarNotificacao;
import com.jp.projetoanimes.types.Anime;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NotifyService extends Service {

    private DatabaseReference myRef;
    HashMap<String, Anime> listCompleta;

    public NotifyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        myRef = database.getReference(auth.getUid()).child("listaAtu");
        listCompleta = new HashMap<>();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Anime anime = dataSnapshot.getValue(Anime.class);
                listCompleta.put(anime.getIdentifier(), anime);
                if (anime.isLanc()){
                    agendaNotificacao(proxDay(anime.getDias()), anime);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Anime anime = dataSnapshot.getValue(Anime.class);
                listCompleta.remove(anime.getIdentifier());
                if(anime.isLanc() && anime.isAgend()){
                    cancelNotify(anime.getIdentifier());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private int proxDay(List<Integer> diass){
        Date data = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        int dia = c.get(Calendar.DAY_OF_WEEK);
        int proxDia = -1;
        for (Integer diaa: diass) {
            if (diaa > dia){
                proxDia = diaa;
                break;
            }
        }
        if (proxDia == -1){
            proxDia = diass.get(0);
        }
        int diasFuturos = proxDia-dia;
        return diasFuturos < 1 ? diasFuturos+7 : diasFuturos;
    }

    private void agendaNotificacao(int diasFuturos, Anime anime){
        Date data = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, diasFuturos);

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        Intent it = new Intent(this, CriarNotificacao.class);
        it.putExtra("identifier", anime.getIdentifier());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), anime.getIdentifier().hashCode(), it, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        anime.setAgend(true);
        myRef.child(anime.getIdentifier()).child("agend").setValue(true);
    }

    private void cancelNotify(String identifier){
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        Intent it = new Intent(this, CriarNotificacao.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), identifier.hashCode(), it, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
