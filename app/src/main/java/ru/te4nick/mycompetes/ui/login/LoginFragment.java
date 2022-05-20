package ru.te4nick.mycompetes.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import ru.te4nick.mycompetes.MainActivity;
import ru.te4nick.mycompetes.R;
import ru.te4nick.mycompetes.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private TextView login_button, register_button;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FragmentLoginBinding binding;
    private LoginHandler loginHandler = new LoginHandler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextEmail = binding.loginEmail;
        editTextPassword = (EditText) binding.loginPassword;
        progressBar = (ProgressBar) binding.loginProgressBar;
        login_button = (Button) binding.buttonLogin;
        register_button = binding.gotoRegister;
        login_button.setOnClickListener(view -> authHandler());
        register_button.setOnClickListener(view -> {Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_register);});
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void authHandler() {
        progressBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        switch (loginHandler.loginWithEmailAndPassword(email, password)) {
            case LoginHandler.EMPTY_EMAIL:
                editTextEmail.setError(getString(R.string.require_email));
                editTextEmail.requestFocus();
                break;
            case LoginHandler.EMPTY_PASSWORD:
                editTextPassword.setError(getString(R.string.require_password));
                editTextPassword.requestFocus();
                break;
            case LoginHandler.INVALID_EMAIL:
                editTextEmail.setError(getString(R.string.invalid_email));
                editTextEmail.requestFocus();
                break;
            case LoginHandler.SHORT_PASSWORD:
                editTextPassword.setError(getString(R.string.invalid_password));
                editTextPassword.requestFocus();
                break;
            case LoginHandler.UNCONFIRMED_EMAIL:
                Toast.makeText(getActivity(), getString(R.string.verify_email), Toast.LENGTH_LONG).show();
                break;
            case LoginHandler.DATABASE_ERROR:
                Toast.makeText(getActivity(), getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
                break;
            case LoginHandler.ERROR:
                Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                break;
            case LoginHandler.SUCCESS:
                Toast.makeText(getActivity(), getString(R.string.welcome), Toast.LENGTH_LONG).show();
//                View nav_header_main = getView().findViewById(R.id.nav_header_main);
//                TextView user_email = (TextView) getView().findViewById(R.id.userEmail);
//                user_email.setText(email);
                ((ImageView) getActivity().findViewById(R.id.logout)).setVisibility(View.VISIBLE);
                ImageView logoutImageView = (ImageView) getActivity().findViewById(R.id.logout);
                // handling user logout
                logoutImageView.setOnClickListener(view -> {
                    loginHandler.signOut();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                });
                ((TextView) getActivity().findViewById(R.id.userEmail)).setText(email);
                ((TextView) getActivity().findViewById(R.id.userName)).setText(loginHandler.getName());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_home);
                break;

        }
        progressBar.setVisibility(View.GONE);
    }
}

