package com.infosys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.infosys.dependency.DaggerDependency;
import com.infosys.dependency.Dependency;
import com.infosys.network.NetworkModule;

import java.io.File;

public class BaseActivity extends AppCompatActivity {
    Dependency dependency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
        dependency = DaggerDependency.builder().networkModule(new NetworkModule(cacheFile)).build();
    }

    public Dependency getDependency() {
        return dependency;
    }
}
