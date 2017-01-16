package com.framgia.photoeditor.ui.mergeimage;

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
 * Created by tuanbg on 1/16/17.
 */
public class MergeImageAdapter extends RecyclerView.Adapter<MergeImageAdapter.MergeHolder> {
    private List<Control> mItemList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public MergeImageAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mOnItemClickListener = onItemClickListener;
    }

    public void setListControl(List<Control> controls) {
        mItemList.clear();
        mItemList.addAll(controls);
        notifyDataSetChanged();
    }

    @Override
    public MergeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MergeHolder(mInflater.inflate(R.layout.item_control_image, parent, false));
    }

    @Override
    public void onBindViewHolder(MergeHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public class MergeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_feature)
        ImageView mImageControl;
        @BindView(R.id.text_feature)
        TextView mTextControl;

        public MergeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int position) {
            Control items = mItemList.get(position);
            mTextControl.setText(items.getTitle() != null ? items.getTitle() : "");
            mImageControl.setImageResource(items.getImage());
        }

        @OnClick(R.id.linear_feature)
        void clickItem() {
            if (mOnItemClickListener == null) return;
            mOnItemClickListener.onClickImage(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onClickImage(int position);
    }
}
