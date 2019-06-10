package com.example.passdemo1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mTimes=new ArrayList<>();
    private ArrayList<String> mSubjects=new ArrayList<>();
    private ArrayList<String> mRoom=new ArrayList<>();
    private Context mComntext;

    public RecyclerViewAdapter(ArrayList<String> mTimes, ArrayList<String> mSubjects, ArrayList<String> mRoom, Context mComntext) {
        this.mTimes = mTimes;
        this.mSubjects = mSubjects;
        this.mRoom=mRoom;
        this.mComntext = mComntext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_table_card,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.t_time.setText(converto12(mTimes.get(i)));
        viewHolder.t_class.setText(mSubjects.get(i));
        viewHolder.t_room.setText(mRoom.get(i));
    }

    @Override
    public int getItemCount() {
        return mTimes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView t_time;
        TextView t_class;
        TextView t_room;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t_time=itemView.findViewById(R.id.tableCardTime);
            t_class=itemView.findViewById(R.id.tableCardSubject);
            t_room=itemView.findViewById(R.id.tableCardRoom);
        }
    }

    public String converto12(String str) {
        String finalt="";
        int h1 = (int) str.charAt(0) - '0';
        int h2 = (int) str.charAt(1) - '0';
        int h3 = (int) str.charAt(6) - '0';
        int h4 = (int) str.charAt(7) - '0';

        int hh = h1 * 10 + h2;
        int hh1 = h3 * 10 + h4;

        String Meridien;
        if (hh < 12) {
            Meridien = "AM";
        } else
            Meridien = "PM";

        hh %= 12;
        if (hh == 0) {
            finalt += "12";


            for (int i = 2; i < 5; ++i) {
                finalt += str.charAt(i);
            }
        } else {
            finalt += Integer.toString(hh);
            for (int i = 2; i < 5; ++i) {

                finalt += str.charAt(i);
            }

            finalt += Meridien;
            finalt += " to ";

            String Meridien1;
            if (hh1 < 12) {
                Meridien1 = "AM";
            } else
                Meridien1 = "PM";

            hh1 %= 12;
            if (hh1 == 0) {
                finalt += "12";


                for (int i = 8; i < 11; ++i) {
                    finalt += str.charAt(i);
                }
            } else {
                finalt += Integer.toString(hh1);
                for (int i = 8; i < 11; ++i) {
                    finalt += str.charAt(i);
                }

                finalt += Meridien1;
            }
        }
        return finalt;
    }
}
