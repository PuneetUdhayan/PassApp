package com.example.passdemo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Cards> {
    public static final int CLASS_CARD=0;
    public static final int AVAILABLE_CARD=1;

    private static final String TAG="CustomListAdapter";
    private Context mContext;
    private int mResource;

    public CustomListAdapter(Context context, int resource, ArrayList<Cards> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String ctime=getItem(position).getTime();
        String csub=getItem(position).getSubject();
        String croom=getItem(position).getRoom();

        if(getItemViewType(position)==CLASS_CARD)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.class_card,parent,false);
        else
            convertView = LayoutInflater.from(mContext).inflate(R.layout.available_card,parent,false);

        TextView t=convertView.findViewById(R.id.classCardTime);
        TextView s=convertView.findViewById(R.id.classCardSubject);
        TextView r=convertView.findViewById(R.id.classCardRoom);
        t.setText(converto12(ctime));
        s.setText(csub);
        r.setText(croom);

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Cards bob=getItem(position);
        if(bob.getAvailable()){
            return  AVAILABLE_CARD;
        }
        else{
            return  CLASS_CARD;
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
