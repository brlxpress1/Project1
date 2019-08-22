package com.tur.job1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tur.job1.R;
import com.tur.job1.company.Company_SearchBoard;
import com.tur.job1.others.Skill_Selector;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultExample extends BaseAdapter {



    int count;

    ArrayList<Integer> jobSeekerId;
    ArrayList<String> jobSeekerPhotoUrl;
    ArrayList<String> jobSeekerName;
    ArrayList<String> jobSeekerDesignation;
    ArrayList<Integer> jobSeekerExperience;
    ArrayList<Integer> jobSeekerExpectedSalary;


    Context context;

    // int [] imageId;

    private static LayoutInflater inflater=null;

    public SearchResultExample(Company_SearchBoard company_searchBoard, int count1, ArrayList<Integer> jobSeekerId1, ArrayList<String> jobSeekerPhotoUrl1, ArrayList<String> jobSeekerName1, ArrayList<String> jobSeekerDesignation1, ArrayList<Integer> jobSeekerExperience1, ArrayList<Integer> jobSeekerExpectedSalary1) {

        // TODO Auto-generated constructor stub





        count = count1;

        jobSeekerId = jobSeekerId1;
        jobSeekerPhotoUrl = jobSeekerPhotoUrl1;
        jobSeekerName = jobSeekerName1;
        jobSeekerDesignation = jobSeekerDesignation1;
        jobSeekerExperience = jobSeekerExperience1;
        jobSeekerExpectedSalary = jobSeekerExpectedSalary1;

        context=company_searchBoard;

        // imageId=prgmImages;

        inflater = (LayoutInflater)context.

                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override

    public int getCount() {

        // TODO Auto-generated method stub

        return count;

    }



    @Override

    public Object getItem(int position) {

        // TODO Auto-generated method stub

        return position;

    }



    @Override

    public long getItemId(int position) {

        // TODO Auto-generated method stub

        return position;

    }



    public class Holder

    {

        LinearLayout masterLayout;
        CircleImageView profilePhoto;
        TextView fullname;
        TextView designation;
        TextView experience;
        TextView expectedSalary;


    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub

        final SearchResultExample.Holder holder=new SearchResultExample.Holder();

        View rowView;

        rowView = inflater.inflate(R.layout.item_employee_search, null);





        //holder.skill_id_display=(TextView) rowView.findViewById(R.id.skillID);
        holder.masterLayout = (LinearLayout)rowView.findViewById(R.id.masterLayout);
        holder.profilePhoto=(CircleImageView) rowView.findViewById(R.id.profile_image2);
        holder.fullname = (TextView) rowView.findViewById(R.id.name);
        holder.designation = (TextView) rowView.findViewById(R.id.designation);
        holder.experience = (TextView) rowView.findViewById(R.id.experience);
        holder.expectedSalary = (TextView) rowView.findViewById(R.id.expected_salary);

        if(jobSeekerPhotoUrl.get(position)!= null || !jobSeekerPhotoUrl.get(position).toString().equalsIgnoreCase("")){


            Glide.with(context)
                    .load(jobSeekerPhotoUrl.get(position))
                    .centerCrop()
                    .placeholder(R.drawable.default_avatar)
                    .into(holder.profilePhoto);

        }

        holder.fullname.setText(jobSeekerName.get(position));
        holder.designation.setText(jobSeekerDesignation.get(position));
        holder.experience.setText("Experience : "+jobSeekerExperience.get(position)+" years");
        holder.expectedSalary.setText("Expected Salary : "+jobSeekerExperience.get(position)+" taka");




        holder.masterLayout.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {


                String temp = String.valueOf(jobSeekerId.get(position));

                //(int userID, String photoUrl, String userName, String email, String Experience, String expectedSalary)
                ((Company_SearchBoard)context).searchItemClick(position,holder.masterLayout,Integer.parseInt(temp),jobSeekerPhotoUrl.get(position),jobSeekerName.get(position),jobSeekerDesignation.get(position),String.valueOf(jobSeekerExperience.get(position)),String.valueOf(jobSeekerExpectedSalary.get(position)));

            }

        });


        return rowView;


    }















}

