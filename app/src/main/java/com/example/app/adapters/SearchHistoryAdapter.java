package com.example.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.models.SearchHistory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private List<SearchHistory> searchHistoryList;
    private final Context context;
    private OnItemClickListener listener;
    private OnFavoriteClickListener favoriteListener;
    private OnDeleteClickListener deleteListener;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi"));

    public interface OnItemClickListener { void onItemClick(SearchHistory item); }
    public interface OnFavoriteClickListener { void onFavoriteClick(SearchHistory item, boolean isFavorite); }
    public interface OnDeleteClickListener { void onDeleteClick(SearchHistory item); }

    public SearchHistoryAdapter(Context context, List<SearchHistory> list) {
        this.context = context;
        this.searchHistoryList = list;
    }

    public void setOnItemClickListener(OnItemClickListener l) { this.listener = l; }
    public void setOnFavoriteClickListener(OnFavoriteClickListener l) { this.favoriteListener = l; }
    public void setOnDeleteClickListener(OnDeleteClickListener l) { this.deleteListener = l; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchHistory item = searchHistoryList.get(position);
        holder.locationName.setText(item.getFullLocationName());
        holder.searchDate.setText("Tìm kiếm: " + dateFormat.format(item.getSearchDate()));
        holder.searchCount.setText("Lượt tìm: " + item.getSearchCount());

        holder.itemView.setOnClickListener(v -> { if (listener != null) listener.onItemClick(item); });
        holder.favoriteButton.setOnClickListener(v -> { if (favoriteListener != null) favoriteListener.onFavoriteClick(item, !item.isFavorite()); });
        holder.deleteButton.setOnClickListener(v -> { if (deleteListener != null) deleteListener.onDeleteClick(item); });
    }

    @Override
    public int getItemCount() { return searchHistoryList != null ? searchHistoryList.size() : 0; }

    public void updateData(List<SearchHistory> newList) { this.searchHistoryList = newList; notifyDataSetChanged(); }
    public void updateItem(SearchHistory item) { notifyDataSetChanged(); }
    public void removeItem(SearchHistory item) { searchHistoryList.remove(item); notifyDataSetChanged(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationName, searchDate, searchCount; ImageButton favoriteButton, deleteButton;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.tv_location_name);
            searchDate = itemView.findViewById(R.id.tv_search_date);
            searchCount = itemView.findViewById(R.id.tv_search_count);
            favoriteButton = itemView.findViewById(R.id.btn_favorite);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}


