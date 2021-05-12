package com.example.mappis.CardMaps;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mappis.Database.MapRepository;

public class AddCardViewModel extends AndroidViewModel {

    private MapRepository repository;


    public AddCardViewModel(@NonNull Application application) {
        super(application);
        repository = new MapRepository(application);
    }

    public void addCardItem(CardItem item) {
        repository.addCardItem(item);
    }
}
