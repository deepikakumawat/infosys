package com.infosys.dependency;

import com.infosys.network.NetworkModule;
import com.infosys.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface Dependency {
    void inject(MainActivity mainActivity);
}
