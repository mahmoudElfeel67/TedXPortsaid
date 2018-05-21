package com.bloomers.tedxportsaid.Utitltes.other;


import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@SuppressWarnings("WeakerAccess")
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
      //  registry.append(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());
    }
}