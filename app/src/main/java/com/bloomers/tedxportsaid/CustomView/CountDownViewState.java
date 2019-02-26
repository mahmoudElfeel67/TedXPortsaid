package com.bloomers.tedxportsaid.CustomView;


import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
class CountDownViewState extends View.BaseSavedState {
    long startDuration;
    long currentDuration;
    boolean timerRunning;
    public static final Creator<CountDownViewState> CREATOR = new Creator<CountDownViewState>() {
        public CountDownViewState createFromParcel(Parcel in) {
            return new CountDownViewState(in);
        }

        public CountDownViewState[] newArray(int size) {
            return new CountDownViewState[size];
        }
    };

    CountDownViewState(Parcelable superState) {
        super(superState);
    }

    private CountDownViewState(Parcel source) {
        super(source);
        this.startDuration = source.readLong();
        this.currentDuration = source.readLong();
        this.timerRunning = source.readInt() == 1;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeLong(this.startDuration);
        out.writeLong(this.currentDuration);
        out.writeInt(this.timerRunning ? 1 : 0);
    }
}
