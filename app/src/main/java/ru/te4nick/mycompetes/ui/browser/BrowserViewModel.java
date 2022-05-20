package ru.te4nick.mycompetes.ui.browser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BrowserViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BrowserViewModel() {
        mText = new MutableLiveData<>();
        // mText.setValue("This is browser fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}