package app.mat.tt.parcelableandyql;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TT on 7/2/2016.
 */
public class Result implements Parcelable{


    String title;
    String address;
    String city;
    String phone;
    String bussinessUrl;
    String distance;

    double latitude;
    double longitude;

    public Result(double longitude, String title, String address, String city, String phone, String bussinessUrl, String distance, double latitude) {
        this.longitude = longitude;
        this.title = title;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.bussinessUrl = bussinessUrl;
        this.distance = distance;
        this.latitude = latitude;
    }

    protected Result(Parcel in) {
        title = in.readString();
        address = in.readString();
        city = in.readString();
        phone = in.readString();
        bussinessUrl = in.readString();
        distance = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(phone);
        parcel.writeString(bussinessUrl);
        parcel.writeString(distance);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
