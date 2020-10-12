package me.mikolajt.phy.events;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import me.mikolajt.phy.CalculatorActivity;
import me.mikolajt.phy.units.Unit;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class UnitIconClickListener implements View.OnClickListener {

    private final Unit unit;
    private final Activity activity;

    public UnitIconClickListener(Unit unit, Activity activity){
        this.unit = unit;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        Log.i("[app]", unit.getName() + " was clicked!");
        Intent intent = new Intent(activity, CalculatorActivity.class);
        activity.startActivity(intent);
        Log.i("[app]", unit.getName() + "sent an event!");
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onReadyToReceiveUnitInfo(ReadyToReceiveUnitInfoEvent event){
        EventBus.getDefault().post(new UnitInfoEvent(unit));
        EventBus.getDefault().unregister(this);
    }

}
