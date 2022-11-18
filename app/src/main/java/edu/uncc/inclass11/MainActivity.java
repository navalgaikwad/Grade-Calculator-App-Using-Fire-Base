package edu.uncc.inclass11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener,
        SignUpFragment.SignUpListener, GradesFragment.GradeListener, AddCourseFragment.CreateGradeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .commit();
    }

    @Override
    public void loginFromMain(String currentUser) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new GradesFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void login() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void redirectToPostFragment(String user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new GradesFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addGradeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AddCourseFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToGrade() {
        getSupportFragmentManager().popBackStack();
    }
}