package com.example.education;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.education.databinding.FragmentVideoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import adapter.VideoAdapter;
import adapter.pdf_RecyclerAdapter;

public class video extends Fragment {
    FragmentVideoBinding bind;
    ArrayList<Data> pdf_list;

    VideoAdapter adapter;
    FirebaseAuth auth;
    FirebaseFirestore fireStore;
    String course;
    public video() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bind=FragmentVideoBinding.inflate(inflater,container, false);
        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        assert getArguments() != null;
        course=getArguments().getString("course");

        pdf_list=new ArrayList<>();

        bind.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
//
        adapter=new VideoAdapter(getContext(),pdf_list,course);
//
        bind.recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return bind.getRoot();
    }
    public void onStart() {
        super.onStart();
        pdf_list.clear();
        fireStore.collection("courses").document(course).collection("video").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots!=null){
                    for (QueryDocumentSnapshot doc:queryDocumentSnapshots
                    ) {
                        Data data=doc.toObject(Data.class);
                        pdf_list.add(data);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "No result found", Toast.LENGTH_SHORT).show();
            }
        });
    }

}