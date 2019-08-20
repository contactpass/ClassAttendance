package com.example.petong.classattendance;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ShowStudentAdapter extends FirestoreRecyclerAdapter<Student, ShowStudentAdapter.ShowStudentHolder> {


    public ShowStudentAdapter(String lecturerID, @NonNull FirestoreRecyclerOptions<Student> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShowStudentHolder holder, int position, @NonNull Student model) {
        holder.studentcode.setText(model.getStudentID());
        String fullname = model.getFirstname() + "  " + model.getLastname();
        holder.studentname.setText(fullname);
        String stat;
        if (model.getStatus()){
            stat = "Online";
        } else {
            stat = "Offline";
        }
        holder.status.setText(stat);
    }

    @NonNull
    @Override
    public ShowStudentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_item, viewGroup, false);
        return new ShowStudentHolder(v);
    }

    public class ShowStudentHolder extends RecyclerView.ViewHolder {

        TextView studentcode;
        TextView studentname;
        TextView status;

        public ShowStudentHolder(@NonNull View itemView) {
            super(itemView);
            studentcode = itemView.findViewById(R.id.text_studentID);
            studentname = itemView.findViewById(R.id.text_studentname);
            status = itemView.findViewById(R.id.text_date);
        }
    }
}
