package com.emwno.ngo.datamanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 02 Aug 2018.
 */
public class Search implements Parcelable {

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };

    private String name;
    private String field;
    private String query;
    private String data;

    public Search() {
    }


    protected Search(Parcel in) {
        name = in.readString();
        field = in.readString();
        query = in.readString();
        data = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(field);
        dest.writeString(query);
        dest.writeString(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Search{" +
                "name='" + name + '\'' +
                ", field='" + field + '\'' +
                ", query='" + query + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
