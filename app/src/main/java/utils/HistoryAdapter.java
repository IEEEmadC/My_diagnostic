package utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.tr4android.recyclerviewslideitem.SwipeAdapter;
import com.tr4android.recyclerviewslideitem.SwipeConfiguration;

import org.dev4u.hv.my_diagnostic.R;

import java.util.ArrayList;

import db.MedicalHistory;

/**
 * Created by admin on 1/7/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> implements Filterable {

    Context mContext;
    public ArrayList<MedicalHistory> historyArrayList = new ArrayList<>();
    public ArrayList<MedicalHistory> mOriginalValues = new ArrayList<>();
    public ArrayList<MedicalHistory> selectedList =new ArrayList<>();
    String textSearch = "";

    public HistoryAdapter(Context mContext, ArrayList<MedicalHistory> historyArrayList,ArrayList<MedicalHistory> selectedList) {
        this.mContext = mContext;
        this.historyArrayList = historyArrayList;
        this.mOriginalValues = historyArrayList;
        this.selectedList = selectedList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        final MedicalHistory medicalHistory = historyArrayList.get(position);
        final SpannableStringBuilder sb = new SpannableStringBuilder(medicalHistory.getTitle());
        final ForegroundColorSpan fcs = new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color));
        sb.setSpan(fcs, 0, medicalHistory.getTitle().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        if (medicalHistory.getTitle().toLowerCase().startsWith(textSearch)) {
            final ForegroundColorSpan highlight = new ForegroundColorSpan(Color.CYAN);
            sb.setSpan(highlight, 0, textSearch.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        holder.title.setText(sb);
        holder.description.setText(medicalHistory.getDescription());
        holder.disease.setText(medicalHistory.getName_disease());
        holder.date.setText(medicalHistory.getDate_time());

        if(selectedList.contains(historyArrayList.get(position)))
            holder.listitem.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.listitem.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
    }

    @Override
    public long getItemId(int position) {
        if(position>-1) return Long.parseLong(historyArrayList.get(position).getId_medicalhistory());
        return -1;
    }




    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<MedicalHistory> FilteredArrList = new ArrayList<MedicalHistory>();
                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<MedicalHistory>(historyArrayList); // saves the original data in mOriginalValues
                }
                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                    textSearch = "";
                } else {
                    constraint = constraint.toString().toLowerCase();
                    textSearch = constraint.toString();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getTitle();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            MedicalHistory medicalHistory = new MedicalHistory(
                                    mOriginalValues.get(i).getId_medicalhistory(),
                                    mOriginalValues.get(i).getTitle(),
                                    mOriginalValues.get(i).getDescription(),
                                    mOriginalValues.get(i).getId_diseases(),
                                    mOriginalValues.get(i).getUsername(),
                                    mOriginalValues.get(i).getDate_time()
                            );
                            medicalHistory.setName_disease(mOriginalValues.get(i).getName_disease());
                            FilteredArrList.add(medicalHistory);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                historyArrayList = (ArrayList<MedicalHistory>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }
        };
        return filter;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder  {
    public TextView title;
    public TextView description;
    public TextView disease;
    public TextView date;
    public CardView listitem;

    public HistoryViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.lblTitleitemHistory);
        description = (TextView) view.findViewById(R.id.lblDescriptionItemHistory);
        disease = (TextView) view.findViewById(R.id.lblDiseaseItemHistory);
        date = (TextView)view.findViewById(R.id.lblDateItemHistory);
        listitem = (CardView) view.findViewById(R.id.item_history);
    }
}
}
