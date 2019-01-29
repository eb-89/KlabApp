

package me.eb.klabapp.roombase;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class Puzzle implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    int pkey;

    @ColumnInfo(name = "publicName")
    public String publicName;

    @ColumnInfo(name = "difficulty")
    public String difficulty;

    @ColumnInfo(name = "data")
    public String data;

    Puzzle(String publicName, String difficulty, String data) {
        this.publicName = publicName;
        this.difficulty = difficulty;
        this.data = data;
    }

    @Ignore
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(publicName);
        out.writeString(difficulty);
        out.writeString(data);
    }
    public Puzzle(Parcel in) {
        this.publicName = in.readString();
        this.difficulty = in.readString();
        this.data = in.readString();
    }

    public static final Parcelable.Creator<Puzzle> CREATOR
            = new Parcelable.Creator<Puzzle>() {
        public Puzzle createFromParcel(Parcel in) {
            return new Puzzle(in);
        }

        public Puzzle[] newArray(int size) {
            return new Puzzle[size];
        }
    };


}
