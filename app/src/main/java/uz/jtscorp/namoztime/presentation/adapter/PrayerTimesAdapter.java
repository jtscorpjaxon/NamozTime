package uz.jtscorp.namoztime.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import uz.jtscorp.namoztime.data.entity.PrayerTime;
import uz.jtscorp.namoztime.databinding.ItemPrayerTimeBinding;

public class PrayerTimesAdapter extends ListAdapter<PrayerTime, PrayerTimesAdapter.ViewHolder> {

    public PrayerTimesAdapter() {
        super(new DiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPrayerTimeBinding binding = ItemPrayerTimeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPrayerTimeBinding binding;

        ViewHolder(ItemPrayerTimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PrayerTime prayerTime) {
            binding.tvPrayerName.setText(prayerTime.getName());
            binding.tvPrayerTime.setText(prayerTime.getTime());
            binding.tvPrayerDate.setText(prayerTime.getDate());
        }
    }

    private static class DiffCallback extends DiffUtil.ItemCallback<PrayerTime> {
        @Override
        public boolean areItemsTheSame(@NonNull PrayerTime oldItem, @NonNull PrayerTime newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PrayerTime oldItem, @NonNull PrayerTime newItem) {
            return oldItem.equals(newItem);
        }
    }
} 