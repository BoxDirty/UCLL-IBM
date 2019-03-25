package be.cosci.ibm.ucllwatson.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.cosci.ibm.ucllwatson.R;

/**
 * Created by Petr on 28-Mar-18.
 */

public class AbouUsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> nameList;

    public AbouUsAdapter(Context context, List<String> nameList) {
        this.context = context;
        this.nameList = nameList;
    }

    private class AboutUsViewHolder extends RecyclerView.ViewHolder {

        ImageView flag;
        TextView name;

        AboutUsViewHolder(View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flag);
            name = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View subjectView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_about_us, parent, false);
        return new AbouUsAdapter.AboutUsViewHolder(subjectView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final AbouUsAdapter.AboutUsViewHolder viewHolder = (AbouUsAdapter.AboutUsViewHolder) holder;
        if (position == 1) {
            viewHolder.flag.setImageResource(R.drawable.lith_flag);
        } else if (position == 3) {
            viewHolder.flag.setImageResource(R.drawable.cz_flag);
        } else {
            viewHolder.flag.setImageResource(R.drawable.be_flag);
        }
        viewHolder.name.setText(nameList.get(position));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }
}