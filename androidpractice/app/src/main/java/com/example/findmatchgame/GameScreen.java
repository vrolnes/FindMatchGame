package com.example.findmatchgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


public class GameScreen extends AppCompatActivity {
    private TextView oyunzorluk;
    private TextView timer;
    private TextView skor;
    private long stime;
    private TextView geri;
    private int nofcards;
    private int cskor = 0;
    private ImageView ima; //resim1
    private ImageView ima2; //resim2
    private ImageView ima3; //resim3
    private CountDownTimer ct;
    private ImageView açılani; //açılan resim
    private List<ImageView> imageViews = new ArrayList<>(); //resim arrayi
    private List ids = new ArrayList<>(); //resim idleri
    private boolean açılanvar = false; //açılan var mı
    Dictionary imagepairs = new Hashtable(); //eşleşen resimler
    Dictionary hangiresim = new Hashtable(); //resimlere gelen idler


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_game_screen);
        oyunzorluk = findViewById(R.id.oyunzorluk);
        timer = findViewById(R.id.timer);
        skor = findViewById(R.id.skor);
        skor.setText("" + cskor);
        geri = findViewById(R.id.geri);
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String zorluk = getIntent().getStringExtra("difficulty");
        oyunzorluk.setText("Zorluk: " + zorluk);
        if (zorluk.equalsIgnoreCase("kolay")) {
            stime = 90000;
            nofcards = 3;
        } else if (zorluk.equalsIgnoreCase("orta")) {
            stime = 60000;
            nofcards = 4;
        } else {
            stime = 30000;
            nofcards = 6;
        }

        for (int i = 1; i <= 6; i++) { //resim idleri ekle
            int resID = getResources().getIdentifier("i" + i, "drawable",
                    getPackageName());
            ids.add(resID);
        }
        createNotificationChannel();

    }

    @Override
    protected void onResume() {
        super.onResume();
        (new Handler()).postDelayed(this::closeImages, 3000);
        (new Handler()).postDelayed(this::startTimer, 3000);
        showImages();
    }

    protected void showImages() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, //dokunma
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        for (int i = 2; i <= 13; i++) {
            String idd = "imageView" + i;
            int resID = getResources().getIdentifier(idd, "id", getPackageName());
            ImageView ima2 = findViewById(resID);
            imageViews.add(ima2);
        }

        for (int i = 1; i <= nofcards; i++) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(imageViews.size());
            ImageView im1 = imageViews.get(randomIndex);
            im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    compare(im1);
                }
            });
            imageViews.remove(randomIndex);
            int randomIndexx = rand.nextInt(imageViews.size());
            ImageView im2 = imageViews.get(randomIndexx);
            im2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    compare(im2);
                }
            });
            imagepairs.put(im1, im2);
            imageViews.remove(randomIndexx);
        }

        for (Enumeration k = imagepairs.keys(); k.hasMoreElements(); ) {
            ImageView im1 = (ImageView) k.nextElement();
            ImageView im2 = (ImageView) imagepairs.get(im1);
            Random rand = new Random();
            int randomIndex = rand.nextInt(ids.size());
            int randomElement = (int) ids.get(randomIndex);
            ids.remove(randomIndex);
            im1.setImageResource(randomElement);
            im2.setImageResource(randomElement);
            hangiresim.put(im1, randomElement);
            hangiresim.put(im2, randomElement);
        }

    }

    protected void closeimage() {
        ima2.setImageResource(R.drawable.indir);
    }

    protected void closeimage2() {
        ima3.setImageResource(R.drawable.indir);
    }

    protected void clickale() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void compare(ImageView cim) {
        if (açılanvar == false) {
            cim.setImageResource((Integer) hangiresim.get(cim));
            açılani = cim;
            açılanvar = true;
        } else {
            try {
                if (imagepairs.get(açılani).equals(cim)) {

                    cim.setImageResource((Integer) hangiresim.get(cim));
                    cskor += 1;
                    skor.setText("" + cskor);
                    açılanvar = false;
                    if (cskor == nofcards) {
                        sendnot();
                    }
                    return;
                } else {


                    cim.setImageResource((Integer) hangiresim.get(cim));
                    ima2 = cim;
                    ima3 = açılani;
                    (new Handler()).postDelayed(this::closeimage, 2000);
                    (new Handler()).postDelayed(this::closeimage2, 2000);
                    (new Handler()).postDelayed(this::clickale, 2000);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, //dokunma
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    stime -= 7000;
                    ct.cancel();
                    startTimer();
                    açılanvar = false;
                    return;
                }
            } catch (Exception e) {
                try {


                    if (imagepairs.get(cim).equals(açılani)) {


                        cim.setImageResource((Integer) hangiresim.get(cim));
                        cskor += 1;
                        skor.setText("" + cskor);
                        açılanvar = false;
                        if (cskor == nofcards) {
                            sendnot();
                        }

                        return;
                    } else {


                        cim.setImageResource((Integer) hangiresim.get(cim));
                        ima2 = cim;
                        ima3 = açılani;
                        (new Handler()).postDelayed(this::closeimage, 2000);
                        (new Handler()).postDelayed(this::closeimage2, 2000);
                        (new Handler()).postDelayed(this::clickale, 2000);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, //dokunma
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        stime -= 7000;
                        ct.cancel();
                        startTimer();
                        açılanvar = false;
                        return;
                    }
                } catch (Exception ee) {
                    return;
                }
            }
        }

    }

    protected void closeImages() {
        for (int i = 2; i <= 13; i++) {
            String idd = "imageView" + i;
            int resID = getResources().getIdentifier(idd, "id", getPackageName());
            ima = findViewById(resID);
            ima.setImageResource(R.drawable.indir);
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void sendnot() {
        if (cskor==nofcards){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"1234")
                    .setSmallIcon(R.drawable.indir)
                    .setContentTitle("Tebrikler")
                    .setContentText("ALLAHIN İŞSİZİ...")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
            int cc=cskor*cskor;
            notificationManager.notify(cc, builder.build());
            return;
        }
        else {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"1234")
                .setSmallIcon(R.drawable.indir)
                .setContentTitle("Sen bir DANGALAKSIN")
                .setContentText("AMK ÖZÜRLÜSÜ...")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
        int cc=cskor*cskor;
        notificationManager.notify(cc, builder.build());}
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("bildirim");
            String description = ("bildirim işte");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1234", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("bildirim");
            String description = ("bildirim işte");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("12345", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    protected void startTimer() {
        ct = new CountDownTimer(stime, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Kalan Süre: " + millisUntilFinished / 1000);
                stime = millisUntilFinished;
            }

            public void onFinish() {
                timer.setText("Süre Bitti!");
                sendnot();

            }
        }.start();

    }
}
