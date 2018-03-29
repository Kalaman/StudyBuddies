package com.th_koeln.studybuddies;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Kalaman on 28.03.2018.
 */

public class DatabaseActions {

    private static final String DATABASE_IP = "192.168.0.34:5000";
    private static final String DATABASE_URL_LEARNGROUPS = "http://"+ DATABASE_IP + "/learngroup/";
    private static final String DATABASE_URL_REGISTER= "http://"+ DATABASE_IP + "/register/";

    public interface DBRequestListener {
        public void onDBRequestFinished(String response);
    }

    public ArrayList<Learngroup> getAllLearngroups(Context context,DBRequestListener listener,boolean showProgressDialog) {
        new AsyncDatabaseGET(context,DATABASE_URL_LEARNGROUPS,listener,showProgressDialog).execute();
        return null;
    }

    public ArrayList<Learngroup> getUserLearngroups(Context context,DBRequestListener listener,boolean showProgressDialog) {
        new AsyncDatabaseGET(context,DATABASE_URL_LEARNGROUPS + MainActivity.studentName,listener, showProgressDialog).execute();
        return null;
    }

    public ArrayList<Learngroup> getLearngroups(Context context,DBRequestListener listener,Course course,boolean showProgressDialog) {
        new AsyncDatabaseGET(context,DATABASE_URL_LEARNGROUPS + "?courseid=" + course.getCid(),listener,showProgressDialog).execute();
        return null;
    }


    public ArrayList<Learngroup> registerUser(Context context,DBRequestListener listener,String studentname,String semester, int studyProgramID,boolean showProgressDialog) {
        String [] formDataKey = new String[] {"studentname","semester","studentprogram"};
        String [] formDataValue = new String[] {studentname,semester,String.valueOf(studyProgramID)};

        new AsyncDatabasePOST(context,DATABASE_URL_REGISTER,listener,formDataKey,formDataValue,showProgressDialog).execute();
        return null;
    }

    private class AsyncDatabaseGET extends AsyncTask<Void,Void,String>
    {
        String url;
        Response response;
        DBRequestListener listener;
        ProgressDialog progressDialog;
        Context context;

        public AsyncDatabaseGET(Context context, String url, DBRequestListener listener, boolean showProgressDialog) {
            if (showProgressDialog) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Lädt ...");
                progressDialog.show();
            }
            this.listener = listener;
            this.url = url;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(250);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e ) {
                Log.e("Okhttp error", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null)
                listener.onDBRequestFinished(response);
            else
                Toast.makeText(context,"Verbindung zum Server fehlgeschlagen !",Toast.LENGTH_SHORT).show();

            if (this.response != null)
                this.response.body().close();

            if (progressDialog != null)
                progressDialog.dismiss();
        }
    }


    private class AsyncDatabasePOST extends AsyncTask<Void,Void,String>
    {
        String url;
        Response response;
        DBRequestListener listener;
        ProgressDialog progressDialog;
        String [] formDataKey;
        String [] formDataValue;
        Context context;

        public AsyncDatabasePOST(Context context, String url, DBRequestListener listener, String [] formDataKey , String [] formDataValue  , boolean showProgressDialog) {
            if (showProgressDialog) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Lädt ...");
                progressDialog.show();
            }
            this.listener = listener;
            this.url = url;
            this.formDataKey = formDataKey;
            this.formDataValue = formDataValue;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(250);
                OkHttpClient client = new OkHttpClient();

                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                for (int i=0; i< formDataValue.length;++i) {
                    multipartBodyBuilder.addFormDataPart(formDataKey[i],formDataValue[i]);
                }

                Request request = new Request.Builder().url(url).post(multipartBodyBuilder.build()).build();
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e ) {
                Log.e("Okhttp error", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null)
                listener.onDBRequestFinished(response);
            else
                Toast.makeText(context,"Verbindung zum Server fehlgeschlagen !",Toast.LENGTH_SHORT).show();

            if (this.response != null)
                this.response.body().close();

            if (progressDialog != null)
                progressDialog.dismiss();
        }
    }

}
