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

    public ControlImageAdapter(Context context, List<Control> listControl) {
        mListControl = listControl;
        mContext = context;
    }

    @Override
    public ControlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_control_image, parent, false);
        return new ControlViewHolder(itemView);
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
        @BindView(R.id.image_item_control)
        ImageView mImage;
        @BindView(R.id.text_item_control)
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
    }
}
