package com.example.petong.classattendance;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.HashMap;
import java.util.Map;

public class ShowCourseAdapter extends FirestoreRecyclerAdapter<CourseLec, ShowCourseAdapter.ShowCourseHolder> {

    private String lecID;

    public ShowCourseAdapter(String lecID , @NonNull FirestoreRecyclerOptions options) {
        super(options);
        this.lecID = lecID;
    }

    @NonNull
    @Override
    public ShowCourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_course_list, viewGroup, false);
        return new ShowCourseHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShowCourseHolder holder, int position, @NonNull CourseLec model) {
        String mtitle = model.getCourseID() + " - " + model.getTitle();
        holder.title.setText(mtitle);
        Log.d("Section",mtitle);
        HashMap<String, String> courseList = model.getSectionLec();


        for (String section : courseList.keySet()) {
            String lecturerID = courseList.get(section);
            if (lecID.equals(lecturerID)) {
                holder.section.setText(section);
            }
        }

        holder.day.setText(model.getDay());
        holder.time.setText(model.getTime());
    }

    public class ShowCourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView section;
        TextView day;
        TextView time;
        Button buttonCheck;
        Button buttonUpdate;
        Button buttonDelete;

        public ShowCourseHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText2);
            section = itemView.findViewById(R.id.sectionView2);
            day = itemView.findViewById(R.id.dayView2);
            time = itemView.findViewById(R.id.timeView2);
            buttonCheck = itemView.findViewById(R.id.button_Check);
            buttonCheck.setOnClickListener(this);
            buttonUpdate = itemView.findViewById(R.id.button_Update);
            buttonUpdate.setOnClickListener(this);
            buttonDelete = itemView.findViewById(R.id.button_Delete);
            buttonDelete.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.button_Check) {

            } else if (i == R.id.button_Update) {
                //signIn(mEmailField.getText().toString() + "@cmu.ac.th", mPasswordField.getText().toString());
            } else if (i == R.id.button_Delete) {
                //signIn(mEmailField.getText().toString() + "@cmu.ac.th", mPasswordField.getText().toString());
            }
        }
    }
}
