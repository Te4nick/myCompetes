package ru.te4nick.mycompetes.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import ru.te4nick.mycompetes.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private TextView login_button;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private AppBarConfiguration mAppBarConfiguration;
    private  FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        editTextEmail = binding.loginEmail;
        editTextPassword = (EditText) binding.loginPassword;
        progressBar = (ProgressBar) binding.loginProgressBar;
        login_button = (Button) binding.buttonLogin;
        login_button.setOnClickListener(view -> authHandler() );
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Todo: convert plain strings to strings.xml file
    private void authHandler() {
        progressBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        // trying to log in w/ provided credentials
        switch (new LoginHandler().loginWithCredentials(email, password)) {
            case LoginHandler.SUCCESS:
                Toast.makeText(getActivity(), "Logged in successfully", Toast.LENGTH_LONG).show();
                break;
            case LoginHandler.EMPTY_EMAIL:
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                break;
            case LoginHandler.EMPTY_PASSWORD:
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
                break;
            case LoginHandler.INVALID_EMAIL:
                editTextEmail.setError("Provide valid email");
                editTextEmail.requestFocus();
                break;
            case LoginHandler.SHORT_PASSWORD:
                editTextPassword.setError("Password length should be 6 characters");
                editTextPassword.requestFocus();
                break;
            case LoginHandler.DATABASE_ERROR:
                Toast.makeText(getActivity(), "Failed to register! Please, try again!", Toast.LENGTH_LONG).show();
                break;
        }
        // Todo:
        // Trying to register w/ provided credentials
        switch (new LoginHandler().registerWithCredentials(email, password)) {
            case LoginHandler.SUCCESS:
                Toast.makeText(getActivity(), "User has been registered successfully", Toast.LENGTH_LONG).show();
                break;
            case LoginHandler.DATABASE_ERROR:
                Toast.makeText(getActivity(), "Failed to register! Please, try again!", Toast.LENGTH_LONG).show();
                break;
            case LoginHandler.EMPTY_EMAIL:
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                break;
            case LoginHandler.EMPTY_PASSWORD:
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
                break;
            case LoginHandler.INVALID_EMAIL:
                editTextEmail.setError("Provide valid email");
                editTextEmail.requestFocus();
                break;
            case LoginHandler.SHORT_PASSWORD:
                editTextPassword.setError("Password length should be 6 characters");
                editTextPassword.requestFocus();
                break;
        }
        progressBar.setVisibility(View.GONE);
    }
}

