package ru.te4nick.mycompetes.ui.competitions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompetitionsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CompetitionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is competitions fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}