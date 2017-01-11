package com.framgia.photoeditor.ui.imagefolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.LocalImageFolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nhahv on 1/5/2017.
 * <></>
 */
public class ImageFolderAdapter extends RecyclerView.Adapter<ImageFolderAdapter.ImageFolderHolder> {
    private Context mContext;
    private List<LocalImageFolder> mListImageFolder = new ArrayList<>();
    private LayoutInflater mInflater;
    private EventImageFolder mEventImageFolder;

    public ImageFolderAdapter(Context context, EventImageFolder eventImageFolder) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mEventImageFolder = eventImageFolder;
    }

    public void setListImageFolder(List<LocalImageFolder> folders) {
        mListImageFolder.clear();
        mListImageFolder.addAll(folders);
        notifyDataSetChanged();
    }

    @Override
    public ImageFolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageFolderHolder(mInflater.inflate(R.layout.item_image_folder, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageFolderHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListImageFolder.size();
    }

    public class ImageFolderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_picture)
        ImageView mImagePicture;
        @BindView(R.id.text_name_folder)
        TextView mTextFolderName;
        @BindView(R.id.text_number_image)
        TextView mTextNumberImage;

        public ImageFolderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int position) {
            LocalImageFolder folder = mListImageFolder.get(position);
            if (folder == null) return;
            Glide.with(mContext)
                .load(folder.getListImageOfFolder().get(0))
                .into(mImagePicture);
            mTextFolderName
                .setText(folder.getFolderName() != null ? folder.getFolderName() : "");
            mTextNumberImage.setText(
                mContext.getString(R.string.text_number, folder.getListImageOfFolder().size()));
        }

        @OnClick(R.id.linear_folder)
        void pickImageFolder() {
            LocalImageFolder folder = mListImageFolder.get(getAdapterPosition());
            if (mEventImageFolder != null && folder != null) {
                mEventImageFolder.pickImageFolder(folder);
            }
        }
    }

    public interface EventImageFolder {
        void pickImageFolder(LocalImageFolder imageFolder);
    }
}
