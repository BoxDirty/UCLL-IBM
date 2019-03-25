package be.cosci.ibm.ucllwatson.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.cosci.ibm.ucllwatson.R;
import be.cosci.ibm.ucllwatson.utils.item.ReceiptItem;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Petr on 28-Mar-18.
 */
public class RecipeRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ReceiptItem> recipeItemList;

    public RecipeRecycleViewAdapter(Context context, List<ReceiptItem> recipeItemList) {
        this.context = context;
        this.recipeItemList = recipeItemList;
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photo;
        TextView name;

        RecipeViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.recipe_photo);
            name = itemView.findViewById(R.id.recipe_name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View libraryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe_items, parent, false);
        return new RecipeViewHolder(libraryView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final RecipeViewHolder viewHolder = (RecipeViewHolder) holder;
        final ReceiptItem recipeItem = recipeItemList.get(position);
        viewHolder.name.setText(recipeItem.getTitle());

        Picasso.get().load(recipeItem.getImageURL()).into(viewHolder.photo);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeItem.getSourceURL()));
                context.startActivity(browserIntent);
            }
        });
    }

    /**
     * @param data
     */
    public void setData(List<ReceiptItem> data) {
        this.recipeItemList.clear();
        this.recipeItemList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipeItemList.size();
    }
}