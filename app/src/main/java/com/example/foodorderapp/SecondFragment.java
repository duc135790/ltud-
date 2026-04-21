package com.example.foodorderapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    private EditText etName, etPrice, etDesc;
    private MenuDAO dao;
    private int editId = -1; // -1 = thêm mới, khác -1 = đang sửa

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName  = view.findViewById(R.id.et_name);
        etPrice = view.findViewById(R.id.et_price);
        etDesc  = view.findViewById(R.id.et_desc);
        Button btnSave = view.findViewById(R.id.btn_save);
        dao = new MenuDAO(requireContext());

        // Nếu có bundle -> đang sửa, điền dữ liệu vào
        Bundle args = getArguments();
        if (args != null) {
            editId = args.getInt("id", -1);
            etName.setText(args.getString("name"));
            etPrice.setText(String.valueOf(args.getDouble("price")));
            etDesc.setText(args.getString("desc"));
            btnSave.setText("Cập nhật");
        }

        btnSave.setOnClickListener(v -> {
            String name  = etName.getText().toString().trim();
            String price = etPrice.getText().toString().trim();
            String desc  = etDesc.getText().toString().trim();

            // Validate
            if (name.isEmpty()) {
                etName.setError("Vui lòng nhập tên món");
                return;
            }
            if (price.isEmpty()) {
                etPrice.setError("Vui lòng nhập giá");
                return;
            }

            MenuItem item = new MenuItem();
            item.setName(name);
            item.setPrice(Double.parseDouble(price));
            item.setDescription(desc);

            if (editId == -1) {
                // THÊM MỚI
                dao.insert(item);
                Toast.makeText(requireContext(), "Đã thêm " + name, Toast.LENGTH_SHORT).show();
            } else {
                // SỬA
                item.setId(editId);
                dao.update(item);
                Toast.makeText(requireContext(), "Đã cập nhật " + name, Toast.LENGTH_SHORT).show();
            }

            // Quay lại danh sách
            NavHostFragment.findNavController(this).popBackStack();
        });
    }
}