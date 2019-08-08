package com.tur.job1.adapters;

import android.content.Context;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.tur.job1.R;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.others.Skill_Selector;

import java.util.ArrayList;
import java.util.List;



public class SkillsSetAdapter extends BaseAdapter {



    int count;
    //ArrayList<String> skillID;
    ArrayList<String> skillName;

    Context context;

    // int [] imageId;

    private static LayoutInflater inflater=null;

    public SkillsSetAdapter(Skill_Selector mainActivity, int count1, ArrayList<String> skillName1) {

        // TODO Auto-generated constructor stub





        count = count1;
        //skillID = skillID1;
        skillName = skillName1;

        context=mainActivity;

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

        //TextView skill_id_display;
        TextView skill_name_display;
        LinearLayout crossButton;


    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub

        final SkillsSetAdapter.Holder holder=new SkillsSetAdapter.Holder();

        View rowView;

        rowView = inflater.inflate(R.layout.item_skill_set, null);





        //holder.skill_id_display=(TextView) rowView.findViewById(R.id.skillID);
        holder.skill_name_display=(TextView) rowView.findViewById(R.id.skillName);
        holder.crossButton = (LinearLayout)rowView.findViewById(R.id.crossbutton);


        //holder.skill_id_display.setText("1");
        holder.skill_name_display.setText(skillName.get(position));

        holder.crossButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {



                ((Skill_Selector)context).deleteSkillFromList(skillName.get(position));

            }

        });

        return rowView;


    }















}
