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

public class FriendInfoAdapter extends ArrayAdapter<FriendInfo> {

    private int resourceId;

    // ssdkjsdl
    public FriendInfoAdapter(Context context, int textViewResourceId, List<FriendInfo> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        FriendInfo friendInfo=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView friendImage = (ImageView)view.findViewById(R.id.mail_image);
        TextView friendName=(TextView) view.findViewById(R.id.mail_name);

        friendImage.setImageResource(R.drawable.ic_face_black_24dp);

        String friendmsg = friendInfo.getkeywordRst();
        if(!friendInfo.getkeywordRst().equals(friendInfo.getFriendId()))
            friendmsg += "\n" + friendInfo.getFriendId();
        friendName.setText(friendmsg);

        return view;
    }
}
