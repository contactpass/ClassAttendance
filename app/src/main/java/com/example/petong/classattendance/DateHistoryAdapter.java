package com.example.petong.classattendance;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DateHistoryAdapter extends FirestoreRecyclerAdapter<Attendance, DateHistoryAdapter.DateHistoryHolder> {

    public DateHistoryAdapter(@NonNull FirestoreRecyclerOptions<Attendance> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DateHistoryHolder holder, int position, @NonNull Attendance model) {
        holder.studentcode.setText(model.getStudentID());
        holder.studentname.setText(model.getFullname());
    }

    @NonNull
    @Override
    public DateHistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_date_student, viewGroup, false);
        return new DateHistoryHolder(v);
    }

    public class DateHistoryHolder extends RecyclerView.ViewHolder {

        TextView studentcode;
        TextView studentname;

        public DateHistoryHolder(@NonNull View itemView) {
            super(itemView);

            studentcode = itemView.findViewById(R.id.textView_stuID);
            studentname = itemView.findViewById(R.id.textView_stuName);
        }
    }
}
