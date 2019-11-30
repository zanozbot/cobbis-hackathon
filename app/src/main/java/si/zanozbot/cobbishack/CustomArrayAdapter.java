package si.zanozbot.cobbishack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<DataModel> {

    private Context mContext;
    private List<DataModel> mList = new ArrayList<DataModel>();

    public CustomArrayAdapter(@NonNull Context context, List<DataModel> list) {
        super(context, 0, list);
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.custom_list_item,parent,false);

        DataModel curr = mList.get(position);

        TextView number = listItem.findViewById(R.id.textView);
        number.setText(curr.getNumber());

        TextView message = listItem.findViewById(R.id.message);
        message.setText(curr.getMessage());

        return listItem;
    }
}
