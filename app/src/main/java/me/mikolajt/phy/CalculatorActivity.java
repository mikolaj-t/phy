package me.mikolajt.phy;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.google.android.material.textfield.TextInputEditText;
import me.mikolajt.phy.events.*;
import me.mikolajt.phy.units.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.*;

public class CalculatorActivity extends AppCompatActivity {

    private Unit unitToCalculate;
    private Operable currentlyCovered;
    private FormulaSuggester formulaSuggester;
    // ArrayList<TextInputEditText> textInputFields;
    private Set<FormulaSuggestion> appliedSuggestions;
    //private Set<InputFormula> inputFormulas;
    private Map<FormulaSuggestion, Float> inputValues;
    public int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new ReadyToReceiveUnitInfoEvent());
        Log.i("[app]", "registered the listener!");
        this.currentlyCovered = new Operation(TypeOfOperation.MULTIPLICATION, BaseUnit.SECOND, new Operation(TypeOfOperation.REVERSAL, BaseUnit.SECOND));
        this.appliedSuggestions = new HashSet<>();
        this.inputValues = new LinkedHashMap<>();
        formulaSuggester = new FormulaSuggester();
        createCalculationRow(i);
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onReceivedUnitInfo(UnitInfoEvent event){
        unitToCalculate = event.getUnit();
        Log.i("[app]", " event received!");
        TextView tv = findViewById(R.id.calculatorUnitText);
        tv.setText(unitToCalculate.getName());
        EventBus.getDefault().unregister(this);
    }

    public void createCalculationRow(int i){
        ConstraintLayout scrollLayout = findViewById(R.id.calculatorScrollLayout);
        View calculationRow = getLayoutInflater().inflate(R.layout.calculation_row, null);
        calculationRow.setId(View.generateViewId());
        scrollLayout.addView(calculationRow);

        ViewGroup.LayoutParams params = calculationRow.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        calculationRow.setLayoutParams(params);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(scrollLayout);
        constraintSet.connect(calculationRow.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(i * 200));
        constraintSet.applyTo(scrollLayout);

        View dropdownMenuView = ((ViewGroup)calculationRow).getChildAt(0);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) ((ViewGroup)((ViewGroup)dropdownMenuView).getChildAt(0)).getChildAt(0);
        Set<FormulaSuggestion> suggestionSet = formulaSuggester.suggest(unitToCalculate, currentlyCovered);
        FormulaSuggestion[] suggestionArray = suggestionSet.toArray(new FormulaSuggestion[0]);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this, R.layout.dropdown_menu_item, suggestionArray));
       // autoCompleteTextView.setOnItemSelectedListener(new SuggestionChooseListener(suggestionArray));
        SuggestionChooseListener suggestionChooseListener = new SuggestionChooseListener(suggestionArray, this);
        autoCompleteTextView.setOnItemClickListener(suggestionChooseListener);

        TextInputEditText textInputEditText = (TextInputEditText) ((ViewGroup)(((ViewGroup)((ViewGroup)calculationRow).getChildAt(1)).getChildAt(0))).getChildAt(0);//.getChildAt(1);
        textInputEditText.addTextChangedListener(new SuggestionValueInputListener(textInputEditText,this, suggestionChooseListener));

        //todo odczytywanie wartosci - moze mapa fieldow tekstowych z listenerami????
        //todo i wtedy jak jest gotowe sprawdzac zmiany
        //textInputFields.add(textInputEditText);

        View deleteButton = ((ViewGroup)calculationRow).getChildAt(2);
        deleteButton.setOnClickListener(new RemoveCalculatorRowListener(suggestionArray[0], this, calculationRow));
    }

    private float calculateResult(){
        float result = 1;
        for(Map.Entry<FormulaSuggestion, Float> entry : inputValues.entrySet()){
            Log.i("[app]", "value " + entry.getValue() + " unit " + entry.getKey().getUnit().getBaseUnitsWithExponents() + " type " + entry.getKey().getTypeOfOperation().name());
            switch(entry.getKey().getTypeOfOperation()){
                case MULTIPLICATION:
                    result *= entry.getValue();
                    break;
                case REVERSAL:
                    result /= entry.getValue();
                    break;
            }
        }
        return result;
    }

    public void showResult(){
        TextView calculatorResultText = findViewById(R.id.calculatorScore);
        calculatorResultText.setText(calculateResult() + " " + unitToCalculate.getSymbol());
    }

    public void resetResult(){
        TextView calculatorResultText = findViewById(R.id.calculatorScore);
        calculatorResultText.setText("");
    }

    private int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void addFormulaValue(FormulaSuggestion suggestion, float value){
        this.inputValues.put(suggestion, value);
    }

    public void applySuggestion(FormulaSuggestion suggestion){
        this.appliedSuggestions.add(suggestion);
        this.addFormulaValue(suggestion, 0);
        addToCovered(suggestion);
    }

    public void removeSuggestion(FormulaSuggestion suggestion){
        this.appliedSuggestions.remove(suggestion);
        this.inputValues.remove(suggestion);
        removeFromCovered(suggestion);
        resetResult();
    }

    public void replaceSuggestion(FormulaSuggestion old, FormulaSuggestion replacement){
        this.inputValues.put(replacement, this.inputValues.get(old));
        this.inputValues.remove(old);
        this.appliedSuggestions.add(replacement);
        this.appliedSuggestions.remove(old);
        addToCovered(replacement);
        removeFromCovered(old);
    }

    private void addToCovered(FormulaSuggestion suggestion){
        Log.i("[app]", "prev covered: " + currentlyCovered.getBaseUnitsWithExponents() + " unit added: " + suggestion.getUnit().getBaseUnitsWithExponents() + " " + suggestion.getTypeOfOperation().name());
        switch(suggestion.getTypeOfOperation()){
            case MULTIPLICATION:
                this.currentlyCovered = new Operation(TypeOfOperation.MULTIPLICATION, this.currentlyCovered, suggestion);
                break;
            case REVERSAL:
                this.currentlyCovered = new Operation(TypeOfOperation.MULTIPLICATION, this.currentlyCovered, new Operation(TypeOfOperation.REVERSAL, suggestion));
                break;

        }
        Log.i("[app]", "covered: " + currentlyCovered.getBaseUnitsWithExponents());
    }

    private void removeFromCovered(FormulaSuggestion suggestion){
        Log.i("[app]", "prev covered: " + currentlyCovered.getBaseUnitsWithExponents() + " unit removed: " + suggestion.getUnit().getBaseUnitsWithExponents() + " " + suggestion.getTypeOfOperation().name());
        switch(suggestion.getTypeOfOperation()){
            case MULTIPLICATION:
                this.currentlyCovered = new Operation(TypeOfOperation.MULTIPLICATION, this.currentlyCovered, new Operation(TypeOfOperation.REVERSAL, suggestion));
                break;
            case REVERSAL:
                this.currentlyCovered = new Operation(TypeOfOperation.MULTIPLICATION, this.currentlyCovered, suggestion);
                break;
        }
        Log.i("[app]", "covered: " + currentlyCovered.getBaseUnitsWithExponents());
    }

    public boolean isFullyCovered(){
        return this.currentlyCovered.getBaseUnitsWithExponents().equals(this.unitToCalculate.getBaseUnitsWithExponents());
    }
}
