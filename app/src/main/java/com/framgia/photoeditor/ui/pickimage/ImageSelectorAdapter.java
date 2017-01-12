package com.framgia.photoeditor.ui.pickimage;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.framgia.photoeditor.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_MULTIPLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_SINGLE_IMAGE;

/**
 * Created by Nhahv on 1/6/2017.
 * <></>
 */
public class ImageSelectorAdapter
    extends RecyclerView.Adapter<ImageSelectorAdapter.SelectorImageHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private OnPickImageSelected mOnPickImageSelected;
    private ArrayList<String> mListImage = new ArrayList<>();
    private ArrayList<String> mListImageSelected = new ArrayList<>();
    private int mTypePickImage;

    public ImageSelectorAdapter(Context context, OnPickImageSelected onPickImageSelected,
                                int typePickImage) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mOnPickImageSelected = onPickImageSelected;
        mTypePickImage = typePickImage;
    }

    public void setListImage(List<String> pathImages) {
        mListImage.clear();
        mListImage.addAll(pathImages);
        notifyDataSetChanged();
    }

    @Override
    public SelectorImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectorImageHolder(
            mInflater.inflate(R.layout.item_selector_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectorImageHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListImage == null ? 0 : mListImage.size();
    }

    public ArrayList<String> getListImageSelected() {
        return mListImageSelected;
    }

    public class SelectorImageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_picture)
        ImageView mImagePicture;
        @BindView(R.id.image_checked)
        ImageView mImageChecked;

        public SelectorImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mTypePickImage == DATA_PICK_SINGLE_IMAGE)
                mImageChecked.setVisibility(View.GONE);
        }

        private void bind(int position) {
            String pathImage = mListImage.get(position);
            if (pathImage == null) return;
            Glide.with(mContext)
                .load(pathImage)
                .error(R.drawable.ic_splash_screen)
                .centerCrop()
                .into(mImagePicture);
            mImageChecked.setSelected(isSelected(pathImage));
            selectImageChecked(mImageChecked.isSelected());
        }

        @OnClick(R.id.image_checked)
        void clickPickImage() {
            String pathImage = mListImage.get(getAdapterPosition());
            mImageChecked.setSelected(!mImageChecked.isSelected());
            selectImageChecked(mImageChecked.isSelected());
            changeCheckboxState(pathImage, mImageChecked.isSelected());
            if (mOnPickImageSelected != null) {
                mOnPickImageSelected.onImageSelected(mListImageSelected);
            }
        }

        @OnClick(R.id.image_picture)
        void clickPreviewImage() {
            String pathImage = mListImage.get(getAdapterPosition());
            switch (mTypePickImage) {
                case DATA_PICK_SINGLE_IMAGE:
                    if (mOnPickImageSelected != null) {
                        mOnPickImageSelected.pickSingleImage(pathImage);
                    }
                    break;
                case DATA_PICK_MULTIPLE_IMAGE:
                    if (mOnPickImageSelected == null) return;
                    mOnPickImageSelected.pickPreviewImage(mListImage, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }

        private void selectImageChecked(boolean isChecked) {
            int res = isChecked ? R.drawable.ic_checked_blue : R.drawable.ic_no_checked;
            mImageChecked.setImageResource(res);
            if (isChecked)
                mImagePicture.setBackground(
                    mContext.getResources().getDrawable(R.drawable.ic_boder_image_check));
            else mImagePicture.setBackgroundColor(Color.TRANSPARENT);
        }

        private boolean isSelected(String pathImage) {
            for (String media : mListImageSelected) {
                if (media.equals(pathImage)) return true;
            }
            return false;
        }

        private void changeCheckboxState(String pathImage, boolean isChecked) {
            if (isChecked) mListImageSelected.add(pathImage);
            else {
                for (String item : mListImageSelected) {
                    if (item.equals(pathImage)) {
                        mListImageSelected.remove(item);
                        break;
                    }
                }
            }
        }
    }

    public interface OnPickImageSelected {
        void onImageSelected(List<String> images);
        void pickSingleImage(String pathImage);
        void pickPreviewImage(ArrayList<String> pathImages, int position);
    }
}
