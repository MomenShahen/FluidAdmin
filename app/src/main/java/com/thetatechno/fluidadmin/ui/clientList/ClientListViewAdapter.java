package com.thetatechno.fluidadmin.ui.clientList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.Person;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;


public class ClientListViewAdapter extends RecyclerView.Adapter<ClientListViewAdapter.vHolder> implements Filterable {
    Context context;
    FragmentManager fragmentManager;
    List<Person> personList;
    List<Person> filteredClientList;
    OnDeleteListener listener;


    public ClientListViewAdapter(Context context, @Nullable List<Person> personList, FragmentManager fragmentManager) {
        Gson gson = new Gson();
        Log.e("Images", gson.toJson(personList));

        this.context = context;
        this.fragmentManager = fragmentManager;
        this.personList = personList;
        this.filteredClientList = personList;
        if (context instanceof OnDeleteListener)
            listener = (OnDeleteListener) context;
        else
            listener = null;
    }

    @NonNull
    @Override
    public vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        view = LayoutInflater.from(context).inflate(R.layout.client_list_item, parent, false);
        return new vHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final vHolder holder, final int position) {

        try {
            if(!filteredClientList.get(position).getId().isEmpty()) {
                holder.idTxt.setText(filteredClientList.get(position).getId());
                holder.idTxt.setVisibility(View.VISIBLE);
            }
            else {

                holder.idTxt.setVisibility(View.GONE);
            }
            if(!filteredClientList.get(position).getFirstName().isEmpty() || ! filteredClientList.get(position).getFamilyName().isEmpty()) {
                holder.fullNameTxt.setText(filteredClientList.get(position).getFirstName() + " " + filteredClientList.get(position).getFamilyName());
                holder.fullNameTxt.setVisibility(View.VISIBLE);
            }
            else {
                holder.fullNameTxt.setVisibility(View.GONE);
            }
            if(!filteredClientList.get(position).getEmail().isEmpty()) {
                holder.mailTxt.setText(filteredClientList.get(position).getEmail());
                holder.mailTxt.setVisibility(View.VISIBLE);
            }
            else {
                holder.mailTxt.setVisibility(View.GONE);
            }
            if(!filteredClientList.get(position).getMobileNumber().isEmpty()) {
                holder.phoneTxt.setText(filteredClientList.get(position).getMobileNumber());
                holder.phoneTxt.setVisibility(View.VISIBLE);
            }
            else {

                holder.phoneTxt.setVisibility(View.GONE);
            }

            if (!filteredClientList.get(position).getImageLink().isEmpty()) {
                Glide.with(context).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + filteredClientList.get(position).getImageLink())
                        .circleCrop()
                        .into(holder.personImg);
            }
            else{
                if(!filteredClientList.get(position).getGender().isEmpty()) {
                    if (filteredClientList.get(position).getGender().equals(EnumCode.Gender.M.toString())) {
                        holder.personImg.setImageResource(R.drawable.man);
                    } else if(filteredClientList.get(position).getGender().equals(EnumCode.Gender.F.toString())){
                        holder.personImg.setImageResource(R.drawable.ic_girl);
                    }
                }
                else {
                    holder.personImg.setImageResource(R.drawable.man);
                }
            }
        } catch (Exception e) {
            Sentry.capture(e);
        }


    }


    @Override
    public int getItemCount() {
        if(filteredClientList!=null && filteredClientList.size()>0)
        return filteredClientList.size();
        else return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredClientList= personList;
                } else {
                    List<Person> filteredList = new ArrayList<>();
                    for (Person person : personList) {
                        if (person.getFirstName().contains(charSequenceString) || person.getFamilyName().contains(charSequenceString)) {
                            filteredList.add(person);
                        }
                        filteredClientList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredClientList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredClientList = (List<Person>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class vHolder extends RecyclerView.ViewHolder {
        ImageView personImg;
        TextView fullNameTxt, mailTxt, phoneTxt;
        TextView idTxt;


        public vHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.clientImg);
            fullNameTxt = itemView.findViewById(R.id.clientFullNameTxt);
            mailTxt = itemView.findViewById(R.id.client_email_txt);
            phoneTxt = itemView.findViewById(R.id.client_phone_txt);
            idTxt = itemView.findViewById(R.id.clientIdTxt);

        }
    }
}