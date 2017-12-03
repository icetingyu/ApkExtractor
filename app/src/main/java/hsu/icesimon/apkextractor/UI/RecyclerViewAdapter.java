package hsu.icesimon.apkextractor.UI;

/**
 * Created by simon on 2017/11/30.
 */

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hsu.icesimon.apkextractor.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AppInfo} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AppInfo> mValues;
    private Context mContext;
//    private int margin_value;
//    private int image_margin_value;
    private final OnListFragmentInteractionListener mListener;

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AppInfo item);
    }
    public RecyclerViewAdapter(List<AppInfo> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        mContext = parent.getContext();
//        margin_value = (int) mContext.getResources().getDimension(R.dimen.no_margin);
//        image_margin_value = (int) mContext.getResources().getDimension(R.dimen.image_margin);
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ImageTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageTextViewHolder) {
            final ImageTextViewHolder viewHolder = (ImageTextViewHolder) holder;
            viewHolder.mItem = mValues.get(position);

            viewHolder.mImageView.setImageDrawable(viewHolder.mItem.getIcon());
            viewHolder.mTitleView.setText(mValues.get(position).getAppname());
            viewHolder.mContentView.setText(mValues.get(position).getVersionName());

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(viewHolder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // User DiffUtil for updating data.
    // Traditional use notifyDataChanged will not trigger animation in recyclerView.
    // But from the test. Since like DiffUtil cost a little more memory compare to simply replace the whole dataset.

    public void updateData(List<AppInfo> items) {
        DiffCallBack diffCallback = new DiffCallBack(this.mValues, items);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
        mValues = items;
    }

    public void updateData2(List<AppInfo> items) {
        mValues = items;
    }

    public class ImageTextViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mImageView;
        public TextView mTitleView;
        public TextView mContentView;
        public AppInfo mItem;

        public ImageTextViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.imageView);
            mTitleView = view.findViewById(R.id.title);
            mContentView = view.findViewById(R.id.content);
        }

//        public void setupVisibility(boolean visible) {
//            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
//            if (visible) {
//                param.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, mContext.getResources().getDisplayMetrics());
//                param.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                itemView.setVisibility(View.VISIBLE);
//            } else {
//                itemView.setVisibility(View.GONE);
//                param.height = 0;
//                param.width = 0;
//            }
//            itemView.setLayoutParams(param);
//        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
