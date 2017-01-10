package com.framgia.photoeditor.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.Control;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tuanbg on 1/5/17.
 */
public class ControlImageAdapter
    extends RecyclerView.Adapter<ControlImageAdapter.ControlViewHolder> {
    private List<Control> mListControl = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private LayoutInflater mInflater;

    public ControlImageAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mOnItemClickListener = onItemClickListener;
    }

    public void setListControl(List<Control> controls) {
        mListControl.clear();
        mListControl.addAll(controls);
        notifyDataSetChanged();
    }

    @Override
    public ControlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ControlViewHolder(mInflater.inflate(R.layout.item_control_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ControlViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListControl == null ? 0 : mListControl.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    public class ControlViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image_item_control)
        ImageView mImage;
        @BindView(R.id.text_item_control)
        TextView mTitle;

        public ControlViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mImage.setOnClickListener(this);
        }

        private void bind(int position) {
            Control controls = mListControl.get(position);
            mTitle.setText(controls.getTitle() != null ? controls.getTitle() : "");
            mImage.setImageResource(controls.getImage());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener == null) return;
            mOnItemClickListener.onItemClickListener(v, getAdapterPosition());
        }
    }
}
