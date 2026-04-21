package com.example.foodorderapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import java.util.List;

public class FirstFragment extends Fragment {

    private ListView lvMenu;
    private EditText etSearch;
    private MenuDAO dao;
    private MenuAdapter adapter;
    private List<MenuItem> menuList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvMenu   = view.findViewById(R.id.lv_menu);
        etSearch = view.findViewById(R.id.et_search);
        Button btnAdd = view.findViewById(R.id.btn_add);
        dao = new MenuDAO(requireContext());

        loadData();

        btnAdd.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                menuList = keyword.isEmpty() ? dao.getAll() : dao.search(keyword);
                adapter  = new MenuAdapter(requireContext(), menuList,
                        item -> editItem(item),
                        item -> deleteItem(item));
                lvMenu.setAdapter(adapter);
            }
        });
    }

    private void loadData() {
        menuList = dao.getAll();
        adapter  = new MenuAdapter(requireContext(), menuList,
                item -> editItem(item),
                item -> deleteItem(item));
        lvMenu.setAdapter(adapter);
    }

    private void editItem(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("name", item.getName());
        bundle.putDouble("price", item.getPrice());
        bundle.putString("desc", item.getDescription());
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
    }

    private void deleteItem(MenuItem item) {
        dao.delete(item.getId());
        Toast.makeText(requireContext(), "Đã xóa " + item.getName(), Toast.LENGTH_SHORT).show();
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}