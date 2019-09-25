package com.example.petong.classattendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class StudentCourseAdapter extends FirestoreRecyclerAdapter<CourseLec, StudentCourseAdapter.StudentCourseHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private FirebaseFirestore db;
    private String studentID;

    public StudentCourseAdapter(Context context, String studentID, @NonNull FirestoreRecyclerOptions<CourseLec> options) {
        super(options);
        this.mContext = context;
        this.studentID = studentID;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentCourseHolder holder, int position, @NonNull CourseLec model) {
        String mtitle = model.getCourseID() + " - " + model.getTitle();
        holder.textView_title.setText(mtitle);
        String section = null;
        Map<String, String> courseList = model.getStudenntID();
        for (String key: courseList.keySet()) {
            if (key.equals(studentID)  ) {
                section = courseList.get(key);
            }
        }
        holder.textView_section.setText(section);
        holder.textView_day.setText(model.getDay());
        holder.textView_time.setText(model.getTime());
        holder.textView_status.setText("eiei");
    }

    @NonNull
    @Override
    public StudentCourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_student_course, viewGroup, false);
        return new StudentCourseHolder(v);
    }

    public class StudentCourseHolder extends RecyclerView.ViewHolder {
        TextView textView_title;
        TextView textView_section;
        TextView textView_day;
        TextView textView_time;
        TextView textView_status;
        Button buttDetail;

        public StudentCourseHolder(@NonNull View itemView) {
            super(itemView);

            textView_title = itemView.findViewById(R.id.textTitle);
            textView_section =itemView.findViewById(R.id.sectionView);
            textView_day = itemView.findViewById(R.id.dayView);
            textView_time = itemView.findViewById(R.id.timeView);
            textView_status = itemView.findViewById(R.id.statusView);
            buttDetail = itemView.findViewById(R.id.button_attendance_detail);
            buttDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
    }
}
