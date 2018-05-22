package JavaBean.Entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhiweixu.mailclient.R;

import java.util.List;

public class MailAdapter extends ArrayAdapter<Mail> {
    private int resourceId;
    public MailAdapter(Context  context,int textViewResourceId,List<Mail> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Mail mail=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView mailImage = (ImageView)view.findViewById(R.id.mail_image);
        TextView mailName=(TextView) view.findViewById(R.id.mail_name);

        if(mail.getReadStat()==1){
        mailImage.setImageResource(R.drawable.ic_message_black_24dp);}
        else{
            mailImage.setImageResource(R.drawable.ic_message_grey_24dp);
        }

        mailName.setText(mail.getSubject()+'\n'+mail.getContent()+" "+mail.getReadStat());

        return view;
    }



}
