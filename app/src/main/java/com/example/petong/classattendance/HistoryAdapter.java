package com.example.petong.classattendance;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HistoryAdapter extends FirestoreRecyclerAdapter<Attendance, HistoryAdapter.HistoryHolder> {


    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<Attendance> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull Attendance model) {
        holder.studentcode.setText(model.getStudentID());
        holder.course.setText(model.getCourseID());
        holder.date.setText(model.getDate());
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_history_list, viewGroup, false);
        return new HistoryHolder(v);
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView studentcode;
        TextView course;
        TextView date;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            studentcode = itemView.findViewById(R.id.text_studentID);
            course = itemView.findViewById(R.id.text_coursse);
            date = itemView.findViewById(R.id.text_date);
        }
    }
}