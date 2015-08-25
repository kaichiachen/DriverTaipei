package hackntu2015.edu.yzu.drivertaipei.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import hackntu2015.edu.yzu.drivertaipei.Node.NodeConstruct;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeGas;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeParkingLot;
import hackntu2015.edu.yzu.drivertaipei.Node.NodeTraffic;
import hackntu2015.edu.yzu.drivertaipei.R;
import hackntu2015.edu.yzu.drivertaipei.utils.AutoResizeTextView;

/**
 * Created by andy on 8/25/15.
 */
public class CardLayout {

    LinearLayout mCardLayout;
    Button mNavigation;
    Button mPayment;
    GoogleMap mMap;
    Context ctx;

    public CardLayout(Context context, LinearLayout layout,GoogleMap mMap,Button nav,Button pay){
        mCardLayout = layout;
        this.mMap = mMap;
        mNavigation = nav;
        mPayment = pay;
        ctx = context;
    }

    public Marker getGasMarker(NodeGas nodeGas){
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(nodeGas.lat, nodeGas.lon))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_petrolstation)));
    }

    public void showGasCard(NodeGas nodeGas){
        mCardLayout.removeAllViews();
        LinearLayout.LayoutParams layout;

        ImageView categoryIcon;
        AutoResizeTextView categoryTitle;
        ImageView categoryMood;
        AutoResizeTextView categoryStatus;

        categoryIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryIcon.setLayoutParams(layout);
        categoryIcon.setImageResource(R.mipmap.petrolstation);
        mCardLayout.addView(categoryIcon);

        categoryTitle = new AutoResizeTextView(ctx);
        categoryTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.1f));
        categoryTitle.setText(nodeGas.name);
        categoryTitle.setTextColor(Color.BLACK);
        categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        categoryTitle.setGravity(Gravity.CENTER);
        mCardLayout.addView(categoryTitle);

        categoryMood = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryMood.setLayoutParams(layout);
        mCardLayout.addView(categoryMood);

        categoryStatus = new AutoResizeTextView(ctx);
        categoryStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.1f));
        categoryStatus.setTextColor(Color.RED);
        categoryStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        categoryStatus.setGravity(Gravity.LEFT | Gravity.CENTER);
        mCardLayout.addView(categoryStatus);

        if (nodeGas.hasOil) {
            categoryMood.setImageResource(R.mipmap.emoticon_happy);
            if (nodeGas.hasSelf) {
                categoryStatus.setText(R.string.self);
            } else {
                categoryStatus.setText(R.string.no_self);
            }
            categoryStatus.setTextColor(Color.parseColor("#e8a032"));
        } else {
            categoryStatus.setText(R.string.non_operating_in);
            categoryMood.setImageResource(R.mipmap.emoticon_sad);
            categoryStatus.setTextColor(Color.RED);
        }

        Animation amAlpha = new AlphaAnimation(0.0f, 1.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCardLayout.setVisibility(View.VISIBLE);
                mNavigation.setVisibility(View.VISIBLE);
                mPayment.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        mCardLayout.startAnimation(amAlpha);
        mNavigation.startAnimation(amAlpha);
    }

    public Marker getParkingLotMarker(NodeParkingLot nodeParkingLot){
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(nodeParkingLot.lat, nodeParkingLot.lon))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_parkinglot)));
    }

    public void showParkingLotCard(NodeParkingLot nodeParkingLot){
        mCardLayout.removeAllViews();
        LinearLayout.LayoutParams layout;

        ImageView categoryCarIcon;
        AutoResizeTextView categoryCarCount;
        ImageView categoryMotorIcon;
        AutoResizeTextView categoryMotrorCount;
        ImageView categoryMood;
        AutoResizeTextView categoryStatus;

        categoryCarIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryCarIcon.setImageResource(R.mipmap.parkingdark);
        categoryCarIcon.setLayoutParams(layout);
        mCardLayout.addView(categoryCarIcon);

        categoryCarCount = new AutoResizeTextView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.14f);
        if(nodeParkingLot.availableCar < 10)
            layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.15f);
        categoryCarCount.setLayoutParams(layout);
        categoryCarCount.setText("" + nodeParkingLot.availableCar);
        categoryCarCount.setTextColor(Color.BLACK);
        categoryCarCount.setGravity(Gravity.CENTER);
        mCardLayout.addView(categoryCarCount);

        categoryMotorIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryMotorIcon.setLayoutParams(layout);
        categoryMotorIcon.setImageResource(R.mipmap.parking_bike_dark);
        mCardLayout.addView(categoryMotorIcon);

        categoryMotrorCount = new AutoResizeTextView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.14f);
        if(nodeParkingLot.availableMotor < 10)
            layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.15f);
        categoryMotrorCount.setLayoutParams(layout);
        categoryMotrorCount.setText("" + nodeParkingLot.availableMotor);
        categoryMotrorCount.setTextColor(Color.BLACK);
        categoryMotrorCount.setGravity(Gravity.CENTER);
        mCardLayout.addView(categoryMotrorCount);

        categoryMood = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.14f);
        categoryMood.setLayoutParams(layout);
        mCardLayout.addView(categoryMood);

        categoryStatus = new AutoResizeTextView(ctx);
        categoryStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.1f));
        categoryStatus.setTextColor(Color.RED);
        categoryStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        categoryStatus.setGravity(Gravity.LEFT | Gravity.CENTER);
        mCardLayout.addView(categoryStatus);

        if (nodeParkingLot.availableCar > 0 ) {
            categoryMood.setImageResource(R.mipmap.emoticon_happy_green);
            categoryStatus.setText(R.string.parking);
            categoryStatus.setTextColor(Color.parseColor("#22ac38"));
        } else if(nodeParkingLot.availableMotor > 0){
            categoryMood.setImageResource(R.mipmap.emoticon_happy_green);
            categoryStatus.setText(R.string.parking);
            categoryStatus.setTextColor(Color.parseColor("#22ac38"));
        } else{
            categoryMood.setImageResource(R.mipmap.emoticon_sad);
            categoryStatus.setText(R.string.no_parking);
            categoryStatus.setTextColor(Color.RED);
        }


        Animation amAlpha = new AlphaAnimation(0.0f, 1.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCardLayout.setVisibility(View.VISIBLE);
                mNavigation.setVisibility(View.VISIBLE);
                mPayment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        mCardLayout.startAnimation(amAlpha);
        mNavigation.startAnimation(amAlpha);
        mPayment.startAnimation(amAlpha);
    }

    public Marker getConstructMarker(NodeConstruct nodeConstruct){
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(nodeConstruct.lat, nodeConstruct.lon))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_constructionsite)));
    }

    public void showConstructInfo(NodeConstruct nodeConstruct){
        mCardLayout.removeAllViews();
        LinearLayout.LayoutParams layout;

        ImageView categoryIcon;
        AutoResizeTextView categoryTitle;

        categoryIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.12f);
        categoryIcon.setLayoutParams(layout);
        if(nodeConstruct.isToday){ categoryIcon.setImageResource(R.mipmap.warning); }
        else{ categoryIcon.setImageResource(R.mipmap.constructionsite); }

        mCardLayout.addView(categoryIcon);

        categoryTitle = new AutoResizeTextView(ctx);
        categoryTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.08f));
        categoryTitle.setText(nodeConstruct.status);
        categoryTitle.setTextColor(Color.BLACK);
        categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        categoryTitle.setGravity(Gravity.CENTER | Gravity.LEFT);
        mCardLayout.addView(categoryTitle);

        Animation amAlpha = new AlphaAnimation(0.0f, 1.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCardLayout.setVisibility(View.VISIBLE);
                mNavigation.setVisibility(View.INVISIBLE);
                mPayment.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        mCardLayout.startAnimation(amAlpha);
    }

    public Marker getTrafficMarker(NodeTraffic nodeTraffic){
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(nodeTraffic.lat, nodeTraffic.lon))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_warning)));
    }

    public void showTrafficInfo(NodeTraffic nodeTraffic){
        mCardLayout.removeAllViews();
        LinearLayout.LayoutParams layout;

        ImageView categoryIcon;
        AutoResizeTextView categoryTitle;

        categoryIcon = new ImageView(ctx);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.12f);
        categoryIcon.setLayoutParams(layout);
        categoryIcon.setImageResource(R.mipmap.constructionsite);

        mCardLayout.addView(categoryIcon);

        categoryTitle = new AutoResizeTextView(ctx);
        categoryTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.08f));
        categoryTitle.setText(nodeTraffic.status);
        categoryTitle.setTextColor(Color.BLACK);
        categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        categoryTitle.setGravity(Gravity.CENTER | Gravity.LEFT);
        mCardLayout.addView(categoryTitle);

        Animation amAlpha = new AlphaAnimation(0.0f, 1.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCardLayout.setVisibility(View.VISIBLE);
                mNavigation.setVisibility(View.INVISIBLE);
                mPayment.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        mCardLayout.startAnimation(amAlpha);
    }

    public void removeCard(){
        Animation amAlpha = new AlphaAnimation(1.0f, 0.0f);
        amAlpha.setDuration(500);
        amAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCardLayout.setVisibility(View.INVISIBLE);
                mNavigation.setVisibility(View.INVISIBLE);
                mPayment.setVisibility(View.INVISIBLE);
            }
        });
        mCardLayout.startAnimation(amAlpha);
        if(mNavigation.getVisibility() == View.VISIBLE)
            mNavigation.startAnimation(amAlpha);
        if(mPayment.getVisibility() == View.VISIBLE)
            mPayment.startAnimation(amAlpha);
    }
}
