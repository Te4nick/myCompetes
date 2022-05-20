package ru.te4nick.mycompetes.ui.competitions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.te4nick.mycompetes.databinding.FragmentCompetitionsBinding;
import ru.te4nick.mycompetes.ui.browser.BrowserViewModel;

public class CompetitionsFragment extends Fragment {

    private FragmentCompetitionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CompetitionsViewModel galleryViewModel =
                new ViewModelProvider(this).get(CompetitionsViewModel.class);

        binding = FragmentCompetitionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCompetitions;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}