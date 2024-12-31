package com.example.education;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.education.databinding.FragmentPdfBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import Model.Course_Model;
import adapter.pdf_RecyclerAdapter;

public class pdf extends Fragment {
    FragmentPdfBinding bind;

    ArrayList<Data> pdf_list;

    pdf_RecyclerAdapter adapter;
    FirebaseAuth auth;
    FirebaseFirestore fireStore;
    String course;

    public pdf() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bind=FragmentPdfBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        assert getArguments() != null;
        course=getArguments().getString("course");

        pdf_list = new ArrayList<>();
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));
//        pdf_list.add(new Course_Model("MATH",R.drawable.baseline_picture_as_pdf_24));


        bind.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        adapter=new pdf_RecyclerAdapter(getContext(),pdf_list,course);

        bind.recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return bind.getRoot() ;
    }

    @Override
    public void onStart() {
        super.onStart();
        pdf_list.clear();
        fireStore.collection("courses").document(course).collection("pdf").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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