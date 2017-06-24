package utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.dev4u.hv.my_diagnostic.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import db.Disease;
import db.Symptom;

/**
 * Created by admin on 23/6/17.
 */

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> implements Filterable {

    Context mContext;
    public ArrayList<Disease> diseaseArrayList =new ArrayList<>();
    private ArrayList<Disease> mOriginalValues = new ArrayList<>();
    boolean isSearch=false;
    String textSearch="";

    public DiseaseAdapter(Context mContext, ArrayList<Disease> diseaseArrayList, boolean isSearch) {
        this.mContext = mContext;
        this.diseaseArrayList = diseaseArrayList;
        this.mOriginalValues = diseaseArrayList;
        this.isSearch = isSearch;
    }

    @Override
    public DiseaseAdapter.DiseaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disease, parent, false);

        return new DiseaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiseaseAdapter.DiseaseViewHolder holder, int position) {
        Disease disease = diseaseArrayList.get(position);

        final SpannableStringBuilder sb = new SpannableStringBuilder(disease.getName_disease());
        final ForegroundColorSpan fcs = new ForegroundColorSpan(mContext.getResources().getColor(R.color.title_disease));
        sb.setSpan(fcs,0,disease.getName_disease().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        if (disease.getName_disease().toLowerCase().startsWith(textSearch)) {
            final ForegroundColorSpan highlight = new ForegroundColorSpan(Color.CYAN);
            sb.setSpan(highlight,0,textSearch.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        holder.diseaseName.setText(sb);
        holder.symptomsCount.setText("Symptoms : "+disease.getSymptoms().size());
        holder.percentage.setText("");
        holder.symptomsMatch.setText("");
        if(isSearch){
            holder.percentage.setText(new DecimalFormat("###.##").format(disease.getMatchPercentage()));
            holder.symptomsMatch.setText("Maatches : "+disease.getSymptoms_match());
            //holder.symptomsMatch.setText(disease.);
        }
    }

    @Override
    public int getItemCount() {
        return diseaseArrayList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Disease> FilteredArrList = new ArrayList<Disease>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Disease>(diseaseArrayList); // saves the original data in mOriginalValues
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
                        String data = mOriginalValues.get(i).getName_disease();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(
                                    new Disease(
                                            mOriginalValues.get(i).getId_disease(),
                                            mOriginalValues.get(i).getName_disease(),
                                            mOriginalValues.get(i).getDescription(),
                                            mOriginalValues.get(i).getSymptoms()
                                    )
                            );
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
                diseaseArrayList = (ArrayList<Disease>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }
        };
        return filter;
    }

    public class DiseaseViewHolder extends RecyclerView.ViewHolder {
        public TextView diseaseName;
        public TextView symptomsCount;
        public TextView symptomsMatch;
        public TextView percentage;
        public CardView cardView;

        public DiseaseViewHolder(View view) {
            super(view);
            diseaseName = (TextView) view.findViewById(R.id.lblDiseaseName);
            symptomsCount = (TextView) view.findViewById(R.id.lblSymptomsCount);
            percentage = (TextView) view.findViewById(R.id.lblPercentage);
            symptomsMatch = (TextView) view.findViewById(R.id.lblSymptomsMatches);
            cardView=(CardView)view.findViewById(R.id.item_symptom);

        }
    }
}