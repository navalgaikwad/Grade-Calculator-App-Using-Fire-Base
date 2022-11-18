package edu.uncc.inclass11;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.uncc.inclass11.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;



    String username;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid email!", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid password!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth=FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Log.d("Demo", "OnComplete Login Successfull");
                                        FirebaseUser currentUser = mAuth.getCurrentUser();

                                        /* existsUser();*/
                                       // getUsername(email,password);
                                        currentUser.getUid();
                                        mListener.loginFromMain("username".toString());
                                        /*mListener.loginFromMain(username.toString());*/
                                    }else {
                                        Toast.makeText(getActivity(), "Invalid Username/Password!", Toast.LENGTH_SHORT).show();
                                        Log.d("Demo", "OnComplete Error");
                                        Log.d("Demo", "OnComplete Error"+ task.getException().getMessage());

                                    }

                                }
                            });



                }
            }
        });

        binding.buttonCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.createNewAccount();
            }
        });

        getActivity().setTitle(R.string.login_label);
    }


    void getUsername(String email, String password) {

        FirebaseFirestore db=FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Demo", document.getId() + " => " + document.getData());

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (document.getData().get("email").toString().equals(email) &&
                                                document.getData().get("password").toString().equals(password)){
                                            Users user=new Users();
                                            username=document.getData().get("fullName").toString();
                                            user.setFullName(username);
                                            /* user.setCreated_by_uid();*/
                                            mListener.loginFromMain(username.toString());
                                        }

                                    }
                                });


                            }


                        } else {
                            Log.w("Demo", "Error getting documents.", task.getException());
                            Toast.makeText(getActivity(), "Unable to get posts", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    LoginListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (LoginListener) context;
    }

    interface LoginListener {
        void createNewAccount();
        void loginFromMain(String currentUser);
    }
}