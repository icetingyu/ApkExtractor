package hsu.icesimon.apkextractor.UI;

/**
 * Created by simon on 2016/12/19.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import hsu.icesimon.apkextractor.Log;
import hsu.icesimon.apkextractor.MainActivity;
import hsu.icesimon.apkextractor.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AppInfo} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<AppInfo> mValues;
//    private final OnListFragmentInteractionListener mListener;
    private final int IMAGE_WITH_TEXT = 0;
    private final int IMAGE_ONLY = 1;
    private Context mContext;
    private int margin_value;
    private int image_margin_value;
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
        margin_value = (int) mContext.getResources().getDimension(R.dimen.no_margin);
        image_margin_value = (int) mContext.getResources().getDimension(R.dimen.image_margin);
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ImageTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScalingUtils.ScaleType scaleType;

        if (holder instanceof ImageTextViewHolder) {
            final ImageTextViewHolder viewHolder = (ImageTextViewHolder) holder;
//            ImageRequest request;
            viewHolder.mItem = mValues.get(position);
            if(MainActivity.showAlsoSystemApp) {
                viewHolder.setupVisibility(true);
            } else {
                if (viewHolder.mItem.getSystemApp()) {
                    viewHolder.setupVisibility(false);
                } else {
                    viewHolder.setupVisibility(true);
                }
            }

            viewHolder.mImageView.setImageDrawable(viewHolder.mItem.getIcon());

//            if (mValues.get(position).getIcon() != null) {
//                Uri uri = Uri.parse(mValues.get(position).getIcon());
//                request = ImageRequest.ImageRequestBuilder..newBuilderWithSource(uri).build();
//                scaleType = ScalingUtils.ScaleType.CENTER_CROP;
//            } else {
//                request = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.image_unavailable).build();
//                scaleType = ScalingUtils.ScaleType.FIT_CENTER;
//            }
//            viewHolder.itemIndicator.setText(String.valueOf(position + 1));
//
//            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
//            GenericDraweeHierarchy hierarchy = builder
//                    .setActualImageScaleType(scaleType)
//                    .build();
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setImageRequest(request)
//                    .setTapToRetryEnabled(true)
//                    .setOldController(viewHolder.mImageView.getController())
//                    .build();
//            viewHolder.mImageView.setController(controller);
//            viewHolder.mImageView.setHierarchy(hierarchy);

            viewHolder.mTitleView.setText(mValues.get(position).getAppname());
            viewHolder.mContentView.setText(mValues.get(position).getVersionName());

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(viewHolder.mItem);
//                        EventBus.getDefault().post(viewHolder.mItem);
                    }
                }
            });
        }
    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mValues.get(position).getType();
//    }


    @Override
    public int getItemCount() {
        int count = 0;

        boolean status = MainActivity.showAlsoSystemApp;
        if (status) {
            count = mValues.size();
        } else {
            for (int i = 0 ; i < mValues.size(); i++) {
                if (!mValues.get(i).getSystemApp()) {
                    count++;
                }
            }
        }
        Log.d("getItemCount: "+count);
        return count;
    }

    public class ImageTextViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitleView;
        public final TextView mContentView;
        public AppInfo mItem;

        public ImageTextViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        public void setupVisibility(boolean visible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (visible) {
                param.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, mContext.getResources().getDisplayMetrics());
                param.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
