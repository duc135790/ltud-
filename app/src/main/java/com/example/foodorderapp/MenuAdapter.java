package com.example.foodorderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MenuAdapter extends ArrayAdapter<MenuItem> {

    public interface OnItemActionListener {
        void onAction(MenuItem item);
    }

    private Context context;
    private List<MenuItem> list;
    private OnItemActionListener editListener;
    private OnItemActionListener deleteListener;

    public MenuAdapter(Context context, List<MenuItem> list,
                       OnItemActionListener editListener,
                       OnItemActionListener deleteListener) {
        super(context, 0, list);
        this.context        = context;
        this.list           = list;
        this.editListener   = editListener;
        this.deleteListener = deleteListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_menu, parent, false);
        }

        MenuItem item = list.get(position);
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));

        ((TextView) convertView.findViewById(R.id.tv_name)).setText(item.getName());
        ((TextView) convertView.findViewById(R.id.tv_price)).setText(fmt.format(item.getPrice()) + "đ");
        ((TextView) convertView.findViewById(R.id.tv_desc)).setText(item.getDescription());

        Button btnEdit   = convertView.findViewById(R.id.btn_edit);
        Button btnDelete = convertView.findViewById(R.id.btn_delete);

        if (btnEdit != null)   btnEdit.setOnClickListener(v -> editListener.onAction(item));
        if (btnDelete != null) btnDelete.setOnClickListener(v -> deleteListener.onAction(item));

        return convertView;
    }
}