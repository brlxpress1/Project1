package com.tur.job1.others;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tur.job1.R;
import com.tur.job1.company.Company_SearchBoard;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import es.dmoral.toasty.Toasty;


public class Dialogue_Helper {







    //-- Showing name input
    public void askingForName(Activity actv, EditText edt, Job_Seeker_Dashboard job_seeker_dashboard ) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.name_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                               String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "You have to type your name!", Toast.LENGTH_LONG, true).show();
                                    askingForName(actv,edt,job_seeker_dashboard);


                                }else {

                                 edt.setText(nameTemp);

                                 job_seeker_dashboard.setName();


                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-------------------

    //-- Showing Email input
    public void askingForEmail(Activity actv, EditText edt, Job_Seeker_Dashboard job_seeker_dashboard) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.email_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "You have to type your Email!", Toast.LENGTH_LONG, true).show();
                                    askingForEmail(actv,edt,job_seeker_dashboard);


                                }else {

                                    edt.setText(nameTemp);
                                    job_seeker_dashboard.setEmail();


                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-------------------

    //-- Showing Experience input
    public void askingForExperience(Activity actv, EditText edt, Job_Seeker_Dashboard jobSeekerDashboard) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.experience_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "You have to type your Experience!", Toast.LENGTH_LONG, true).show();
                                    askingForExperience(actv,edt,jobSeekerDashboard);


                                }else {

                                    edt.setText(nameTemp);
                                    jobSeekerDashboard.setExperience();


                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-------------------

    //-- Showing salary input
    public void askingForSalary(Activity actv, EditText edt, Job_Seeker_Dashboard jobSeekerDashboard) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.salary_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "Enter the amount of your salary!", Toast.LENGTH_LONG, true).show();
                                    askingForSalary(actv,edt,jobSeekerDashboard);


                                }else {

                                    edt.setText(nameTemp);
                                    jobSeekerDashboard.setSalary();


                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-------------------

    //-- Showing current company input
    public void askingForCurrentCompany(Activity actv, EditText edt, Job_Seeker_Dashboard jobSeekerDashboard) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.current_company_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "You have to type your current company name!", Toast.LENGTH_LONG, true).show();
                                    askingForCurrentCompany(actv,edt,jobSeekerDashboard);


                                }else {

                                    edt.setText(nameTemp);
                                    jobSeekerDashboard.setCompany();


                                }
                            }
                        })
                /*
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
                        */
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-------------------

    //-- Showing current designation input
    public void askingForDesignation(Activity actv, EditText edt, Job_Seeker_Dashboard jobSeekerDashboard) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.designation_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText input_name = (EditText) promptsView
                .findViewById(R.id.name);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "You have to type your designation!", Toast.LENGTH_LONG, true).show();
                                    askingForDesignation(actv,edt,jobSeekerDashboard);


                                }else {

                                    edt.setText(nameTemp);
                                    jobSeekerDashboard.setDesignatiion();


                                }
                            }
                        })
                /*
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
                        */
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-------------------

    //-- Showing prepered location input
    public void askingForPreperedLocation(Activity actv, EditText edt, Job_Seeker_Dashboard jobSeekerDashboard) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.prepered_location_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final AutoCompleteTextView input_name = (AutoCompleteTextView) promptsView
                .findViewById(R.id.name);

        LocationList ll = new LocationList();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(actv, android.R.layout.simple_list_item_1, ll.preparedLocationList);

        input_name.setAdapter(adapter);




        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    Toasty.error(actv, "Enter the location where you want the job most!", Toast.LENGTH_LONG, true).show();
                                    askingForPreperedLocation(actv,edt,jobSeekerDashboard);


                                }else {

                                    edt.setText(nameTemp);
                                    jobSeekerDashboard.setLocation();


                                }
                            }
                        })
                /*
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
                        */
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //-- Showing prepered location input
    public void showLocationSearch(Activity actv, EditText edt, Company_SearchBoard company_searchBoard) {



        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(actv);
        View promptsView = li.inflate(R.layout.prepered_location_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                actv);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final AutoCompleteTextView input_name = (AutoCompleteTextView) promptsView
                .findViewById(R.id.name);

        LocationList ll = new LocationList();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(actv, android.R.layout.simple_list_item_1, ll.preparedLocationList);

        input_name.setAdapter(adapter);




        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                    //Toasty.error(actv, "You must enter the location name!", Toast.LENGTH_LONG, true).show();
                                   // askingForPreperedLocation(actv,edt,company_searchBoard);


                                }else {

                                    edt.setText(nameTemp);
                                    //jobSeekerDashboard.setLocation();


                                }
                            }
                        })
                /*
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                //String nameTemp = "";
                                String nameTemp = input_name.getText().toString();

                                if(nameTemp.equals("")){

                                }else {

                                }
                            }
                        })
                        */
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }




    //-------------------


}
