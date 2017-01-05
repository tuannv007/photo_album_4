package com.framgia.photoeditor.ui.editimage;

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
import butterknife.OnClick;

/**
 * Created by tuanbg on 1/5/17.
 * <></>
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

    public class ControlViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_feature)
        ImageView mImage;
        @BindView(R.id.text_feature)
        TextView mTitle;

        public ControlViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int position) {
            Control controls = mListControl.get(position);
            mTitle.setText(controls.getTitle() != null ? controls.getTitle() : "");
            mImage.setImageResource(controls.getImage());
        }

        @OnClick(R.id.linear_feature)
        void clickFeature() {
            if (mOnItemClickListener == null) return;
            mOnItemClickListener.onItemClickListener(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}
