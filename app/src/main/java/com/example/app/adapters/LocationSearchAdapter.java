package com.example.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app.R;
import com.example.app.models.LocationSearchResult;
import com.example.app.network.ApiClient;
import com.example.app.network.WeatherApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSearchAdapter extends ArrayAdapter<LocationSearchResult> {

    private List<LocationSearchResult> searchResults;
    private final LayoutInflater inflater;
    private final WeatherApiService weatherApiService;

    public LocationSearchAdapter(@NonNull Context context, List<LocationSearchResult> results) {
        super(context, R.layout.item_location_search, results);
        this.searchResults = results;
        this.inflater = LayoutInflater.from(context);
        this.weatherApiService = ApiClient.getWeatherApiService();
    }

    @Override
    public int getCount() {
        return searchResults.size();
    }

    @Nullable
    @Override
    public LocationSearchResult getItem(int position) {
        return searchResults.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_location_search, parent, false);
            holder = new ViewHolder();
            holder.tvLocationName = convertView.findViewById(R.id.tvLocationName);
            holder.tvLocationDetails = convertView.findViewById(R.id.tvLocationDetails);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LocationSearchResult location = getItem(position);
        if (location != null) {
            holder.tvLocationName.setText(location.getName());
            String details = "";
            if (location.getRegion() != null && !location.getRegion().isEmpty() && !location.getRegion().equals(location.getName())) {
                details = location.getRegion();
            }
            if (location.getCountry() != null && !location.getCountry().isEmpty()) {
                if (!details.isEmpty()) details += ", ";
                details += location.getCountry();
            }
            holder.tvLocationDetails.setText(details);
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() < 2) {
                    results.values = new ArrayList<LocationSearchResult>();
                    results.count = 0;
                } else {
                    searchLocationsFromAPI(constraint.toString());
                    results.values = searchResults;
                    results.count = searchResults.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Object values = results.values;
                if (values instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<LocationSearchResult> list = (List<LocationSearchResult>) values;
                    searchResults = list;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private void searchLocationsFromAPI(String query) {
        Call<LocationSearchResult[]> call = weatherApiService.searchLocations(ApiClient.API_KEY, query);
        call.enqueue(new Callback<LocationSearchResult[]>() {
            @Override
            public void onResponse(@NonNull Call<LocationSearchResult[]> call, @NonNull Response<LocationSearchResult[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LocationSearchResult> results = new ArrayList<>();
                    for (LocationSearchResult location : response.body()) results.add(location);
                    searchResults = results;
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LocationSearchResult[]> call, @NonNull Throwable t) {
                // swallow
            }
        });
    }

    static class ViewHolder {
        TextView tvLocationName;
        TextView tvLocationDetails;
    }
}


