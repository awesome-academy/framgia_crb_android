package framgia.vn.framgiacrb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import framgia.vn.framgiacrb.adapter.CustomRecyclerAdapter.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.data.Data;

/**
 * Created by lethuy on 04/07/2016.
 */
public class CustomRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Data> listData = new ArrayList<Data>();

    public CustomRecyclerAdapter(List<Data> listData) {
        this.listData = listData;
    }

    public void updateList(List<Data> data) {
        listData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_search, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        viewHolder.mTvName.setText(listData.get(position).getName());
    }

    public void swap(ArrayList<Data> datas){
        listData.clear();
        listData.addAll(datas);
        notifyDataSetChanged();
    }

    public void addItem(int position, Data data) {
        listData.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * ViewHolder for item view of list
     * */

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private TextView mTvName;
        private ImageButton mBtnDelete;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mBtnDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);

            // set listener for button delete
            mBtnDelete.setOnClickListener(this);
        }

        // remove item when click button delete
        @Override
        public void onClick(View v) {
            removeItem(getAdapterPosition());
        }
    }

}
