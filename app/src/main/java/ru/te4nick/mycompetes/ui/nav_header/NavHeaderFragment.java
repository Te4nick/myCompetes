package ru.te4nick.mycompetes.ui.nav_header;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.te4nick.mycompetes.R;

public class NavHeaderFragment extends View {

    public NavHeaderFragment(Context context) {
        super(context);

    }

    public void setNavHeader(String name, String email) {
        ((TextView) findViewById(R.id.userEmail)).setText(email);
        ((TextView) findViewById(R.id.userName)).setText(name);
    }
}