package com.example.my2do;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.my2do.Model.ToDoModel;
import com.example.my2do.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";

    private EditText editTextTitle, editTextDescription, editTextTaskDate;
    private Spinner spinnerCategory;
    private Button buttonAdd, buttonCancel;
    private DatePickerDialog picker;
    private MaterialCheckBox checkBoxStatus;
    private LinearLayout layoutTaskStatus;

    private DataBaseHelper myDb;

    private boolean mSpinnerInitialized;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    String categories[] = new String[]{
            "Task Category...",
            "Business",
            "Shopping",
            "Study",
            "Diet",
            "Workout"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheetInternal);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return inflater.inflate(R.layout.add_new_task_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle = view.findViewById(R.id.edit_text_task_title);
        editTextDescription = view.findViewById(R.id.edit_text_task_description);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        buttonAdd = view.findViewById(R.id.btn_add);
        buttonCancel = view.findViewById(R.id.btn_cancel);
        editTextTaskDate = view.findViewById(R.id.edit_text_task_date);
        checkBoxStatus = view.findViewById(R.id.checkbox_status);
        layoutTaskStatus = view.findViewById(R.id.layout_task_status);

        myDb = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;

            buttonAdd.setText("Edit");
            buttonAdd.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yellow));
            Drawable img = buttonAdd.getContext().getResources().getDrawable(R.drawable.ic_baseline_edit_white_24);
            buttonAdd.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            buttonAdd.setEnabled(false);
            buttonAdd.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_grey, null));

            layoutTaskStatus.setVisibility(View.VISIBLE);

            int taskStatus = bundle.getInt("taskStatus");

            if(taskStatus == 1){
                checkBoxStatus.setChecked(true);
                checkBoxStatus.setText("Done");
            }

            String taskTitle = bundle.getString("taskTitle");
            editTextTitle.setText(taskTitle);

            String taskDescription = bundle.getString("taskDescription");
            if (taskDescription != null) {
                editTextDescription.setText(taskDescription);
            }

            String taskDate = bundle.getString("taskDate");

            editTextTaskDate.setText(taskDate);

            String taskCategory = bundle.getString("taskCategory");
            if (taskCategory != null) {
                spinnerCategory.setSelection(Arrays.asList(categories).indexOf(taskCategory));
            }

//            if (editTextDescription.length() > 0 || spinnerCategory.getSelectedItem().toString().equals("Task Category...")) {
//                buttonAdd.setEnabled(false);
//                buttonAdd.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_grey, null));
//            }
        }

        final boolean finalIsUpdate = isUpdate;

        if (finalIsUpdate) {
            checkBoxStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setText("Done");
                    }
                    if (!isChecked) {
                        buttonView.setText("In Progress");
                    }
                    buttonAdd.setEnabled(true);
                    buttonAdd.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
                }
            });

            editTextTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().equals("")) {
                        buttonAdd.setEnabled(false);
                        buttonAdd.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dark_grey, null));
                    } else {
                        buttonAdd.setEnabled(true);
                        buttonAdd.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            editTextTaskDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    buttonAdd.setEnabled(true);
                    buttonAdd.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            editTextDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    buttonAdd.setEnabled(true);
                    buttonAdd.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (!mSpinnerInitialized) {
                        mSpinnerInitialized = true;
                        return;
                    } else {
                        buttonAdd.setEnabled(true);
                        buttonAdd.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });
        }


        editTextTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String dayStr = "";
                                String monthStr = "";
                                Log.i("Day", String.valueOf(dayOfMonth));
                                monthOfYear = monthOfYear + 1;

                                if (monthOfYear < 9) {
                                    monthStr = "0" + String.valueOf(monthOfYear);
                                }
                                if (dayOfMonth <= 9) {
                                    dayStr = "0" + String.valueOf(dayOfMonth);
                                }
                                if (monthOfYear > 9) {
                                    monthStr = String.valueOf(monthOfYear);
                                }
                                if (dayOfMonth > 9) {
                                    dayStr = String.valueOf(dayOfMonth);
                                }

                                editTextTaskDate.setText(dayStr + "/" + monthStr + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dateError = false;
                String taskTitle = editTextTitle.getText().toString();
                String taskDescription = editTextDescription.getText().toString();
                String taskCategory = spinnerCategory.getSelectedItem().toString();
                String taskDateStr = editTextTaskDate.getText().toString();
                int taskStatus = 0;
                if(checkBoxStatus.isChecked()){
                    taskStatus = 1;
                }
                Date taskDate = null;
                try {
                    taskDate = new SimpleDateFormat("dd/MM/yyyy").parse(taskDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                    dateError = true;
                }

                if (taskTitle.length() == 0) {
                    Toast.makeText(getContext(), "Task Title Can't be Empty", Toast.LENGTH_SHORT).show();
                }

                if (dateError) {
                    Toast.makeText(getContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
                } else if (finalIsUpdate) {
                    myDb.updateTask(bundle.getInt("id"), taskTitle, taskDateStr, taskDescription, taskCategory, taskStatus);
                    dismiss();
                    Log.i("TEST", "Called");
                    Log.i("TITLE", taskDescription);
                    Toast.makeText(getContext(), "\'" + taskTitle + "\' Edited", Toast.LENGTH_SHORT).show();
                } else {
                    ToDoModel item = new ToDoModel();
                    item.setTaskTitle(taskTitle);
                    item.setTaskCategory(taskCategory);
                    item.setTaskDescription(taskDescription);
                    item.setTaskDate(taskDateStr);
                    item.setTaskStatus(0);
                    myDb.insertTask(item);
                    dismiss();
                    Toast.makeText(getContext(), "\'" + taskTitle + "\' Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}