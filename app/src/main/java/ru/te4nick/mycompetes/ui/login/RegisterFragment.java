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
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import ru.te4nick.mycompetes.R;
import ru.te4nick.mycompetes.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {

        private TextView register_button;
        private EditText editTextEmail, editTextPassword, editTextName;
        private ProgressBar progressBar;
        private FragmentRegisterBinding binding;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            binding = FragmentRegisterBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            editTextEmail = (EditText) binding.registerEmail;
            editTextPassword = (EditText) binding.registerPassword;
            editTextName = (EditText) binding.registerName;
            progressBar = (ProgressBar) binding.registerProgressBar;
            register_button = (Button) binding.buttonRegister;
            register_button.setOnClickListener(view -> authHandler());
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
            String name = editTextName.getText().toString().trim();
            if (name.length() < 3) {
                editTextName.setError(getString(R.string.require_name));
                editTextName.requestFocus();
            }
            switch (new LoginHandler().registerWithEmailAndPassword(email, password, name)) {
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
                    Toast.makeText(getActivity(), getString(R.string.verify_email), Toast.LENGTH_LONG).show();
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_login);
                    break;

            }
            progressBar.setVisibility(View.GONE);
        }
    }
