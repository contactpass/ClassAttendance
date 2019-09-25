package com.example.petong.classattendance;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
        Spinner spinnerDays;
        TextView textViewsTime;
        TextView textVieweTime;
        EditText editTextSection;
        EditText editTextRoom;
        CourseLec mCourse;
        String days;
        String stime;
        String etime;

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
            spinnerDays = view.findViewById(R.id.spinnerDay);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.days, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerDays.setAdapter(adapter);
            spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    days = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            textViewsTime = view.findViewById(R.id.textview_starttime);
            textVieweTime = view.findViewById(R.id.textview_endtime);
            editTextSection = view.findViewById(R.id.edit_section);
            editTextRoom = view.findViewById(R.id.edit_room);

            editTextCourseno.setText(course.getCourseID());
            editTextTitle.setText(course.getTitle());
            spinnerDays.setSelection(adapter.getPosition(course.getDay()));
            String[] time = course.getTime().split(" - ");
            final String[] starttime = time[0].split("(?<=\\G.{2})");
            final String[] endtime = time[1].split("(?<=\\G.{2})");
            stime = starttime[0] + " : " + starttime[1];
            etime = endtime[0] + " : " + endtime[1];
            textViewsTime.setText(stime);
            textViewsTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePicker = new TimePickerDialog(mContext, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hour = String.valueOf(hourOfDay);
                            String min = String.valueOf(minute);

                            if (hourOfDay < 10) {
                                hour = "0" + hourOfDay;
                            } if (minute < 10) {
                                min = "0" + minute;
                            }
                            String time = hour + " : " + min;
                            stime = hour + min;
                            textViewsTime.setText(time);
                            //Log.d("setTime", stime);
                        }
                    }, Integer.valueOf(starttime[0]), Integer.valueOf(starttime[1]), true);
                    timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    timePicker.show();
                }
            });
            textVieweTime.setText(etime);
            textVieweTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePicker = new TimePickerDialog(mContext, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hour = String.valueOf(hourOfDay);
                            String min = String.valueOf(minute);

                            if (hourOfDay < 10) {
                                hour = "0" + hourOfDay;
                            } if (minute < 10) {
                                min = "0" + minute;
                            }
                            String time = hour + " : " + min;
                            etime = hour + min;
                            textViewsTime.setText(time);
                            //Log.d("setTime", stime);
                        }
                    }, Integer.valueOf(endtime[0]), Integer.valueOf(endtime[1]), true);
                    timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    timePicker.show();
                }
            });

            HashMap<String, String> sec = course.getSectionLec();
            String section = sec.get(lecID);
            editTextSection.setText(section);
            HashMap<String, String> room = course.getSectionRoom();
            editTextRoom.setText(room.get(section));
            final HashMap<String, String> student = course.getStudenntID();

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
                            String day = days;
                            String time = stime + " - " + etime;
                            String sec = editTextSection.getText().toString();
                            HashMap<String, String> section = new HashMap<>();
                            section.put(lecID, sec);
                            HashMap<String, String> room = new HashMap<>();
                            room.put(sec, editTextRoom.getText().toString());

                            CourseLec course = new CourseLec(courseno ,title, day, time,true, section, room, student);
                            db.collection("Course").document(courseno).set(course);


                        }
                    });
            builder.show();
        }
    }
}
