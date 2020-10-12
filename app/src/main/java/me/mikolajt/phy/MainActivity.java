package me.mikolajt.phy;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import me.mikolajt.phy.events.UnitIconClickListener;
import me.mikolajt.phy.units.*;

import java.util.*;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout scrollLayout = findViewById(R.id.scrollLayout);
        scrollLayout.setConstraintSet(null);
        //int i = 0;
        List<View> iconsList = new ArrayList<>();
        View lastIcon = null;
        Log.i("[app]", "AFASFASFDAFSD" + new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.METRE, BaseUnit.SECOND).getBaseUnitsWithExponents());
        List<Unit> unitList = new ArrayList<>();
        Collections.addAll(unitList, BaseUnit.values());
        Collections.addAll(unitList, DerivedUnit.values());
        int wholeAmount = (unitList.size()/3);
        int moduloAmount = (((unitList.size()%3) == 0) ? 0 : 1);
        int totalAmount = wholeAmount + moduloAmount;
        for(int i=0; i<totalAmount; i++) {
            View iconRow = getLayoutInflater().inflate(R.layout.icon_row, null);
            iconRow.setId(View.generateViewId());
            scrollLayout.addView(iconRow);
            int rowCount = 3;
            if((i==(totalAmount-1)) && (moduloAmount > 0)){
                rowCount = (DerivedUnit.values().length%3);
            }
            for(int j=0; j<rowCount; j++){
                View currentIconView = ((ViewGroup) iconRow).getChildAt(j);
                TextView iconText = (TextView) ((ViewGroup) currentIconView).getChildAt(0);
                TextView bottomText = (TextView) ((ViewGroup) currentIconView).getChildAt(1);
                Unit currentUnit = unitList.get(i*3+j);
                currentIconView.setOnClickListener((new UnitIconClickListener(currentUnit, this)));
                iconText.setText(currentUnit.getSymbol());
                bottomText.setText(currentUnit.getName());
            }
            ViewGroup.LayoutParams params = iconRow.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            iconRow.setLayoutParams(params);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(scrollLayout);
            constraintSet.connect(iconRow.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constraintSet.connect(iconRow.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constraintSet.connect(iconRow.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(120*i));
            constraintSet.applyTo(scrollLayout);

        }
         /*ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) scrollLayout.getLayoutParams();
        params.constrainedHeight = true;
        params.constrainedWidth = true;*/
        /*for(Unit unit : DerivedUnit.values()){
            Log.i("[app]", unit.getSymbol());
            View unitIcon = getLayoutInflater().inflate(R.layout.calculation_icon, null);
            unitIcon.setId(View.generateViewId());
            scrollLayout.addView(unitIcon);
            TextView symbolText = (TextView)((ViewGroup) unitIcon).getChildAt(0);
            TextView bottomText = (TextView)((ViewGroup) unitIcon).getChildAt(1);
            symbolText.setText(symbolText.getText() + unit.getSymbol());
            bottomText.setText(bottomText.getText() + unit.getName());

           ConstraintSet constraintSet = new ConstraintSet();
           constraintSet.clone(scrollLayout);
           switch(i%3){
                case 0:
                    Log.i("[app]", "case 0");
                    //dp 0
                    constraintSet.connect(unitIcon.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
                    iconsList.add(unitIcon);
                    break;
                case 1:
                    Log.i("[app]", "case 1");
                    //dp 120
                    constraintSet.connect(unitIcon.getId(), ConstraintSet.LEFT, lastIcon.getId(), ConstraintSet.RIGHT);
                    iconsList.add(unitIcon);
                    break;
                case 2:
                    Log.i("[app]", "case 2");
                    //dp 120
                    constraintSet.connect(unitIcon.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
                    Log.i("[app]", "ustawiam");
                    iconsList.add(unitIcon);
                    /*int[] ids = new int[3];
                    float[] weights = new float[3];
                    for(int j=0; j<3; j++){
                        ids[j] = iconsList.get(j).getId();
                        Log.i("[app]", "id z " + j + " - " + ids[j]);
                        weights[j] = 1;
                    }
                    constraintSet.createHorizontalChain(ConstraintSet.PARENT_ID, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, ids, weights, ConstraintSet.CHAIN_PACKED);
                    iconsList.clear();
                    break;
            }
            constraintSet.connect(unitIcon.getId(), ConstraintSet.TOP, scrollLayout.getId(), ConstraintSet.TOP, dpToPx(20 + (i/3)*200));
            constraintSet.applyTo(scrollLayout);
            lastIcon = unitIcon;
            i++;
        }*/

    }

    private int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
