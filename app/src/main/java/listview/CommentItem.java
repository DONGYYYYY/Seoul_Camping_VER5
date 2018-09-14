package listview;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentItem implements Parcelable {
    private int number;
    private String id;
    private String text;
    private float star;
    private String type;
    private String regdate;

    public CommentItem() {

    }
    public CommentItem(String id, String text , float star,String type)
    {
        this.id = id;
        this.text =text;
        this.star = star;
        this.type = type;
        this.regdate=regdate;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }
    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public CommentItem(Parcel src) {
        id = src.readString();
        text = src.readString();
        star = src.readFloat();
        type = src.readString();
        regdate = src.readString();
    }

    public static final Creator CREATOR = new Creator() {
        public CommentItem createFromParcel(Parcel src) {
            return new CommentItem(src);
        }

        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    @Override
    public int describeContents() { // 필수 오버라이드
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { // 필수 오버라이드
        dest.writeString(id);
        dest.writeString(text);
        dest.writeFloat(star);
        dest.writeString(type);
    }
}
