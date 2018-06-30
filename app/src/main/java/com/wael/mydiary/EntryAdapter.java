package com.wael.mydiary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wael.mydiary.Database.Entry;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.List;


public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ArticleViewHolder> {

    private static final String TAG = EntryAdapter.class.getSimpleName();
    private List<Entry> mEntries;
    private Context mContext;
    public EntryAdapter(List<Entry> mEntries, Context context, ListItemClickListener mItemClickListener) {
        this.mEntries = mEntries;
        this.mContext = context;
        this.mItemClickListener = mItemClickListener;
    }

    final private ListItemClickListener mItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
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

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d(TAG, "#" + position);
        // Getting Day of week
        Calendar cal = Calendar.getInstance();
        cal.setTime(mEntries.get(position).getCreatedAt());

        holder.mEntryId = mEntries.get(position).getId();
        holder.text.setText(mEntries.get(position).getText());
        holder.title.setText(mEntries.get(position).getTitle());
        holder.dayNumber.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        DayOfWeek dayOfWeek = DayOfWeek.of(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) + 1);
        holder.day.setText(String.valueOf(dayOfWeek));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
       // ImageView image;
        TextView title;
        TextView text;
        TextView day;
        TextView dayNumber;
        LinearLayout layout_row;
        int mEntryId = -1;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            int mEntryId = -1;

//            image = (ImageView) itemView.findViewById(R.id.image_entry_item);
            title = (TextView) itemView.findViewById(R.id.title_entry_item);
            text = (TextView) itemView.findViewById(R.id.text_entry_item);
            day = (TextView) itemView.findViewById(R.id.day_entry_item);
            dayNumber = (TextView) itemView.findViewById(R.id.day_number_entry_item);
            layout_row = (LinearLayout) itemView.findViewById(R.id.layout_row);
            itemView.setOnClickListener(this);
        }


        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mItemClickListener.onListItemClick(this.mEntryId);
        }
    }
    public void setEntries(List<Entry> Entries) {
        this.mEntries = Entries;
        notifyDataSetChanged();
    }

}
