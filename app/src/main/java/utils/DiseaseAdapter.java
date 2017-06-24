package utils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dev4u.hv.my_diagnostic.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import db.Disease;
import db.Symptom;

/**
 * Created by admin on 23/6/17.
 */

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    Context mContext;
    public ArrayList<Disease> diseaseArrayList =new ArrayList<>();
    boolean isSearch=false;

    public DiseaseAdapter(Context mContext, ArrayList<Disease> diseaseArrayList, boolean isSearch) {
        this.mContext = mContext;
        this.diseaseArrayList = diseaseArrayList;
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
        holder.diseaseName.setText(disease.getName_disease());
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
