package com.example.zoir.company;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private DatabaseReference mRef;
    private DatabaseReference mZakaz;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private RecyclerView mBlogList;

    private ListView myUser;
    private List<String> connected = new ArrayList<String>();


    protected static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;


        myUser = (ListView) findViewById(R.id.user);
        ArrayAdapter<String> adapter = new ArrayAdapter< String > (this, android.R.layout.simple_list_item_1, connected );
        myUser.setAdapter(adapter);


        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot zakaz = dataSnapshot.child("Zakaz");
                for ( DataSnapshot mData : zakaz.getChildren() ) {
                    //name =  ( dataSnapshot.child("Users").child( mData.getKey() ).child("name").getValue().toString() );

                    String row = "Name:\n" +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("name").getValue().toString() +
                            "\n\nSize:\nArm - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("arm").getValue().toString() +
                            ", Bust - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("bust").getValue().toString() +
                            ", Height - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("height").getValue().toString() +
                            ", Hips - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("hips").getValue().toString() +
                            ", Hollow - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("hollow").getValue().toString() +
                            ", Leg - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("leg").getValue().toString() +
                            ", Shoulder - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("shoulder").getValue().toString() +
                            ", UppArm - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("uppArm").getValue().toString() +
                            ", Waist - " +
                            dataSnapshot.child("Users").child( mData.getKey() ).child("waist").getValue().toString() + "\n\nProducts: ";

                    DataSnapshot products = zakaz.child(mData.getKey());
                    for (DataSnapshot item: products.getChildren() ) {
                        row += "\n" + dataSnapshot.child("Blog").child(item.getKey()).child("title").getValue().toString();
                    }
                    connected.add(row);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //mBlogList = (RecyclerView) findViewById(R.id.Blog_list);
        //mBlogList.setHasFixedSize(true);
        //mBlogList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginActivity = new Intent(MainActivity.this, loginactivity.class);
                    loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginActivity);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


        /*
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mZakaz
        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {

                final String post_key = getRef(position).getKey();



            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
        */
    }

/*
    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title) {
            TextView  post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }
    }
*/
    @Override
    public void onBackPressed() {
        MainActivity.this.finish();
        if(loginactivity.LoginActivity != null){
            loginactivity.LoginActivity.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        else if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }
}
