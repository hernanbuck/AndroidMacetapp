package com.example.macetapp40.di

import androidx.lifecycle.MutableLiveData
import com.example.macetapp40.ShareDataViewModel
import com.example.macetapp40.repository.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    single (named("retrofitConfig")) {
        provideRetrofit(provideHttpclient(), "http://macetapp.herokuapp.com/api/")
    }

    single (named("macetappAPI")) {
        MacetappAPI.provideMacetappAPI(get(named("retrofitConfig")))
    }

    factory <MacetappRepository> (named("macetappRepository")) {
        MacetappRepositoryImpl(get(named("macetappAPI")))
    }

    viewModel{
        ShareDataViewModel(
            macetappRepository = get(named("macetappRepository")),
            _viewModelState = MutableLiveData()
        )
    }
}