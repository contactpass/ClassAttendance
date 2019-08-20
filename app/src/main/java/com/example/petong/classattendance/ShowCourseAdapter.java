package com.example.petong.classattendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class ShowCourseAdapter extends FirestoreRecyclerAdapter<CourseLec, ShowCourseAdapter.ShowCourseHolder> {

    private String lecID;
    private Context mContext;
    private LayoutInflater mInflater;
    private FirebaseFirestore db;

    public ShowCourseAdapter(Context context, String lecID , @NonNull FirestoreRecyclerOptions options) {
        super(options);
        this.lecID = lecID;
        this.mContext = context;

        mInflater = LayoutInflater.from(mContext);
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
        String section = courseList.get(lecID);
        holder.section.setText(section);

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
        EditText editTextCourseno;
        EditText editTextTitle;
        EditText editTextDay;
        EditText editTextSTime;
        EditText editTextETime;
        EditText editTextSection;
        EditText editTextRoom;
        CourseLec mCourse;

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
            db = FirebaseFirestore.getInstance();
        }

        //public interface On


        @Override
        public void onClick(View v) {
            int i = v.getId();

            if (i == R.id.button_Check) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION ) {
                    DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
                    mCourse = documentSnapshot.toObject(CourseLec.class);
                    if (mCourse != null){
                        String id = mCourse.getCourseID();
                        String title = mCourse.getTitle();
                        HashMap<String, String> sec = mCourse.getSectionLec();
                        String section = sec.get(lecID);
                        Intent intent = new Intent(mContext, StudentListActivity.class);
                        intent.putExtra("CourseNO", id);
                        intent.putExtra("Title", title);
                        intent.putExtra("Section", section);
                        mContext.startActivity(intent);
                    }
                }
            } else if (i == R.id.button_Update) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION ) {
                    DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
                    mCourse = documentSnapshot.toObject(CourseLec.class);
                    if (mCourse != null){
                        String id = mCourse.getCourseID();
                        Log.d("UpdateClick", id);
                        openUpdateDialog(v, mCourse);
                    }
                }

            } else if (i == R.id.button_Delete) {
                deleteCourse();
            }
        }

        private void deleteCourse() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("CONFIRM DELETE")
                    .setMessage("Are you sure you want to delete this course")
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = getAdapterPosition();
                            getSnapshots().getSnapshot(position).getReference().delete();
                        }
                    });
            builder.show();
        }

        private void openUpdateDialog(View v, CourseLec course) {

            //db.collection("Course").document()

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View view = mInflater.inflate(R.layout.dialog_add_course, null);
            editTextCourseno = view.findViewById(R.id.edit_couseno);
            editTextTitle = view.findViewById(R.id.edit_title);
            editTextDay = view.findViewById(R.id.edit_day);
            editTextSTime = view.findViewById(R.id.edit_starttime);
            editTextETime = view.findViewById(R.id.edit_endtime);
            editTextSection = view.findViewById(R.id.edit_section);
            editTextRoom = view.findViewById(R.id.edit_room);

            editTextCourseno.setText(course.getCourseID());
            editTextTitle.setText(course.getTitle());
            editTextDay.setText(course.getDay());
            String[] time = course.getTime().split(" - ");
            editTextSTime.setText(time[0]);
            editTextETime.setText(time[1]);

            HashMap<String, String> sec = course.getSectionLec();
            String section = sec.get(lecID);
            editTextSection.setText(section);
            HashMap<String, String> room = course.getSectionRoom();
            editTextRoom.setText(room.get(section));

            builder.setView(view)
                    .setTitle("UPDATE COURSE")
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String courseno = editTextCourseno.getText().toString();
                            String title = editTextTitle.getText().toString();
                            String day = editTextDay.getText().toString();
                            String time = editTextSTime.getText().toString() + " - " + editTextETime.getText().toString();
                            String sec = editTextSection.getText().toString();
                            HashMap<String, String> section = new HashMap<>();
                            section.put(lecID, sec);
                            HashMap<String, String> room = new HashMap<>();
                            room.put(sec, editTextRoom.getText().toString());

                            CourseLec course = new CourseLec(courseno ,title, day, time,true, section, room);
                            db.collection("Course").document(courseno).set(course);


                        }
                    });
            builder.show();
        }
    }
}
