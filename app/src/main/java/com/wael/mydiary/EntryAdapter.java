package com.wael.mydiary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ArticleViewHolder> {

    private static final String TAG = EntryAdapter.class.getSimpleName();

    private String[] mDataset = {"Wael", "Mohamed", "Khalil"};

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public EntryAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.entry_row_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d(TAG, "#" + position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView image;
        TextView title;
        TextView text;
        TextView day;
        TextView dayNumber;


        public ArticleViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image_entry_item);
            title = (TextView) itemView.findViewById(R.id.title_entry_item);
            text = (TextView) itemView.findViewById(R.id.text_entry_item);
            day = (TextView) itemView.findViewById(R.id.day_entry_item);
            dayNumber = (TextView) itemView.findViewById(R.id.day_number_entry_item);

            itemView.setOnClickListener(this);
        }


        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
