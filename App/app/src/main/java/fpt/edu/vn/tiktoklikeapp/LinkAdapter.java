package fpt.edu.vn.tiktoklikeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {
    private List<Link> links;
    private Context context;
    private OnLinkActionListener listener;

    public interface OnLinkActionListener {
        void onUpdate(Link link);
        void onDelete(int id);
    }

    public LinkAdapter(Context context, List<Link> links, OnLinkActionListener listener) {
        this.context = context;
        this.links = links;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_link, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Link link = links.get(position);
        holder.tvName.setText(link.getName());
        holder.tvUrl.setText(link.getUrl());
        holder.btnUpdate.setOnClickListener(v -> listener.onUpdate(link));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(link.getId()));
    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    public void updateLinks(List<Link> newLinks) {
        links.clear();
        links.addAll(newLinks);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUrl;
        Button btnUpdate, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}