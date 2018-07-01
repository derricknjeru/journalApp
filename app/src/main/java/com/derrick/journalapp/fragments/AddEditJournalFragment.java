package com.derrick.journalapp.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.derrick.journalapp.R;
import com.derrick.journalapp.contracts.AddEditJournalContract;
import com.derrick.journalapp.databinding.FragmentAddEditJournalBinding;
import com.derrick.journalapp.pojos.Journal;
import com.derrick.journalapp.utilities.ActivityUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.derrick.journalapp.ui.AddEditJournalActivity.REQUEST_ADD_JOURNAL;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditJournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditJournalFragment extends Fragment implements AddEditJournalContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARGUMENT_EDIT_JOURNAL_ID = "JOURNAL_EDIT_ID";
    public static final String ARGUMENT_USER_ID = "ARGUMENT_USER_ID";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AddEditJournalContract.Presenter mPresenter;

    private FragmentAddEditJournalBinding mBinding;
    private Calendar mDateCalender;
    private Calendar mTimeCalender;
    private String mFinalDate;
    private String mFinalTime;


    public AddEditJournalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditJournalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditJournalFragment newInstance(String param1, String param2) {
        AddEditJournalFragment fragment = new AddEditJournalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mDateCalender = Calendar.getInstance();
        mTimeCalender = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_edit_journal, container, false);
        View v = mBinding.getRoot();

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_edit_journal);

        fab.setImageResource(R.drawable.ic_done_white_24dp);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * setting an order id using timeStamp
                 */
                long orderId = -1 * new Date().getTime();

                Journal journal = new Journal(mBinding.addTitle.getText().toString(),
                        mBinding.addDescription.getText().toString(), mBinding.edtDate.getText().toString(),
                        mBinding.edtTime.getText().toString(), orderId);

                mPresenter.saveJournal(journal);
            }
        });

        mBinding.edtDate.setOnTouchListener(datePicker);
        mBinding.edtTime.setOnTouchListener(timePicker);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showEmptyJournalError() {
        Snackbar.make(mBinding.getRoot(), getString(R.string.empty_journal_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showPostedSuccessFully() {

    }

    @Override
    public void showNotPosted() {

    }

    @Override
    public void showJournalsList() {
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }

    }

    @Override
    public void setTitle(String title) {
        mBinding.addTitle.setText(title);
    }

    @Override
    public void setDate(String date) {
        mBinding.edtDate.setText(date);
    }

    @Override
    public void setTime(String time) {
        mBinding.edtTime.setText(time);
    }

    @Override
    public void setDescription(String description) {
        mBinding.addDescription.setText(description);
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(AddEditJournalContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            mDateCalender.set(Calendar.YEAR, year);
            mDateCalender.set(Calendar.MONTH, monthOfYear);
            mDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            setDateCalender(mDateCalender);

        }

    };

    final TimePickerDialog.OnTimeSetListener mTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int time) {
            mTimeCalender.set(Calendar.HOUR, hour);
            mTimeCalender.set(Calendar.MINUTE, time);
            setTimeCalender(mTimeCalender);
        }
    };

    private void setDateCalender(Calendar myCalendar) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);
        SimpleDateFormat mSimpleDateFormat1 = new SimpleDateFormat("yyyy MMM dd", Locale.US);

        mBinding.edtDate.setError(null);

        mFinalDate = mSimpleDateFormat.format(myCalendar.getTime());
        mBinding.edtDate.setText(mSimpleDateFormat1.format(myCalendar.getTime()));

        ActivityUtils.hideKeyBoard(mBinding.edtDate, getActivity());
    }


    private void setTimeCalender(Calendar mTimeCalender) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:a", Locale.US);

        mBinding.edtTime.setError(null);

        mFinalTime = mSimpleDateFormat.format(mTimeCalender.getTime());

        mBinding.edtTime.setText(mFinalTime);

        ActivityUtils.hideKeyBoard(mBinding.edtDate, getActivity());
    }


    View.OnTouchListener datePicker = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setDateOnTouch();
            }

            return false;
        }
    };

    View.OnTouchListener timePicker = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setTimeOnTouch();
            }

            return false;
        }
    };


    private void setTimeOnTouch() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), mTime, mTimeCalender
                .get(Calendar.HOUR), mTimeCalender.get(Calendar.SECOND), true);

        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();

            }
        });

        timePickerDialog.show();
    }


    private void setDateOnTouch() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, mDateCalender
                .get(Calendar.YEAR), mDateCalender.get(Calendar.MONTH),
                mDateCalender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();

            }
        });

        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_JOURNAL) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }


}
