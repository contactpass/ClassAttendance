package com.example.petong.classattendance;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class StudentListAdapter extends FirestoreRecyclerAdapter<Student, StudentListAdapter.StudentHolder> {

    private Context mContext;
    private HistoryAdapter historyAdapter;
    private FirebaseFirestore db;
    private String courseNo;

    public StudentListAdapter(Context context, String id, @NonNull FirestoreRecyclerOptions options) {
        super(options);

        this.mContext = context;
        this.courseNo = id;
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentHolder holder, int position, @NonNull Student model) {
        String stuID = model.getStudentID();
        String stuName = model.getFirstname() + " " + model.getLastname();
        holder.textViewStuID.setText(stuID);
        holder.textViewStuName.setText(stuName);
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_student_list, viewGroup, false);
        return new StudentHolder(v);
    }

    public class StudentHolder extends RecyclerView.ViewHolder {

        TextView textViewStuID;
        TextView textViewStuName;
        Button buttonDetail;
        RecyclerView recyclerView;
        Student student;

        public StudentHolder(@NonNull View itemView) {
            super(itemView);

            db = FirebaseFirestore.getInstance();
            textViewStuID = itemView.findViewById(R.id.textView_stuID);
            textViewStuName = itemView.findViewById(R.id.textView_stuName);
            buttonDetail = itemView.findViewById(R.id.buttonDetail);
            buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
                        student = documentSnapshot.toObject(Student.class);
                        if (student != null) {
                            String stuID = student.getStudentID();
                            openDetailDialog(stuID);
                        }

                    }

                }
            });
        }
        private void openDetailDialog( String id) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);

            View view = inflater.inflate(R.layout.dialog_student_online, null);

            setupStudentRecyclerView(view, id);
            historyAdapter.startListening();

            builder.setView(view)
                    .setTitle("SHOW STUDENT ONLINE")
                    .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            historyAdapter.stopListening();
                            dialog.dismiss();

                        }
                    });
            builder.show();

            db.collection("ClassAttendance").whereEqualTo("studentID", id).whereEqualTo("courseID", courseNo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int k = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    k++;
                                } Toast.makeText(mContext, "Your Class Attendance is " + k, Toast.LENGTH_LONG).show();
                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }

                        }
                    });

        }

        private void setupStudentRecyclerView(View view, String id) {
            Query query = db.collection("ClassAttendance").whereEqualTo("studentID", id).whereEqualTo("courseID", courseNo).orderBy("date", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<Attendance> options = new FirestoreRecyclerOptions.Builder<Attendance>()
                    .setQuery(query, Attendance.class)
                    .build();
            historyAdapter = new HistoryAdapter(options);
            recyclerView = view.findViewById(R.id.student_recyclrview );
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(historyAdapter);

        }
    }
}
