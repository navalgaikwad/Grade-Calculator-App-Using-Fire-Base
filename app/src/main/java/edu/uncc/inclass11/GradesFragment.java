package edu.uncc.inclass11;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.inclass11.databinding.FragmentGradesBinding;
import edu.uncc.inclass11.databinding.GradeRowItemBinding;

public class GradesFragment extends Fragment {
    private FirebaseAuth mAuth;

    public GradesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentGradesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        /*gpa=0.0;
        totalHr=0;*/
        binding = FragmentGradesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:  {
                // navigate to settings screen
                mListener.addGradeFragment();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth= FirebaseAuth.getInstance();



        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getGrade();
                calculateGPA();

            }
        });

        binding.recyclerViewGrade.setLayoutManager(new LinearLayoutManager(getContext()));
        gradeAdapter = new GradeAdapter();
        binding.recyclerViewGrade.setAdapter(gradeAdapter);

        getActivity().setTitle(R.string.posts_label);

    }
    void getGrade(){

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db.collection("courses")
                .document(currentUser.getUid()).collection("userCourse")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mGrade.clear();
                for (QueryDocumentSnapshot document : value) {
                    if (document.getData().size()>0) {
                        Grade grade = new Grade();
                        Log.d("Demo", document.getId() + " => " + document.getData());
                        grade.setCourseName(document.getData().get("courseName").toString());
                        grade.setCourseNo(document.getData().get("courseNo").toString());
                        grade.setCreditHr(document.getData().get("creditHr").toString());
                        grade.setGrade(document.getData().get("grade").toString());
                        grade.setCourseId(document.getId());


                        mGrade.add(grade);
                    }
                }
                calculateGPA();
                gradeAdapter.notifyDataSetChanged();
            }
        });



    }

    void calculateGPA(){
        double gpa=0.0;
        double totalHr=0;
        int gradePointTotal = 0;
        for (Grade grade: mGrade) {
            totalHr=totalHr+grade.getTotalhr();
            grade.setGpa(gpa);
            gradePointTotal=gradePointTotal+ grade.getGradePointTotal();


            grade.setTotalhr((int) totalHr);
            gpa=gradePointTotal/totalHr;

        }
        binding.textViewGPA.setText("GPA :"+ String.valueOf(gpa).substring(0, 3));
        binding.textViewHr.setText("Total Hr :"+totalHr);

    }
    GradeAdapter gradeAdapter;
    ArrayList<Grade> mGrade = new ArrayList<>();



    class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.PostsViewHolder> {
        @NonNull
        @Override
        public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            GradeRowItemBinding binding = GradeRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new PostsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
            Grade post = mGrade.get(position);
            holder.setupUI(post);
        }

        @Override
        public int getItemCount() {
            return mGrade.size();
        }

        class PostsViewHolder extends RecyclerView.ViewHolder {
            GradeRowItemBinding mBinding;
            Grade mGrade;
            public PostsViewHolder(GradeRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Grade grade) {
                mGrade = grade;
                mBinding.textViewCourseName.setText(grade.getCourseName());
                mBinding.textViewCourseNumber.setText(grade.getCourseNo());
                mBinding.textViewCourseHours.setText(grade.getCreditHr());

                /*  if(mPost.getCreated_by_uid().equals(currentUser.getUid())){*//*
                 *//*    mBinding.imageViewDelete.setVisibility(View.VISIBLE);*//*
                    mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                *//*} else {
                    mBinding.imageViewDelete.setVisibility(View.INVISIBLE);
                }*/

                    mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDeleteField(grade.courseId);
                            calculateGPA();
                            gradeAdapter.notifyDataSetChanged();
                        }
                    });

            }
        }

    }
    public void updateDeleteField(String course) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("courses")
                .document(currentUser.getUid()).collection("userCourse")

                .document(course)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("demo", "DocumentSnapshot successfully deleted!");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                            }
                        });
                        gradeAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("demo", "Error deleting document", e);
                    }
                });
    }

    GradeListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (GradeListener) context;
    }

    interface GradeListener{
        void addGradeFragment();
    }
}