package com.datttph39843.app1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {

    FirebaseFirestore dataBase;
    Button btnInsert, btnUpdate, btnDelete;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        tvResult = findViewById(R.id.demo151Tv1);
        dataBase = FirebaseFirestore.getInstance();
        btnInsert = findViewById(R.id.demo151BtnInsert);
        btnInsert.setOnClickListener(v -> insertFireBase(tvResult));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnUpdate = findViewById(R.id.demo151BtnUpdate);
        btnUpdate.setOnClickListener(v -> {
            updateFireBase(tvResult);
        });
        btnDelete = findViewById(R.id.demo151BtnDelete);
        btnDelete.setOnClickListener(v -> {
            deleteFireBase(tvResult);
        });
        selectDataFromFireBase(tvResult);
    }

    String id = "";
    ToDo toDo = null;

    public void insertFireBase(TextView tvResult) {
        id = UUID.randomUUID().toString();//lay id bat ki
        //tao doi tuong insert
        toDo = new ToDo(id, "title 2", "content 2");
        //chuyen doi doi tuong co the thao tac voi  firebase
        HashMap<String, Object> mapTodo = toDo.convertHashMap();
        //insert vao database
        dataBase = FirebaseFirestore.getInstance();//khoi tao dataBase
        dataBase.collection("TODO").document(id)
                .set(mapTodo)//doi tuong can insert
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("insert success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });

    }

    public void updateFireBase(TextView tvResult) {
        id = "3caa62b4-bd98-4293-b638-a0a1e7282430";
        toDo = new ToDo(id, "sua title 1", "sua content 1");
        dataBase.collection("TODO").document(toDo.getId())
                .update(toDo.convertHashMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("sua thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    public void deleteFireBase(TextView tvResult) {
        id = "3caa62b4-bd98-4293-b638-a0a1e7282430";
        dataBase.collection("TODO").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("xoa thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    String strResult = "";

    public ArrayList<ToDo> selectDataFromFireBase(TextView tvResult) {
        ArrayList<ToDo> list = new ArrayList<>();
        dataBase.collection("TODO").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            strResult="";
                            for (QueryDocumentSnapshot document : task.getResult()){
                                ToDo toDo1=document.toObject(ToDo.class);
                                strResult +=  "Id: " + toDo1.getId() + "\n";
                                list.add(toDo1);
                            }
                            tvResult.setText(strResult);
                        }
                        else {
                            tvResult.setText("doc that bai");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
        return list;
    }
}