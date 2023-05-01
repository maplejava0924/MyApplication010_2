package com.example.myapplication010.viewmodel;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    public ObservableField<String> text = new ObservableField<String>();
    public ObservableField<Integer> resource = new ObservableField<Integer>();

    public MyViewModel(String text) {
        this.text.set(text);
    }

    public ObservableField<String> getText() {
        return this.text;
    }

    public ObservableField<Integer> getResource() {
        return this.resource;
    }


}
