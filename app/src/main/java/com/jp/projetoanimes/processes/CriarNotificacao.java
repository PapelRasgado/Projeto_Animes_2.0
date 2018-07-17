package com.jp.projetoanimes.processes;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jp.projetoanimes.R;
import com.jp.projetoanimes.activitys.DetailsActivity;
import com.jp.projetoanimes.types.Anime;

public class CriarNotificacao extends BroadcastReceiver {

    private Anime anime;
    private Context cont;
    private DatabaseReference myRef;

    @Override
    public void onReceive(Context context, Intent intent) {
        String identifier = intent.getStringExtra("identifier");
        cont = context;
        myRef = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("listaAtu").child(identifier);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                anime = dataSnapshot.getValue(Anime.class);
                makeNotification();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addListenerForSingleValueEvent(listener);
    }

    private void makeNotification() {
        Intent it = new Intent(cont, DetailsActivity.class);
        it.putExtra("anime_detalhe", anime.getIdentifier());
        it.putExtra("type", 0);
        PendingIntent p = getPendingIntent(anime.getIdentifier().hashCode(), it, cont);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(cont);
        notificacao.setSmallIcon(R.drawable.icon_notify);
        notificacao.setContentTitle("Anime novinho saindo do forno");
        notificacao.setContentText("Hoje tem episódio de " + anime.getNome() + ", se lembre de assistir!");
        notificacao.setContentIntent(p);
        notificacao.setLargeIcon(BitmapFactory.decodeResource(cont.getResources(),
                R.drawable.icon_notify));
        notificacao.setAutoCancel(true);
        if (Patterns.WEB_URL.matcher(anime.getLink()).matches()){
            Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(anime.getLink()));

            PendingIntent contentIntent = PendingIntent.getActivity(cont, anime.getLink().hashCode(), notificationIntent, 0);
            notificacao.addAction(R.drawable.ic_link, "Abrir no navegador", contentIntent);
        }


        NotificationManagerCompat nm = NotificationManagerCompat.from(cont);
        nm.notify(anime.getIdentifier().hashCode(), notificacao.build());
        myRef.child("agend").setValue(false);

    }

    private PendingIntent getPendingIntent(int id, Intent intent, Context context) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);

        return stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
