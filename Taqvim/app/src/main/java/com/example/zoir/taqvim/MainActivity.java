package com.example.zoir.taqvim;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Calendar calendar;
    boolean flag = false;

    TextView tdata, Name;

    String nm[] = {"То Бомдод  ", "То Пешин  ", "То Aср  " , "То Шом  " , "То Хуфтан  "  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView s =  (TextView) findViewById(R.id.btm);
        s.setText( "03:40" );

        s =  (TextView) findViewById(R.id.ptm);
        s.setText( "13:00" );

        s =  (TextView) findViewById(R.id.atm);
        s.setText( "17:49" );

        s =  (TextView) findViewById(R.id.shtm);
        s.setText( "19:49" );

        s =  (TextView) findViewById(R.id.khtm);
        s.setText( "21:19" );

        tdata = (TextView) findViewById(R.id.rtm);
        Name = (TextView) findViewById(R.id.name);


        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }
    private void updateTextView() {
        Date noteTS = Calendar.getInstance().getTime();

        String time = "hh:mm:ss"; // 12:00
        String frmt = "HH:mm:ss";
        time = (String) DateFormat.format(frmt, noteTS);

        /*
        */

        //System.out.println( check(time, "13:00:00") );

        String namoz[] = {
             "03:40:00",
             "13:00:00",
             "17:49:00",
             "19:49:00",
             "21:19:00" };

        //System.out.println( bomdod + " " + time + " " + time.compareTo(bomdod) );

        for( int i=0; i<5; i++ ) {
            //System.out.println( nm[i] + " " + namoz[i] + " " + time + " "  + namoz[i].compareTo(time) ) ;
            if( namoz[i].compareTo(time) >= 0 ) {

                Name.setText( nm[i] );
                long ppp = check(time, namoz[i]);
                time = (String) String.format( "%02d:%02d:%02d" , ppp/3600, ppp%3600/60, ppp%3600%60 );
                tdata.setText(time );

                break;
            }
            else {
                if( i==4 ) {
                    Name.setText(nm[0]);
                    long ppp = 24 * 3600 - check(namoz[i], time);
                    time = (String) String.format( "%02d:%02d:%02d" , ppp/3600, ppp%3600/60, ppp%3600%60 );
                    tdata.setText(time );
                }
            }
        }
        ///String date = "dd MMMMM yyyy"; // 01 January 2013
        ///tvDate.setText(DateFormat.format(date, noteTS));
    }

    private long check( String s1, String s2 ) {
        long num1 = (s1.charAt(0)*10 + s1.charAt(1))*3600+(s1.charAt(3)*10 + s1.charAt(4))*60 + (s1.charAt(6)*10 + s1.charAt(7));
        long num2 = (s2.charAt(0)*10 + s2.charAt(1))*3600+(s2.charAt(3)*10 + s2.charAt(4))*60 + (s2.charAt(6)*10 + s2.charAt(7));

        //System.out.println( (num2 - num1) );

        return num2-num1;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}

