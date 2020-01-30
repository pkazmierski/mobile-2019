package pl.mobile.kandydatpl.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.mobile.kandydatpl.models.File;
import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.models.Question;

import java.util.ArrayList;

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FilesRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "FilesRVAdapter";

    private Context ctx;
    private ArrayList<File> files = new ArrayList<>();

    public FilesRecyclerViewAdapter(Context ctx, ArrayList<File> files) {
        this.ctx = ctx;
        this.files = files;
    }

    @NonNull
    @Override
    //responsible for inflating the view
    //can be always the same (beside the layout name)
    public FilesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_file_item, parent, false);
        FilesRecyclerViewAdapter.ViewHolder holder = new FilesRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    //change what layout look like
    public void onBindViewHolder(@NonNull FilesRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.fileNameTxt.setText(files.get(position).getName());
        holder.fileCard.setOnClickListener(view -> {
            files.get(position).downloadFile(view.getContext());
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //holds individual entries in memory
        TextView fileNameTxt;
        CardView fileCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTxt = itemView.findViewById(R.id.fileNameTxt);
            fileCard = itemView.findViewById(R.id.fileCard);
        }
    }

    public void filterList(ArrayList<File> filteredFiles) {
        files = filteredFiles;
        notifyDataSetChanged();
    }
}
