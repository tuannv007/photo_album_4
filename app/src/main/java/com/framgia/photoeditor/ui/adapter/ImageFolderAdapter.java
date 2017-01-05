package com.framgia.photoeditor.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.LocalImageFolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nhahv on 1/5/2017.
 * <></>
 */
public class ImageFolderAdapter extends RecyclerView.Adapter<ImageFolderAdapter.ImageFolderHolder> {
    private Context mContext;
    private List<LocalImageFolder> mListImageFolder;
    private LayoutInflater mInflater;

    public ImageFolderAdapter(Context context, List<LocalImageFolder> folders) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListImageFolder = folders;
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
        @BindView(R.id.image_checked)
        ImageView mImageChecked;

        public ImageFolderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int position) {
            LocalImageFolder folder = mListImageFolder.get(position);
            if (folder != null) {
                Glide.with(mContext)
                    .load(folder.getListImageOfFolder().get(0).getPath())
                    .thumbnail(1f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImagePicture);
            }
        }
    }
}
