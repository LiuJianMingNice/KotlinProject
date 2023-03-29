package com.example.myapplication.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

/**
 * Copyright (c) 2021 Raysharp.cn. All rights reserved.
 * <p>
 * Account
 *
 * @author liujianming
 * @date 2021-12-23
 */
public class Account implements Parcelable {

    public String name;
    public String type;
    private @Nullable String accessId;

    protected Account(Parcel in) {
        name = in.readString();
        type = in.readString();
        accessId = in.readString();
    }

    public Account(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Account() {
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(accessId);
    }
}
