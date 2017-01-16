package com.framgia.photoeditor.ui.framgent.effect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.photoeditor.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nhahv on 1/13/2017.
 * <></>
 */
public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.EffectHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private EventEffect mEventEffect;
    private List<String> mListEffectName = new ArrayList<>();
    private List<Integer> mListEffectImage = new ArrayList<>();

    public EffectAdapter(Context context, EventEffect eventEffect) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mEventEffect = eventEffect;
    }

    public void setEffect(List<String> effects, List<Integer> images) {
        mListEffectImage.clear();
        mListEffectImage.addAll(images);
        mListEffectName.clear();
        mListEffectName.addAll(effects);
        notifyDataSetChanged();
    }

    @Override
    public EffectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EffectHolder(mInflater.inflate(R.layout.item_effect, parent, false));
    }

    @Override
    public void onBindViewHolder(EffectHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListEffectImage == null ? 0 : mListEffectImage.size();
    }

    public class EffectHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_effect)
        ImageView mImageEffect;
        @BindView(R.id.text_effect)
        TextView mTextEffect;

        public EffectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            Integer result = mListEffectImage.get(position);
            if (result != null) {
                mImageEffect.setImageResource(result);
            }
            String effectName = mListEffectName.get(position);
            mTextEffect.setText(effectName == null ? "" : effectName);
        }

        @OnClick(R.id.linear_effect)
        void pickEffect() {
            if (mEventEffect != null) mEventEffect.pickEffect(getAdapterPosition());
        }
    }

    interface EventEffect {
        void pickEffect(int position);
    }
}
