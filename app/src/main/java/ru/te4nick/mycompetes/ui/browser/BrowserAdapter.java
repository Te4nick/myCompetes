package ru.te4nick.mycompetes.ui.browser;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.te4nick.mycompetes.R;

public class BrowserAdapter extends RecyclerView.Adapter<BrowserAdapter.TableViewHolder> {

    private List<Table> tableList = new ArrayList<>();

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TableViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView hostTextView;
        private TextView membersTextView;
        private TextView descriptionTextView;


        public TableViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.name_text_view);
            hostTextView = view.findViewById(R.id.host_text_view);
            membersTextView = view.findViewById(R.id.members_text_view);
            descriptionTextView = view.findViewById(R.id.description_text_view);
        }
    }
}
