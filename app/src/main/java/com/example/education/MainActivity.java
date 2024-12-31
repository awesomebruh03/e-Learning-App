package com.example.education;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.education.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

import Model.Course_Model;
import Model.User_Model;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    TextView gradient;
    private BottomNavigationView bottom_nav_view;
    private  DrawerLayout draw;
    Content_main adapter;
    ArrayList<Course_Model> list;
    ActivityMainBinding bind;
    RecyclerView courseRv;
    FirebaseAuth auth;
    FirebaseFirestore store;
    NavigationView navigationView;
    TextView name,email;
    ImageView profile_pic;
    CardView card1,card2,card3,card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        bottom_nav_view=findViewById(R.id.navigation);
        courseRv=findViewById(R.id.recycler_courses);

        auth = FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();
        navigationView=bind.navView;
        View header = navigationView.getHeaderView(0);
        name=header.findViewById(R.id.text_navigation);
        email=header.findViewById(R.id.text_navigation_email);
        profile_pic=header.findViewById(R.id.imageView);
        draw =findViewById(R.id.drawer_layout);
        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        card4=findViewById(R.id.card4);


        gradient=findViewById(R.id.gradient);

        TextPaint paint = gradient.getPaint();
        float width = paint.measureText("Continue to \\n your Course");
        //gradient textview colour
        Shader textShader = new LinearGradient(0, 0, width, gradient.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#FFFFFF"),
                }, null, Shader.TileMode.CLAMP);
        gradient.getPaint().setShader(textShader);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){

                    case R.id.nav_Upload:
                        Intent intent=new Intent(MainActivity.this,Pdf_Video_Upload.class);
                        startActivity(intent);
                        draw.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.discuss:
                        Intent intent4=new Intent(MainActivity.this,PersonalChat.class);
                        startActivity(intent4);
                        draw.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.browse:
                        Intent intent2=new Intent(MainActivity.this,IncognitoActivity.class);
                        startActivity(intent2);
                        draw.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.gpt:
                        Intent intent3=new Intent(MainActivity.this,Setting.class);
                        startActivity(intent3);
                        draw.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_logout:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this).setTitle("Log out!!")
                                        .setMessage("Are you sure?").setIcon(R.drawable.baseline_delete_24)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                auth.signOut();
                                                startActivity(new Intent(getApplicationContext(), Login_Module.class));
                                                finish();
                                            }
                                        })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                        dialog.show();
                        return true;
                    case R.id.nav_share:
                        shareApp();
                        return true;
                }
                return true;
            }
        });
        profile_pic.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),profile.class));
            draw.closeDrawer(GravityCompat.START);
        });

        list = new ArrayList<>();
        list.add(new Course_Model("MATHEMATICS      ",R.drawable.ic_pi));
        list.add(new Course_Model("App Development   ",R.drawable.android_developer));
        list.add(new Course_Model("Web Development    ",R.drawable.web));
        list.add(new Course_Model("Java                       ",R.drawable.java_logo));
        list.add(new Course_Model("Python                     ",R.drawable.py));


        courseRv.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new Content_main(MainActivity.this,list);
        courseRv.setAdapter(adapter);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Pdf_Videos.class);
                intent.putExtra("course","Stories");
                startActivity(intent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Pdf_Videos.class);
                intent.putExtra("course","KG");
                startActivity(intent);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Pdf_Videos.class);
                intent.putExtra("course","Basic Maths");
                startActivity(intent);
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Pdf_Videos.class);
                intent.putExtra("course","Cartoons");
                startActivity(intent);
            }
        });

        bottom_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.nav_MyCourses:
                        intent=new Intent(MainActivity.this,avail_courses.class);
                        startActivity(intent);
                       return true;
                    case R.id.nav_Home:
//                        intent=new Intent(MainActivity.this,Pdf_Videos.class);
//                        startActivity(intent);
                        return true;
                    case R.id.nav_Search:
                        intent=new Intent(MainActivity.this,search.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_MyProfile:
                        intent=new Intent(MainActivity.this,profile.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_Menu:
                        draw.openDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //share function in Navigation
    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareText = "Check out this awesome app!";
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        store.collection("Users").document(Objects.requireNonNull(auth.getUid())).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    if (value!=null){
                        User_Model user_model = value.toObject(User_Model.class);
                        if (user_model!=null){
                            Glide.with(MainActivity.this).load(user_model.getProfile_pic()).apply(RequestOptions.circleCropTransform()).error(R.drawable.boy_bag).placeholder(R.drawable.boy_bag).into(profile_pic)
                            ;
                            name.setText(user_model.getUserName());
                            email.setText(user_model.getEmail());
                        }
                    }
                }
            }
        });
    }
}