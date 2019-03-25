package be.cosci.ibm.ucllwatson.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import be.cosci.ibm.ucllwatson.R;
import be.cosci.ibm.ucllwatson.activity.IngredientsDetail;
import be.cosci.ibm.ucllwatson.activity.SearchActivity;
import be.cosci.ibm.ucllwatson.db.PhotosRepository;
import be.cosci.ibm.ucllwatson.db.item.PhotoItem;

/**
 * Created by Petr on 28-Mar-18.
 */
public class IngredientRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<PhotoItem> ingredientItemList;
    private PhotosRepository photosRepository;

    public IngredientRecycleViewAdapter(Activity activity, List<PhotoItem> ingredientItemList) {
        this.activity = activity;
        this.ingredientItemList = ingredientItemList;
        this.photosRepository = new PhotosRepository(activity);
    }

    public PhotosRepository getPhotosRepository() {
        return photosRepository;
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name;
        Button removeButton;

        HistoryViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.ingredient_photo);
            name = itemView.findViewById(R.id.ingredient_name);
            removeButton = itemView.findViewById(R.id.ingredient_delete);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View libraryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredient_items, parent, false);
        return new HistoryViewHolder(libraryView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final HistoryViewHolder viewHolder = (HistoryViewHolder) holder;

        final PhotoItem photoItem = ingredientItemList.get(position);

        viewHolder.name.setText(photoItem.getIngredientName());

        Picasso.get()
                .load(new File(new PhotosRepository(activity).getItem(photoItem.getId()).getPath()))
                .rotate(90f)
                .into(viewHolder.photo);

        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientItemList.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
                notifyItemRangeChanged((viewHolder.getAdapterPosition()), ingredientItemList.size());
                photosRepository.remove(photoItem.getId());
                File file = new File(photoItem.getPath());
                file.delete();
                if (ingredientItemList.size() == 0) {
                    ((SearchActivity) activity).loadRecipes();
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, IngredientsDetail.class);
                intent.putExtra(IngredientsDetail.INGREDIENT_INTENT, photoItem.getId());
                activity.startActivity(intent);
            }
        });

    }

    /**
     * @param photoItem
     */
    public void updateIngredient(final PhotoItem photoItem) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPhotosRepository().update(photoItem);
                ingredientItemList.clear();
                ingredientItemList.addAll(getPhotosRepository().getAllItems());
                ((SearchActivity) activity).refreshRecipes();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientItemList.size();
    }
}