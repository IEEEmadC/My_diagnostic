package org.dev4u.hv.my_diagnostic.Fragments;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import db.MedicalHistory;
import db.Symptom;
import db.User;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.DiseaseUtilitesSingleton;
import utils.HistoryAdapter;
import utils.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment {

    private View view;
    private ImageView hearth;
    private AnimatedVectorDrawable hearthAnim;
    private Bitmap activePicture;
    private ImageView imageViewDialog;
    private CircleImageView circleImageView;
    private User user;
    private TextView lblUsername;
    private TextView lblBirtday;
    private TextView lblBloodtype;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private FloatingActionButton btnAddHistory;
    private ArrayList<MedicalHistory> medicalHistoryArrayList;
    boolean isMultiSelect = false;
    private ArrayList<MedicalHistory> multiselect_list = new ArrayList<>();
    private ActionMode mActionMode;
    private Menu context_menu;
    MenuItem itemFind;
    SearchView sv ;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history, container, false);
        medicalHistoryArrayList = new ArrayList<>();

        MedicalHistory medicalHistory = new MedicalHistory("1","Titulo "+medicalHistoryArrayList.size()+1,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sed augue ut enim elementum efficitur. In in dignissim dolor, sed rutrum tortor. Nunc eget leo varius, maximus lacus sed, faucibus lorem. Vivamus eget facilisis ex, sit amet tristique odio. Vivamus placerat ex nisl, et pharetra mauris porta id. "
                ,"1","1","Fecha");
        medicalHistory.setName_disease("Lupus");

        medicalHistoryArrayList.add(medicalHistory);
        //animation
        hearth = (ImageView)view.findViewById(R.id.pulse);
        hearthAnim = ((AnimatedVectorDrawable) ((ImageView) view.findViewById(R.id.pulse)).getDrawable());
        //controls

        activePicture = ((BitmapDrawable) getActivity().getBaseContext().getDrawable(R.drawable.ic_profile)).getBitmap();
        circleImageView = (CircleImageView)view.findViewById(R.id.profile_image);
        lblUsername = (TextView) view.findViewById(R.id.lblFullname);
        lblBirtday = (TextView) view.findViewById(R.id.lblBirthday);
        lblBloodtype = (TextView) view.findViewById(R.id.lblBloodtype);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        btnAddHistory = (FloatingActionButton) view.findViewById(R.id.btnAddHistory);

        historyAdapter = new HistoryAdapter(getContext(),medicalHistoryArrayList,multiselect_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);
        //historyAdapter.notifyDataSetChanged();


        //setting the profile image
        Bitmap b =null;//= loadImageFromStorage("Profile","profile.png");
        if(b!=null) {
            activePicture = b;
            circleImageView.setImageBitmap(Bitmap.createScaledBitmap (b,(int) (b.getWidth() * .4), (int) (b.getHeight() * .4),true));
        }

        setHasOptionsMenu(true);
        //restartCursiveAnimation();

        AnimatedVectorDrawable d = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.hearth_pulse_animation); // Insert your AnimatedVectorDrawable resource identifier
        hearth.setImageDrawable(d);
        d.start();


        hearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartCursiveAnimation();
            }
        });


        //update values
        preferences = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        editorPreferences = preferences.edit();

        String status = preferences.getString("USERNAME","null");
        if(!status.equals("null")){
            user = DiseaseUtilitesSingleton.getInstance().getUser(status);
            lblUsername.setText(user.getFullname());

            String parts[] = user.getBirthday().split("-");
            String bday = parts[2]+"/"+parts[1]+"/"+parts[0];
            lblBloodtype.setText("BloodType :"+user.getName_bloodtype());
            lblBirtday.setText("BirthDay :"+bday);
        }

        btnAddHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mActionMode != null) {
                    mActionMode.finish();
                }
                if (!sv.isIconified()) {
                    sv.onActionViewCollapsed();
                }

                MedicalHistory medicalHistory = new MedicalHistory("1","Titulo "+medicalHistoryArrayList.size()+1,
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sed augue ut enim elementum efficitur. In in dignissim dolor, sed rutrum tortor. Nunc eget leo varius, maximus lacus sed, faucibus lorem. Vivamus eget facilisis ex, sit amet tristique odio. Vivamus placerat ex nisl, et pharetra mauris porta id. "
                        ,"1","1","Fecha");
                medicalHistory.setName_disease("Lupus");

                medicalHistoryArrayList.add(medicalHistory);
                historyAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else
                    Toast.makeText(getContext(), "Details Page", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<MedicalHistory>();
                    isMultiSelect = true;
                    if (mActionMode == null) {
                        mActionMode =  getActivity().startActionMode(mActionModeCallback);
                    }
                }
                multi_select(position);
            }
        }));


        return view;
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(medicalHistoryArrayList.get(position)))
                multiselect_list.remove(medicalHistoryArrayList.get(position));
            else
                multiselect_list.add(medicalHistoryArrayList.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else {
                mActionMode.setTitle("");
                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }

            refreshAdapter();

        }
    }

    private void restartCursiveAnimation() {

        Log.d("Estado anim$$$$$$$$$$$","Estado "+hearthAnim.isRunning());
        hearthAnim.stop();
        hearthAnim.start();
        Log.d("Estado anim$$$$$$$$$$$","Estado "+hearthAnim.isRunning());
    }


    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_find, menu);
        itemFind = menu.findItem(R.id.action_search);

        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        sv = new SearchView(((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(itemFind, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(itemFind, sv);


        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                historyAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                //handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap loadImageFromStorage(String path,String name)
    {
        try {
            ContextWrapper cw = new ContextWrapper(getContext());
            File directory = cw.getDir(path, Context.MODE_PRIVATE);
            File f=new File(directory, name);
            Log.d("Se busca en: ",f.getAbsolutePath());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;

    }
    //callback
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.delete_menu, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    showDialogPicture();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<MedicalHistory>();
            refreshAdapter();
        }
    };

    private void showDialogPicture(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Warning");
        dialogBuilder.setMessage("Delete this medical history(ies)?");
        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(multiselect_list.size()>0)
                {
                    for(int i=0;i<multiselect_list.size();i++)
                        medicalHistoryArrayList.remove(multiselect_list.get(i));
                    historyAdapter.notifyDataSetChanged();
                    if (mActionMode != null) {
                        mActionMode.finish();
                    }
                    Toast.makeText(getContext(), "Delete Click", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    public void refreshAdapter()
    {
        //if (!sv.isIconified()) {
        //    sv.onActionViewCollapsed();
        //}
        historyAdapter.selectedList=multiselect_list;
        historyAdapter.historyArrayList=medicalHistoryArrayList;
        //historyAdapter.notifyItemRangeRemoved(0,medicalHistoryArrayList.size());
        historyAdapter.notifyDataSetChanged();
    }
}
